package com.learning.keycloakadapter.service;

import com.learning.keycloakadapter.keycloak.KeycloakClient;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class KeycloakAdminService {

    private final KeycloakClient keycloakClient;

    @Autowired
    public KeycloakAdminService(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public Response createUser(UserRepresentation user) {
        return keycloakClient.getAdminResource().users().create(user);
    }

    public GroupRepresentation getGroupByGroupName(String groupName) {
        return keycloakClient.getAdminResource().groups().groups().stream().filter(groupRepresentation -> groupRepresentation.getName().equals(groupName)).findAny().orElse(null);
    }

    public List<UserRepresentation> listAllUsers() {
        return keycloakClient.getAdminResource().users().list();
    }

    public boolean usernameExists(String username) {
        Optional<UserRepresentation> userRepresentation = keycloakClient.getAdminResource().users()
                .search(username, true).stream().findAny();
        return userRepresentation.isPresent();
    }

    public void clearUserCache() {
        keycloakClient.getAdminResource().clearUserCache();
    }

    public void logout(Long userId) {
        keycloakClient.getAdminResource().users().get(userId.toString()).logout();
    }

    public UserRepresentation getUser(String userId) {
        return keycloakClient.getAdminResource().users().get(userId).toRepresentation();
    }

    public boolean updateUser(UserRepresentation user) {
        boolean exists = usernameExists(user.getUsername());
        if (!exists) {
            log.error("User {} does not exist in keycloak", user.getUsername());
            return false;
        }

        keycloakClient.getAdminResource().users().get(user.getId()).update(user);
        return true;
    }

    @Transactional
    public void deleteUser(String userId) {
        keycloakClient.getAdminResource().users().delete(userId);
    }

    public Optional<UserRepresentation> getUserByEmail(String email) {
        return keycloakClient.getAdminResource().users().search(null, null, null, email, null, 1).stream().findAny();
    }

    public Optional<UserRepresentation> getUserByUsername(String username) {
        return keycloakClient.getAdminResource().users().search(username, null, null, null, null, 1).stream().findAny();
    }

}
