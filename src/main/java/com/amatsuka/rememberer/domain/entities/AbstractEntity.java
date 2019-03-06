package com.amatsuka.rememberer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
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
