package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DataGenerator {

    @Autowired
    private JpaRepository<StockData,Long> stockDataRepository;

    public void generateStockData() {
        List<StockData> stockDataList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            StockData stockData = new StockData();
            stockData.setSymbol("STOCK" + (i % 100));
            stockData.setDate(LocalDate.now().minusDays(random.nextInt(365)));
            stockData.setOpen(100.0 + random.nextDouble() * 50.0);
            stockData.setHigh(stockData.getOpen() + random.nextDouble() * 10.0);
            stockData.setLow(stockData.getOpen() - random.nextDouble() * 10.0);
            stockData.setClose(stockData.getLow() + random.nextDouble() * (stockData.getHigh() - stockData.getLow()));
            stockData.setVolume(random.nextLong() % 100000);
            stockDataList.add(stockData);
        }
        stockDataRepository.saveAll(stockDataList);
    }
}