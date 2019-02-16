package com.amatsuka.rememberer.web;

import com.amatsuka.rememberer.requests.StoreRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {
    public void index() {

    }

    @PostMapping
    public void store(@Valid StoreRecord storeRecord, Errors errors) {
        if (errors.hasErrors()) {
            log.error(errors.getAllErrors().toString());
        } else {
            log.info(storeRecord.toString());
        }
    }

    public void update() {

    }

    public void destroy() {

    }

    public void show() {

    }
}
