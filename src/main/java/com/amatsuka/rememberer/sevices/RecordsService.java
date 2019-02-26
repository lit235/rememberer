package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.mappers.RecordMapper;
import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.domain.entities.Record;
import com.amatsuka.rememberer.domain.repositories.RecordsRepository;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotEncryptedException;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class RecordsService {

    private final RecordsRepository recordsRepository;
    private final RecordEncryptService recordEncryptService;

    @Autowired
    public RecordsService(RecordsRepository recordsRepository, RecordEncryptService recordEncryptService) {
        this.recordsRepository = recordsRepository;
        this.recordEncryptService = recordEncryptService;
    }

    //TODO организовать логику сохраниения зашифрованных и незашифрованных данных
    public RecordResource storeRecord(StoreRecordRequest recordRequest) {

        RecordResource recordResource = this.storeRecord(RecordMapper.INSTANCE.storeRecordRequestToRecordResource(recordRequest));

        if (recordRequest.getPassword() != null && !recordRequest.getPassword().isEmpty()) {
            recordResource = this.recordEncryptService.encrypt(recordResource, recordRequest.getPassword());
        }

        return recordResource;
    }

    public RecordResource storeRecord(RecordResource recordResource) {

        recordResource.setCode(this.generateCode());
        Record record = RecordMapper.INSTANCE.recordResourceToRecord(recordResource);

        Record result;

        try {
            result = recordsRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new RecordNotStoredException(e);
        }

        return RecordMapper.INSTANCE.recordToRecordResource(result);
    }

    public RecordResource getRecordByCode(String code, String password) {
        Record record = recordsRepository.getRecordByCode(code);

        if (record == null) {
            return null;
        }

        RecordResource result = RecordMapper.INSTANCE.recordToRecordResource(record);

        //TODO что-то делать с исклбчением при ошибке расшифровки
        if (password != null && !password.isEmpty()) {
            try {
                result = this.recordEncryptService.decrypt(result, password);
            } catch (RecordNotEncryptedException e) {
                return null;
            }
        }

        return result;
    }

    private String generateCode() {
        Faker faker = new Faker();

        return faker.funnyName().name();
    }
}
