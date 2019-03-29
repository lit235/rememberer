package com.amatsuka.rememberer.domain.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Data
@RequiredArgsConstructor
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
}
