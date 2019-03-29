package com.amatsuka.rememberer.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdateUserRequest {

    private String username;

    private String name;
}