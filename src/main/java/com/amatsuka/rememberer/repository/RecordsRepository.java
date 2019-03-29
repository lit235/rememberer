package com.amatsuka.rememberer.repository;

import com.amatsuka.rememberer.domain.entity.QRecord;
import com.amatsuka.rememberer.domain.entity.Record;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordsRepository extends QueryDslRepository<Record, QRecord,  Long> {
    Record getRecordByCode(String code);

    List<Record> findByCreatedAtBefore(Date date);
}
