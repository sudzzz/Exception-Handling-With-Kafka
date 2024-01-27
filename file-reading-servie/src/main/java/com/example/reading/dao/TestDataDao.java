package com.example.reading.dao;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestDataDao {

    private int empId;
    private String employeeName;
    private String designation;
    private String group;
    private String managerName;

    public TestDataDao(KafkaRecordForApiInsertionDao kafkaRecordForApiInsertionDao){
        this.empId = kafkaRecordForApiInsertionDao.getEmpId();
        this.employeeName = kafkaRecordForApiInsertionDao.getEmployeeName();
        this.designation = kafkaRecordForApiInsertionDao.getDesignation();
        this.group = kafkaRecordForApiInsertionDao.getGroup();
        this.managerName = kafkaRecordForApiInsertionDao.getManagerName();
    }

}
