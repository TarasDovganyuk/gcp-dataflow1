package com.epam.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class Event implements Serializable {
    private Long id;
    private String userId;
    private String city;
    private String eventType;
    private Timestamp timestamp;
    private EventSubject eventSubject;
}
