package com.learning.keycloakadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

}
