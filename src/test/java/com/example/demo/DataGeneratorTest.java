package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class DataGeneratorTest {

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private StockDataDao stockDataDao;

    @Test
    public void testGenerateStockData() {
        dataGenerator.generateStockData();
        assertTrue(stockDataDao.count() >= 1000000);
    }
}