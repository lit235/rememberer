package com.amatsuka.rememberer.tasks;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.services.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class CleanRecords implements Runnable {

    @Autowired
    RecordsService recordsService;

    @Scheduled(fixedDelay = 1000 * 3600)
    @Override
    public void run() {
        List<RecordDto> records = this.recordsService.getExpiredRecords();
        this.recordsService.deleteRecords(records);
    }
}
