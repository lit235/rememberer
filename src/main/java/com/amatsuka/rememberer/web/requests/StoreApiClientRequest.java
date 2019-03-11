package com.amatsuka.rememberer.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class StoreApiClientRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "User id is required")
    private Long userId;
}