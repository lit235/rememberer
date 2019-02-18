package com.amatsuka.rememberer.repositories;

import com.amatsuka.rememberer.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordsRepository extends JpaRepository<Record, Long> {
    Record getRecordByCode(String code);
}
