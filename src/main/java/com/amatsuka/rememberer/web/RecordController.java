package com.amatsuka.rememberer.web;

import com.amatsuka.rememberer.dto.RecordDTO;
import com.amatsuka.rememberer.sevices.RecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {

    private RecordsService recordService;

    @Autowired
    public RecordController(RecordsService recordService) {
        this.recordService = recordService;
    }

    public void index() {

    }

    @PostMapping
    public @ResponseBody String store(@Valid RecordDTO recordDTO, Errors errors) {
        boolean result = false;

        if (errors.hasErrors()) {
            log.error(errors.getAllErrors().toString());
        } else {
            result = this.recordService.storeRecord(recordDTO);
        }

        if (!result) {
            return "Some store error";
        }

        return "Success";
    }

    public void update() {

    }

    public void destroy() {

    }

    @GetMapping("{code}")
    public RecordDTO show(@PathVariable("code") String code) {
        RecordDTO recordDTO = this.recordService.getRecordByCode(code);

        return recordDTO;
    }
}
