package com.epam.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Activity implements Serializable {
    private String city;
    private String subjectId;
    private String subjectType;
    private String type;
    private Long past7daysCount = 0L;
    private Long past7daysUniqueCount = 0L;
    private Long past30daysCount = 0L;
    private Long past30daysUniqueCount = 0L;
}
