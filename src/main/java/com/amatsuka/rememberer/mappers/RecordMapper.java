package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.Record;
import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    RecordMapper INSTANCE = Mappers.getMapper( RecordMapper.class );

    RecordDto recordToRecordResource(Record record);

    Record recordResourceToRecord(RecordDto recordDto);

    RecordDto storeRecordRequestToRecordResource(StoreRecordRequest recordRequest);
}