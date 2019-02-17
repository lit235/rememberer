package com.amatsuka.rememberer.repositories;

import com.amatsuka.rememberer.entities.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordsRepository extends CrudRepository<Record, Long> {
    Record getRecordByCode(String code);
}
