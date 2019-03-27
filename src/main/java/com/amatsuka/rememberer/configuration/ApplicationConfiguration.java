package com.amatsuka.rememberer.configuration;

import com.amatsuka.rememberer.tasks.CleanRecordsTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {
}
