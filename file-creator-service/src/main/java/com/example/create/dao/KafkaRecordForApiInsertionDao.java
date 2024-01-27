package com.example.create.dao;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRecordForApiInsertionDao extends TestDataDao {
    private int apiFlag;
    private String dateOfApiInsertion;

    public KafkaRecordForApiInsertionDao(TestDataDao testDataDao, int apiFlag, String dateOfApiInsertion){
        super(testDataDao.getEmpId(),testDataDao.getEmployeeName(),testDataDao.getDesignation(),
                testDataDao.getGroup(),testDataDao.getManagerName());
        this.apiFlag = apiFlag;
        this.dateOfApiInsertion = dateOfApiInsertion;
    }

    @Override
    public String toString(){
        return "KafkaRecordForApiInsertionDao(apiFlag="+ this.apiFlag+ ", dateOfApiInsertion="+ this.dateOfApiInsertion+ ")" + super.toString();
    }
}
