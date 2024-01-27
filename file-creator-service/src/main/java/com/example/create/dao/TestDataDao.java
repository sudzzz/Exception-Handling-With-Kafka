package com.example.create.dao;

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

}
