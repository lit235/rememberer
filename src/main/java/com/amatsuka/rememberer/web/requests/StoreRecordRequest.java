package com.amatsuka.rememberer.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class StoreRecordRequest {

    @NotBlank(message = "Text is required")
    private String text;

    private String password;
}