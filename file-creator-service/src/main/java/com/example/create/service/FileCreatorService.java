package com.example.create.service;

import com.example.create.dao.KafkaRecordForDbInsertionDao;
import com.example.create.dao.TestDataDao;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileCreatorService {

    private static final Logger log = LoggerFactory.getLogger(FileCreatorService.class);

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd-MM-yyyy 'at' h:m:s a z");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private static final String FILE_PATH = "D:\\D-Drive Backup\\My Documents\\Important Data\\Super App\\fileHandling\\";

    @KafkaListener(topics = "test_topic",groupId = "test_topic_group")
    public void saveTestData(String testData){

        try {
            Gson gson = new Gson();
            TestDataDao testDataDao = gson.fromJson(testData,TestDataDao.class);

            KafkaRecordForDbInsertionDao kafkaRecordForDbInsertionDao = new KafkaRecordForDbInsertionDao();

            kafkaRecordForDbInsertionDao.setDbFlag(1);
            kafkaRecordForDbInsertionDao.setDateOfDbInsertion(dateFormatter.format(new Date()));
            kafkaRecordForDbInsertionDao.setEmpId(testDataDao.getEmpId());


            String currentDate = dateFormat.format(new Date());
            String fileName = FILE_PATH+"DB_INSERTION_"+currentDate+".csv";

            File file = new File(fileName);

            if(file.exists()){
                writeEntries(fileName,kafkaRecordForDbInsertionDao);
            } else {
                createFolderIfNotExists();
                writeEntries(fileName,kafkaRecordForDbInsertionDao);
            }
        } catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
    }

    private void createFolderIfNotExists() {
        File folder = new File(FILE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void writeEntries(String fileName, KafkaRecordForDbInsertionDao kafkaRecordForDbInsertionDao) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName,true))){

            if( new File(fileName).length() == 0){
                bufferedWriter.write("empId,dbFlag,dateOfDbInsertion");
                bufferedWriter.newLine();
            }
            bufferedWriter.write( String.format("%d,%d,%s",
                    kafkaRecordForDbInsertionDao.getEmpId(),
                    kafkaRecordForDbInsertionDao.getDbFlag(),
                    kafkaRecordForDbInsertionDao.getDateOfDbInsertion()));
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
