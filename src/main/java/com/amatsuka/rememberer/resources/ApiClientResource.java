package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiClientResource {
    private Long id;

    private String name;

    private String clientId;

    private String secret;
}
