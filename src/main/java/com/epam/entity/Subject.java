package com.epam.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Subject implements Serializable {
    private String id;
    private String type;
    private Activity[] activities;
}
