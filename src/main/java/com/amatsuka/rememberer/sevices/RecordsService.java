package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.mappers.RecordMapper;
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
    public RecordDto storeRecord(StoreRecordRequest recordRequest) {

        RecordDto recordDto = RecordMapper.INSTANCE.storeRecordRequestToRecordResource(recordRequest);

        if (recordRequest.getPassword() != null && !recordRequest.getPassword().isEmpty()) {
            recordDto = this.recordEncryptService.encrypt(recordDto, recordRequest.getPassword());
        }

        return this.storeRecord(recordDto);
    }

    public RecordDto storeRecord(RecordDto recordDto) {

        recordDto.setCode(this.generateCode());
        Record record = RecordMapper.INSTANCE.recordResourceToRecord(recordDto);

        Record result;

        try {
            result = recordsRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new RecordNotStoredException(e);
        }

        return RecordMapper.INSTANCE.recordToRecordResource(result);
    }

    public RecordDto getRecordByCode(String code, String password) {
        Record record = recordsRepository.getRecordByCode(code);

        if (record == null) {
            return null;
        }

        RecordDto result = RecordMapper.INSTANCE.recordToRecordResource(record);

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
