package com.amatsuka.rememberer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RecordDTO {

    @NotBlank(message = "Text is required")
    private String text;

    private String code;
}
