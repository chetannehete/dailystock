package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@EnableKafka
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Value("${NAME:World}")
    String name;

    @Autowired
    private StockDataDao stockDataDao;

    @Autowired
    private StockDataProducer stockDataProducer;

    @Autowired
    private DataGenerator dataGenerator;    

    @RestController
    class HelloworldController {

        @GetMapping("/")
        String hello() {
            return "Hello " + name + "!";
        }

        String analyzeDailyStockTrends(String symbol, LocalDate startDate, LocalDate endDate) {
            List<Double> dailyClosingPrices = fetchDailyStockData(symbol, startDate, endDate);

            if (dailyClosingPrices == null || dailyClosingPrices.size() < 2) {
                System.out.println("Insufficient data for trend analysis.");
                return "Insufficient data for trend analysis.";
            }

            double firstPrice = dailyClosingPrices.get(0);
            double lastPrice = dailyClosingPrices.get(dailyClosingPrices.size() - 1);

            if (lastPrice > firstPrice) {
                System.out.println("Trend for " + symbol + " between " + startDate + " and " + endDate + ": Uptrend");
                return "Uptrend";
            } else if (lastPrice < firstPrice) {
                System.out.println("Trend for " + symbol + " between " + startDate + " and " + endDate + ": Downtrend");
                return "Downtrend";
            } else {
                System.out.println("Trend for " + symbol + " between " + startDate + " and " + endDate + ": Sideways");
                return "Sideways";
            }
        }

        // Mock method to simulate fetching data from an external API
        private List<Double> fetchDailyStockData(String symbol, LocalDate startDate, LocalDate endDate) {
            // Replace this with actual API call in real application
            List<Double> mockData = new ArrayList<>();
            mockData.add(150.0);
            mockData.add(152.5);
            mockData.add(151.8);
            mockData.add(153.2);
            mockData.add(154.0);
            return mockData;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Generating StockData");
        dataGenerator.generateStockData();
        System.out.println("Sending data to Kafka");
        List<StockData> allStockData;
        try {
            allStockData = stockDataDao.findAll();
            allStockData.forEach(stockData -> {
                try {
                    stockDataProducer.sendStockData(stockData);
                } catch (Exception e) {
                    System.err.println("Error sending data to Kafka: " + e.getMessage());
                }
            });
        } catch (DataAccessException e) {
            System.err.println("Error reading data from the database: " + e.getMessage());
        }
    }
}
