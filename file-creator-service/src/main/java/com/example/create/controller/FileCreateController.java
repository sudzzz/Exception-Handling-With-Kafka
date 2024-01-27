package com.example.create.controller;

import com.example.create.dao.ResponseDao;
import com.example.create.dao.TestDataDao;
import com.example.create.dao.KafkaRecordForApiInsertionDao;
import com.google.gson.Gson;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class FileCreateController {

    @Autowired
    NewTopic kafkaTestTopic;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplateForTestTopic;


    private static final Logger log = LoggerFactory.getLogger(FileCreateController.class);

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd-MM-yyyy 'at' h:m:s a z");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private static final String FILE_PATH = "D:\\D-Drive Backup\\My Documents\\Important Data\\Super App\\fileHandling\\";

    @PostMapping("/saveTestData")
    public ResponseDao saveTestData(@RequestBody TestDataDao testDataDao){

        try {
            ResponseDao responseDao = new ResponseDao();

            log.info("incoming message is {}",testDataDao);

            KafkaRecordForApiInsertionDao kafkaRecordForApiInsertionDao = new KafkaRecordForApiInsertionDao(testDataDao,1,dateFormatter.format(new Date()));

            String currentDate = dateFormat.format(new Date());
            String fileName = FILE_PATH+"API_INSERTION_"+currentDate+".csv";

            File file = new File(fileName);

            if(file.exists()){
                writeEntries(fileName,kafkaRecordForApiInsertionDao);
            } else {
                createFolderIfNotExists();
                writeEntries(fileName,kafkaRecordForApiInsertionDao);
            }

            Gson gson = new Gson();
            String kafkaData = gson.toJson(testDataDao);

            Message<String> message = MessageBuilder
                    .withPayload(kafkaData)
                    .setHeader(KafkaHeaders.TOPIC,kafkaTestTopic.name())
                    .build();
            log.info("message before insertion in topic is {}",message);
            kafkaTemplateForTestTopic.send(message);

            responseDao.setStatus(1);
            responseDao.setMessage("Data inserted successfully");
            return responseDao;
        } catch (Exception e){
            ResponseDao responseDao = new ResponseDao();
            responseDao.setStatus(0);
            responseDao.setMessage("Some error occurred");
            return responseDao;
        }

    }

    private void createFolderIfNotExists() {
        File folder = new File(FILE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void writeEntries(String fileName, KafkaRecordForApiInsertionDao kafkaRecordForApiInsertionDao) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName,true))){

            if( new File(fileName).length() == 0){
                bufferedWriter.write("empId,employeeName,designation,group,managerName,apiFlag,dateOfApiInsertion");
                bufferedWriter.newLine();
            }
            bufferedWriter.write( String.format("%d,%s,%s,%s,%s,%d,%s",
                    kafkaRecordForApiInsertionDao.getEmpId(),
                    kafkaRecordForApiInsertionDao.getEmployeeName(),
                    kafkaRecordForApiInsertionDao.getDesignation(),
                    kafkaRecordForApiInsertionDao.getGroup(),
                    kafkaRecordForApiInsertionDao.getManagerName(),
                    kafkaRecordForApiInsertionDao.getApiFlag(),
                    kafkaRecordForApiInsertionDao.getDateOfApiInsertion()));
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
