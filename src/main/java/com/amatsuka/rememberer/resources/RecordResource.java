package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordResource {

    private Long id;

    private String text;

    private String code;

    private String passwordHash;
}
