package com.amatsuka.rememberer.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class StoreApiClientRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "ClientId id is required")
    private String clientId;
}