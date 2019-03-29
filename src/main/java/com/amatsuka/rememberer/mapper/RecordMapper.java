package com.amatsuka.rememberer.mapper;

import com.amatsuka.rememberer.domain.entity.Record;
import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.web.request.StoreRecordRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    RecordDto recordToRecordResource(Record record);

    Record recordResourceToRecord(RecordDto recordDto);

    RecordDto storeRecordRequestToRecordResource(StoreRecordRequest recordRequest);
}