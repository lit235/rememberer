package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.sevices.RecordsService;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.exceptions.BadRequestException;
import com.amatsuka.rememberer.web.exceptions.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exceptions.ValidationException;
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
class RecordController {

    private RecordsService recordService;

    @Autowired
    public RecordController(RecordsService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public RecordResource create(@RequestBody @Valid StoreRecordRequest recordRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        try {
            return this.recordService.storeRecord(recordRequest);
        } catch (RecordNotStoredException e) {
            throw new BadRequestException();
        }

    }

    @GetMapping("{code}")
    public RecordResource findOneByCode(@PathVariable("code") String code, @RequestParam(value = "password", required = false) String password) {
        RecordResource recordResource = this.recordService.getRecordByCode(code, password);

        if (recordResource == null) {
            throw new ResourceNotFoundException();
        }

        return recordResource;
    }
}
