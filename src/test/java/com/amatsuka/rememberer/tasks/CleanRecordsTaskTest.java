package com.amatsuka.rememberer.tasks;

import com.amatsuka.rememberer.domain.entities.Record;
import com.amatsuka.rememberer.domain.repositories.RecordsRepository;
import com.amatsuka.rememberer.util.BaseTest;
import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
//TODO какойто костыль, заменить на норм решение
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/clean_records_test_data.sql")
public class CleanRecordsTaskTest extends BaseTest {
    @Autowired
    private RecordsRepository recordsRepository;

    @Autowired
    private CleanRecordsTask cleanRecordsTask;

    private Faker faker;

    public CleanRecordsTaskTest() {
        this.faker = new Faker();
    }

    @Test
    public void cleanOldRecordsTest() {
        Record record = new Record(faker.lorem().paragraph(), faker.name().fullName());

        Record oldRecord = new Record(faker.lorem().paragraph(), faker.name().fullName());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        record = this.recordsRepository.save(record);
        oldRecord = this.recordsRepository.save(oldRecord);

        oldRecord.setCreatedAt(Date.from(c.toInstant()));
        oldRecord = this.recordsRepository.save(oldRecord);

        cleanRecordsTask.run();

        List<Record> recordList = Lists.newArrayList(this.recordsRepository.findAll());

        assertThat(recordList.size(), is(1));

        assertThat(recordList.get(0).getId(), is(record.getId()));
    }
}
