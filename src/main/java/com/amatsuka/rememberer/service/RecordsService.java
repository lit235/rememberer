package com.amatsuka.rememberer.service;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.mapper.RecordMapper;
import com.amatsuka.rememberer.domain.entity.Record;
import com.amatsuka.rememberer.repository.RecordsRepository;
import com.amatsuka.rememberer.service.exceptions.RecordNotEncryptedException;
import com.amatsuka.rememberer.service.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.request.StoreRecordRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RecordsService {

    private final RecordsRepository recordsRepository;
    
    private final RecordEncryptService recordEncryptService;
    
    private final RecordMapper recordMapper;

    @Autowired
    public RecordsService(RecordsRepository recordsRepository, RecordEncryptService recordEncryptService, RecordMapper recordMapper) {
        this.recordsRepository = recordsRepository;
        this.recordEncryptService = recordEncryptService;
        this.recordMapper = recordMapper;
    }

    //TODO организовать логику сохраниения зашифрованных и незашифрованных данных
    public RecordDto storeRecord(StoreRecordRequest recordRequest) {

        RecordDto recordDto = recordMapper.storeRecordRequestToRecordResource(recordRequest);

        if (recordRequest.getPassword() != null && !recordRequest.getPassword().isEmpty()) {
            recordDto = this.recordEncryptService.encrypt(recordDto, recordRequest.getPassword());
        }

        return this.storeRecord(recordDto);
    }

    public RecordDto storeRecord(RecordDto recordDto) {

        recordDto.setCode(this.generateCode());
        Record record = recordMapper.recordResourceToRecord(recordDto);

        Record result;

        try {
            result = recordsRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new RecordNotStoredException(e);
        }

        return recordMapper.recordToRecordResource(result);
    }

    public RecordDto getRecordByCode(String code, String password) {
        Record record = recordsRepository.getRecordByCode(code);

        if (record == null) {
            return null;
        }

        RecordDto result = recordMapper.recordToRecordResource(record);

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

    public List<RecordDto> getExpiredRecords() {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -5);

        List<Record> records  = this.recordsRepository.findByCreatedAtBefore(calendar.getTime());

        return records.stream()
                .map(recordMapper::recordToRecordResource)
                .collect(Collectors.toList());
    }

    public void deleteRecords(List<RecordDto> recordsDto) {

        List<Record> records = recordsDto.stream().map(recordMapper::recordResourceToRecord).collect(Collectors.toList());

        this.recordsRepository.deleteAll(records);
    }
}
