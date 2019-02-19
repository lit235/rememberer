package com.amatsuka.rememberer.web;

import com.amatsuka.rememberer.dto.RecordDTO;
import com.amatsuka.rememberer.sevices.RecordsService;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotFoundException;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/records")
public class RecordController {

    private RecordsService recordService;

    @Autowired
    public RecordController(RecordsService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public RecordDTO store(@Valid RecordDTO recordDTO, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errors.getAllErrors().toString());
        }

        try {
            return this.recordService.storeRecord(recordDTO);
        } catch (RecordNotStoredException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record not saved", e);
        }

    }

    @GetMapping("{code}")
    public RecordDTO show(@PathVariable("code") String code) {
        RecordDTO recordDTO= this.recordService.getRecordByCode(code);

        if (recordDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }

        return recordDTO;
    }
}
