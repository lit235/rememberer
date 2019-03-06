package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.QRecord;
import com.amatsuka.rememberer.domain.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordsRepository extends QueryDslRepository<Record, QRecord,  Long> {
    Record getRecordByCode(String code);
}
