package com.amatsuka.rememberer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiClientDto {
    private Long id;

    private String name;

    private String clientId;

    private Long userId;
}
