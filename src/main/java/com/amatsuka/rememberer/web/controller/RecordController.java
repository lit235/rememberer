package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.service.RecordsService;
import com.amatsuka.rememberer.web.exception.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exception.ValidationException;
import com.amatsuka.rememberer.web.request.StoreRecordRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(value = "Records", description = "Создание и получение созданных заметок")
@RestController
@RequestMapping("/api/records")
class RecordController {

    private RecordsService recordService;

    @Autowired
    public RecordController(RecordsService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecordDto create(@RequestBody @Valid StoreRecordRequest recordRequest,  @ApiIgnore Errors errors) {
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
