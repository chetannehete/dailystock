package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class StockDataProducerTest {

    @Autowired
    private StockDataProducer stockDataProducer;

    @MockBean
    private KafkaTemplate<String, StockData> kafkaTemplate;

    @Test
    void testSendMessage() {
        StockData stockData = new StockData("AAPL", LocalDate.now(), 150.0, 155.0, 148.0, 152.0, 1000000L);
        CompletableFuture<SendResult<String, StockData>> future = new CompletableFuture<>();
        future.complete(Mockito.mock(SendResult.class));

        when(kafkaTemplate.send(anyString(), any(StockData.class))).thenReturn(future);

        stockDataProducer.sendStockData(stockData);

        verify(kafkaTemplate, times(1)).send(anyString(), any(StockData.class));
    }

    @Test
    void testSendMessage_Exception() {
        StockData stockData = new StockData("AAPL", LocalDate.now(), 150.0, 155.0, 148.0, 152.0, 1000000L);
        CompletableFuture<SendResult<String, StockData>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Test Exception"));

        when(kafkaTemplate.send(anyString(), any(StockData.class))).thenReturn(future);

        stockDataProducer.sendStockData(stockData);

        verify(kafkaTemplate, times(1)).send(anyString(), any(StockData.class));
    }
}
