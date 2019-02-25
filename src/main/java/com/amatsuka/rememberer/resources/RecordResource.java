package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class RecordResource {

    @Nullable
    private Long id;

    private String text;

    private String code;

    private String passwordHash;
}
