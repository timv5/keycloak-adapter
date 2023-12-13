package com.learning.keycloakadapter.service;

import com.learning.keycloakadapter.dto.CustomerDTO;
import com.learning.keycloakadapter.exception.CustomerException;
import com.learning.keycloakadapter.exception.RequestValidationException;
import com.learning.keycloakadapter.model.CustomerEntity;
import com.learning.keycloakadapter.repository.CustomerRepository;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           KeycloakAdminService keycloakAdminService
    ) {
        this.customerRepository = customerRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    @Transactional
    public void create(CustomerDTO customerDTO) {
        validate(customerDTO);

        CustomerEntity customerEntity = buildCustomerEntity(customerDTO);
        customerEntity = customerRepository.save(customerEntity);

        UserRepresentation userRepresentation = mapToKeycloakUser(customerEntity, customerEntity.getCustomerId(), customerDTO.getPassword());
        try (Response keycloakResponse = keycloakAdminService.createUser(userRepresentation)) {
            if (keycloakResponse.getStatus() != Response.Status.CREATED.getStatusCode()) {
                log.error("Error while creating customer in keycloak, error status: {}", keycloakResponse.getStatus());
                throw new CustomerException("Error while creating customer in keycloak");
            }
        }
    }

    private UserRepresentation mapToKeycloakUser(CustomerEntity customer, Integer customerId, String password) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(customer.getUsername());
        userRepresentation.setEmail(customer.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(List.of(getPasswordCredentialRepresentation(password)));
        userRepresentation.singleAttribute("customer_id", String.valueOf(customerId));

        return userRepresentation;
    }

    private CredentialRepresentation getPasswordCredentialRepresentation(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    private CustomerEntity buildCustomerEntity(CustomerDTO customerDTO) {
        return CustomerEntity.builder()
                .email(customerDTO.getEmail())
                .lastName(customerDTO.getLastName())
                .username(customerDTO.getUsername())
                .firstName(customerDTO.getFirstName())
                .build();
    }

    private void validate(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            log.error("Missing request");
            throw new RequestValidationException("Invalid request");
        }

        if (StringUtils.isAnyEmpty(customerDTO.getUsername(), customerDTO.getEmail(), customerDTO.getPassword())) {
            log.error("Missing request params");
            throw new RequestValidationException("Missing request params");
        }
    }

}
