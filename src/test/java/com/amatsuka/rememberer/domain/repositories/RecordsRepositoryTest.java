package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.Record;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class RecordsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecordsRepository recordsRepository;

    private Faker faker;

    public RecordsRepositoryTest() {
        this.faker = new Faker();
    }

    @Test
    public void should_store_record() {
        String text = faker.lorem().paragraph();
        String code = faker.name().fullName();

        Record record = new Record(text, code);

        Record result = this.recordsRepository.save(record);

        assertThat(result.getText(), is(text));
        assertThat(result.getCode(), is(code));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void should_throw_errors_with_store_non_unique_record() {
        String text = faker.lorem().paragraph();
        String code = faker.name().fullName();

        Record record = new Record(faker.lorem().paragraph(), code);
        Record secondRecord = new Record(faker.lorem().paragraph(), code);

        this.recordsRepository.save(record);
        this.recordsRepository.save(secondRecord);

        List<Record> res = this.recordsRepository.findAll();

    }

    @Test
    public void should_return_record_by_code() {
        String text = faker.lorem().paragraph();
        String code = faker.name().fullName();

        Record record = new Record(text, code);

        this.recordsRepository.save(record);

        List<Record> res = this.recordsRepository.findAll();

        Record recordByCode = this.recordsRepository.getRecordByCode(code);

        assertThat(recordByCode.getText(), is(text));
        assertThat(recordByCode.getCode(), is(code));

    }

    @Test
    public void should_return_null_if_record_by_code_not_found() {
        String text = faker.lorem().paragraph();
        String code = faker.name().fullName();
        String worngCode = faker.name().fullName();

        Record record = new Record(text, code);

        this.recordsRepository.save(record);

        List<Record> res = this.recordsRepository.findAll();

        Record recordByCode = this.recordsRepository.getRecordByCode(worngCode);

        assertThat(recordByCode, nullValue());
    }
}
