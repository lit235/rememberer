package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RecordResource {

    @NotBlank(message = "Text is required")
    private String text;

    private String code;
}
