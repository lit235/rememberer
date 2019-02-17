package com.amatsuka.rememberer.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class RecordDTO {

    @NotBlank(message = "Text is required")
    private String text;

    public RecordDTO(String text) {
    }
}
