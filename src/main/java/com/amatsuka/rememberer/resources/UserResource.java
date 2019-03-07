package com.amatsuka.rememberer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResource {
    private Long id;

    private String name;

    private String username;
}
