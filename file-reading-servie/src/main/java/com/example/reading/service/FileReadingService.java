package com.example.reading.service;

import com.example.reading.dao.KafkaRecordForApiInsertionDao;
import com.example.reading.dao.KafkaRecordForDbInsertionDao;
import com.example.reading.dao.TestDataDao;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReadingService {

    private static final String FILE_PATH = "D:\\D-Drive Backup\\My Documents\\Important Data\\Super App\\fileHandling\\";

    public List<TestDataDao> getMissingRecord(String date) {
        List<TestDataDao> testDataDaoList = new ArrayList<>();

        String api_file = FILE_PATH + "API_INSERTION_"+date+".csv";
        String db_file = FILE_PATH + "DB_INSERTION_"+date+".csv";

        List<KafkaRecordForApiInsertionDao> kafkaRecordForApiInsertionDaoList = readCsvForApiInsertion(api_file);
        List<KafkaRecordForDbInsertionDao> kafkaRecordForDbInsertionDaoList = readCsvForDbInsertion(db_file);
        
        for(KafkaRecordForApiInsertionDao kafkaRecordForApiInsertionDao : kafkaRecordForApiInsertionDaoList){
            int dataFlag = 0;
            for(KafkaRecordForDbInsertionDao kafkaRecordForDbInsertionDao : kafkaRecordForDbInsertionDaoList){
                if(kafkaRecordForApiInsertionDao.getEmpId() == kafkaRecordForDbInsertionDao.getEmpId()){
                    dataFlag = 1;
                    break;
                }
            }
            if(dataFlag == 0){
                TestDataDao testDataDao = new TestDataDao(kafkaRecordForApiInsertionDao);
                testDataDaoList.add(testDataDao);
            }
        }
        

        return testDataDaoList;
    }


    private List<KafkaRecordForApiInsertionDao> readCsvForApiInsertion(String apiFile) {
        List<KafkaRecordForApiInsertionDao> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(apiFile))) {
            // Read the header (if present)
            String header = reader.readLine();

            // Read and process each line in the CSV file
            String line;
            while ((line = reader.readLine()) != null) {
                KafkaRecordForApiInsertionDao record = processCsvLineForApiFile(line);
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    private List<KafkaRecordForDbInsertionDao> readCsvForDbInsertion(String dbFile) {
        List<KafkaRecordForDbInsertionDao> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dbFile))) {
            // Read the header (if present)
            String header = reader.readLine();

            // Read and process each line in the CSV file
            String line;
            while ((line = reader.readLine()) != null) {
                KafkaRecordForDbInsertionDao record = processCsvLineForDbFile(line);
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    private KafkaRecordForApiInsertionDao processCsvLineForApiFile(String line) {
        String[] fields = line.split(",");

        // Assuming the order of fields matches your CSV structure
        KafkaRecordForApiInsertionDao kafkaRecordForApiInsertionDao = new KafkaRecordForApiInsertionDao();
        kafkaRecordForApiInsertionDao.setEmpId(Integer.parseInt(fields[0]));
        kafkaRecordForApiInsertionDao.setEmployeeName(fields[1]);
        kafkaRecordForApiInsertionDao.setDesignation(fields[2]);
        kafkaRecordForApiInsertionDao.setGroup(fields[3]);
        kafkaRecordForApiInsertionDao.setManagerName(fields[4]);
        kafkaRecordForApiInsertionDao.setApiFlag(Integer.parseInt(fields[5]));
        kafkaRecordForApiInsertionDao.setDateOfApiInsertion(fields[6]);

        return kafkaRecordForApiInsertionDao;
    }

    private KafkaRecordForDbInsertionDao processCsvLineForDbFile(String line) {
        String[] fields = line.split(",");

        KafkaRecordForDbInsertionDao kafkaRecordForDbInsertionDao = new KafkaRecordForDbInsertionDao();
        kafkaRecordForDbInsertionDao.setEmpId(Integer.parseInt(fields[0]));
        kafkaRecordForDbInsertionDao.setDbFlag(Integer.parseInt(fields[1]));
        kafkaRecordForDbInsertionDao.setDateOfDbInsertion(fields[2]);

        return kafkaRecordForDbInsertionDao;
    }
}
