package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.sevices.RecordsService;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.exceptions.BadRequestException;
import com.amatsuka.rememberer.web.exceptions.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exceptions.ValidationException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/records")
class RecordController {

    private RecordsService recordService;

    @Autowired
    public RecordController(RecordsService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public RecordDto create(@RequestBody @Valid StoreRecordRequest recordRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        return this.recordService.storeRecord(recordRequest);

    }

    @GetMapping("{code}")
    public RecordDto findOneByCode(@PathVariable("code") String code, @RequestParam(value = "password", required = false) String password) {
        RecordDto recordDto = this.recordService.getRecordByCode(code, password);

        if (recordDto == null) {
            throw new ResourceNotFoundException();
        }

        return recordDto;
    }
}
