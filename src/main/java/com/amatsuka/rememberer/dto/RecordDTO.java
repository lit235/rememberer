package com.amatsuka.rememberer.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class RecordDTO {

    @NotBlank(message = "Text is required")
    private String text;

    private String code;

    public RecordDTO(String text, String code) {
        this(text);
        this.code = code;
    }

    public RecordDTO(String text) {
        this.text = text;
    }
}
