package com.example.create.dao;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDao {
    private int status;
    private String message;
}
