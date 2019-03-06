package com.amatsuka.rememberer.web.requests;

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

}
