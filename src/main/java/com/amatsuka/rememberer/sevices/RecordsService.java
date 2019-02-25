package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.entities.Record;
import com.amatsuka.rememberer.repositories.RecordsRepository;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RecordsService {

    private RecordsRepository recordsRepository;

    @Autowired
    public RecordsService(RecordsRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }

    public RecordResource storeRecord(StoreRecordRequest recordRequest) {
        Record record = new Record(recordRequest.getText(), this.generateCode());
        Record result;

        try {
            result = recordsRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new RecordNotStoredException(e);
        }

        if (result == null) {
            throw new RecordNotStoredException();
        }

        return new RecordResource(record.getText(), record.getCode());
    }

    public RecordResource getRecordByCode(String code) {
        Record record = recordsRepository.getRecordByCode(code);

        return record == null ? null : new RecordResource(record.getText(), record.getCode());
    }

    private String generateCode() {
        Faker faker = new Faker();

        return faker.funnyName().name();
    }


}
