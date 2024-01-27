package com.example.reading.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaRecordForDbInsertionDao {
    private int empId;
    private int dbFlag;
    private String dateOfDbInsertion;
}
