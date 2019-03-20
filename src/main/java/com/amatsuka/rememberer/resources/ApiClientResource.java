package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiClientResource {
    private Long id;

    private String name;

    private String clientId;
}
