package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.entities.Record;
import com.amatsuka.rememberer.repositories.RecordsRepository;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotEnctyptedException;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import com.github.javafaker.Faker;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

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

        Record record = new Record(recordRequest.getText(), this.generateCode());
        record.setPasswordHash(recordRequest.getPassword());
        Record result;

        try {
            result = recordsRepository.save(record);
        } catch (DataIntegrityViolationException e) {
            throw new RecordNotStoredException(e);
        }

        if (result == null) {
            throw new RecordNotStoredException();
        }

        return new RecordResource(record.getText(), record.getCode(), record.getPasswordHash());
    }

    public RecordResource getRecordByCode(String code, String password) {
        Record record = recordsRepository.getRecordByCode(code);

        if (record == null) {
            return null;
        }

        RecordResource result = new RecordResource(record.getText(), record.getCode(), record.getPasswordHash());

        //TODO что-то делать с исклбчением при ошибке расшифровки
        if (password != null && !password.isEmpty()) {
            try {
                result = this.decryptRecord(result, password);
            } catch (RecordNotEnctyptedException e) {
                return null;
            }
        }

        return result;
    }

    private String generateCode() {
        Faker faker = new Faker();

        return faker.funnyName().name();
    }

    //TODO Разделить dto по слоям. В сервисе все должно обрабатывать RecordResource
    private StoreRecordRequest encryptRecord(StoreRecordRequest record) {
        return new StoreRecordRequest(
                new String(Base64.encodeBase64(record.getText().getBytes())),
                new String(new DigestUtils(MD5).digest(record.getPassword()))
        );
    }

    private RecordResource decryptRecord(RecordResource recordResource, String password) throws RecordNotEnctyptedException {
        String passwordHash = new String(new DigestUtils(MD5).digest(password));

        if (!passwordHash.equals(recordResource.getPasswordHash())) {
            throw new RecordNotEnctyptedException();
        }

        return new RecordResource(
                new String(Base64.decodeBase64(recordResource.getText())),
                recordResource.getCode(),
                recordResource.getPasswordHash()
        );
    }


}
