package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.Record;
import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.web.requests.StoreRecordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    RecordMapper INSTANCE = Mappers.getMapper( RecordMapper.class );

    RecordResource recordToRecordResource(Record record);

    Record recordResourceToRecord(RecordResource recordResource);

    RecordResource storeRecordRequestToRecordResource(StoreRecordRequest recordRequest);
}