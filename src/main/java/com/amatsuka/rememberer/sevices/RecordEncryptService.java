package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotEncryptedException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

@Service
public class RecordEncryptService {
    public RecordResource encrypt(RecordResource recordResource, String password) {

        return new RecordResource(
                recordResource.getId(),
                new String(Base64.encodeBase64(recordResource.getText().getBytes())),
                recordResource.getCode(),
                new String(new DigestUtils(MD5).digest(password))
        );
    }

    public RecordResource decrypt(RecordResource recordResource, String password) throws RecordNotEncryptedException {
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
