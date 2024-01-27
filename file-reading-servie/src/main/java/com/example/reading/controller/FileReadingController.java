package com.example.reading.controller;


import com.example.reading.dao.TestDataDao;
import com.example.reading.service.FileReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileReadingController {

    @Autowired
    FileReadingService fileReadingService;

    private static final Logger log = LoggerFactory.getLogger(FileReadingController.class);

    @GetMapping("/getMissingRecords")
    public List<TestDataDao> getMissingRecords(@RequestParam("date") String date){
        return fileReadingService.getMissingRecord(date);
    }
}
