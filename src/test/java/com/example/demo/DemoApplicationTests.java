package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SpringBootTest
class DemoApplicationTests{

  @Autowired
  private DemoApplication demoApplication;

  @Autowired
  private DataGenerator dataGenerator;

  @Autowired
  private StockDataDao stockDataDao;
  
  
  @BeforeEach
  public void setup() {
    dataGenerator.generateStockData(100);
  }

  @AfterEach
  public void cleanUp(){
    stockDataDao.deleteAll();
  }

  @Test
  void contextLoads() {
    
  }
  @Test
  void testSendStockDataToKafka() {
    List<StockData> allStockData = stockDataDao.findAll();
    Assertions.assertFalse(allStockData.isEmpty());
    demoApplication.sendStockDataToKafka(new ArrayList<>(allStockData));
  }

  @Test
  void testGenerateStockData() {
    Assertions.assertEquals(stockDataDao.findAll().size(),100);
  }
}
