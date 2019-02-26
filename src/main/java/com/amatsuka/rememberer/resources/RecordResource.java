package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordResource {

    private Long id;

    private String text;

    private String code;

    private String passwordHash;
}
