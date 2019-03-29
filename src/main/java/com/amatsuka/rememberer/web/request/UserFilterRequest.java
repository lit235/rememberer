package com.amatsuka.rememberer.web.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class UserFilterRequest {

    private String username;

    private String name;

    private Date createdAtFrom;

    private Date createdAtTo;

    private Integer pageSize;

    private Integer page;

    private String sortBy;

    private String direction;

}
