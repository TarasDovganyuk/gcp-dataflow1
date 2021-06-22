package com.epam.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class EventSubject implements Serializable {
    private String id;
    private String type;
}
