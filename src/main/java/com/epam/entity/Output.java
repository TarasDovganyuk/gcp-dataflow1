package com.epam.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Output implements Serializable {
    private Subject[] subjects;

}
