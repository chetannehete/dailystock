package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StockDataDaoTest {

    @Autowired 
    private StockDataDao stockDataDao;

    @Test
    public void testSaveAndRetrieveStockData() {
        StockData stockData = new StockData(null, "AAPL", LocalDate.of(2023, 1, 1), 150.0, 155.0, 148.0, 152.0, 100000L);
        stockDataDao.save(stockData);

        List<StockData> foundData = stockDataDao.findAll();
        assertThat(foundData).isNotEmpty();
        assertThat(foundData.get(0).getSymbol()).isEqualTo("AAPL");
    }

    @Test
    public void testFindAllStockData() {
        StockData stockData1 = new StockData("GOOG", LocalDate.of(2023, 1, 2), 250.0, 255.0, 248.0, 252.0, 200000L);
        StockData stockData2 = new StockData("MSFT", LocalDate.of(2023, 1, 3), 300.0, 305.0, 298.0, 302.0, 300000L);
        stockDataDao.save(stockData1);
        stockDataDao.save(stockData2);

        List<StockData> allData = stockDataDao.findAll();
        assertThat(allData).hasSizeGreaterThanOrEqualTo(2);
    }
}