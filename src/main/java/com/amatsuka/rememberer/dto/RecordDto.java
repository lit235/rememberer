package com.amatsuka.rememberer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDto {

    private Long id;

    private String text;

    private String code;

    private String passwordHash;

    private Date createdAt;
}
