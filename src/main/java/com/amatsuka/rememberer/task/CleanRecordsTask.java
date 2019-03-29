package com.amatsuka.rememberer.task;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CleanRecordsTask implements Runnable {

    @Autowired
    RecordsService recordsService;

    @Scheduled(fixedDelay = 1000 * 3600)
    @Override
    public void run() {
        List<RecordDto> records = this.recordsService.getExpiredRecords();
        this.recordsService.deleteRecords(records);
    }
}
