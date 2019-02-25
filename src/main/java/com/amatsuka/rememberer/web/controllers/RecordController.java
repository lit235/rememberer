package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.sevices.RecordsService;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public RecordResource store(@Valid StoreRecordRequest recordRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errors.getAllErrors().toString());
        }

        try {
            return this.recordService.storeRecord(recordRequest);
        } catch (RecordNotStoredException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record not saved", e);
        }

    }

    @GetMapping("{code}")
    public RecordResource show(@PathVariable("code") String code) {
        RecordResource recordResource = this.recordService.getRecordByCode(code);

        if (recordResource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        }

        return recordResource;
    }
}
