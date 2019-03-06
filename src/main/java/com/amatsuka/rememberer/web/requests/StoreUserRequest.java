package com.amatsuka.rememberer.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class StoreUserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    private String name;
}