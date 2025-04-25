package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockDataProducer {

    private static final String TOPIC = "stock-data-topic";
    @Autowired
    private KafkaTemplate<String, StockData> kafkaTemplate;
    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    public void sendStockData(StockData stockData) {
        kafkaTemplate.send(topic, stockData);
    }
}