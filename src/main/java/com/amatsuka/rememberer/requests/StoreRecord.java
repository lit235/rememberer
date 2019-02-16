package com.amatsuka.rememberer.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class StoreRecord {

    @NotBlank(message = "Text is required")
    private String text;
}
