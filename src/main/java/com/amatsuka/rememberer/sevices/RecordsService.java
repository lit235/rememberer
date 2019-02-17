package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.dto.RecordDTO;
import com.amatsuka.rememberer.entities.Record;
import com.amatsuka.rememberer.repositories.RecordsRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordsService {

    private RecordsRepository recordsRepository;

    @Autowired
    public RecordsService(RecordsRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }

    public boolean storeRecord(RecordDTO recordDTO) {
        Record record = new Record(recordDTO.getText(), this.generateCode());

        Record result = recordsRepository.save(record);

        return result != null;
    }

    public RecordDTO getRecordByCode(String code) {
        Record record = recordsRepository.getRecordByCode(code);

        return new RecordDTO(record.getText());
    }

    private String generateCode() {
        Faker faker = new Faker();

        return faker.funnyName().name();
    }


}
