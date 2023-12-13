package com.learning.keycloakadapter.keycloak;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
public class KeycloakClient {

    @Value("${keycloak.url}")
    String keycloakUrl;

    @Value("${keycloak.realm}")
    String realm;

    @Value("${keycloak.clientId}")
    String clientId;

    @Value("${keycloak.grantType}")
    String grantType;

    @Value("${keycloak.username}")
    String username;

    @Value("${keycloak.password}")
    String password;

    @Value("${keycloak.admin-realm}")
    String adminRealm;

    Keycloak keycloak;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .clientId(clientId)
                .grantType(grantType)
                .username(username)
                .password(password)
                .realm(realm)
                .build();
    }

    public RealmResource getAdminResource() {
        return keycloak.realm(adminRealm);
    }

}
