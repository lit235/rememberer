package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.mappers.RecordMapper;
import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.domain.entities.Record;
import com.amatsuka.rememberer.domain.repositories.RecordsRepository;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotEncryptedException;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import com.github.javafaker.Faker;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

@Service
public class RecordsService {

    private RecordsRepository recordsRepository;

    @Autowired
    public RecordsService(RecordsRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }

    //TODO организовать логику сохраниения зашифрованных и незашифрованных данных
    public RecordResource storeRecord(StoreRecordRequest recordRequest) {

        if (recordRequest.getPassword() != null && !recordRequest.getPassword().isEmpty()) {
            recordRequest = this.encryptRecord(recordRequest);
        }

        return this.storeRecord(RecordMapper.INSTANCE.storeRecordRequestToRecordResource(recordRequest));
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
                result = this.decryptRecord(result, password);
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

    //TODO выделить в отдельный севис шифровку записей
    private StoreRecordRequest encryptRecord(StoreRecordRequest record) {
        return new StoreRecordRequest(
                new String(Base64.encodeBase64(record.getText().getBytes())),
                new String(new DigestUtils(MD5).digest(record.getPassword()))
        );
    }

    private RecordResource decryptRecord(RecordResource recordResource, String password) throws RecordNotEncryptedException {
        String passwordHash = new String(new DigestUtils(MD5).digest(password));

        if (!passwordHash.equals(recordResource.getPasswordHash())) {
            throw new RecordNotEncryptedException();
        }

        return new RecordResource(
                recordResource.getId(),
                new String(Base64.decodeBase64(recordResource.getText())),
                recordResource.getCode(),
                recordResource.getPasswordHash()
        );
    }


}
