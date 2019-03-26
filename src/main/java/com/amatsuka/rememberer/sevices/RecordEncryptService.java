package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotEncryptedException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class RecordEncryptService {
    public RecordDto encrypt(RecordDto recordDto, String password) {

        return new RecordDto(
                recordDto.getId(),
                new String(Base64.encodeBase64(recordDto.getText().getBytes())),
                recordDto.getCode(),
                DigestUtils.md5Hex(password),
                recordDto.getCreatedAt()
        );
    }

    public RecordDto decrypt(RecordDto recordDto, String password) throws RecordNotEncryptedException {
        String passwordHash = DigestUtils.md5Hex(password);

        if (!passwordHash.equals(recordDto.getPasswordHash())) {
            throw new RecordNotEncryptedException();
        }

        return new RecordDto(
                recordDto.getId(),
                new String(Base64.decodeBase64(recordDto.getText())),
                recordDto.getCode(),
                recordDto.getPasswordHash(),
                recordDto.getCreatedAt()
        );
    }
}
