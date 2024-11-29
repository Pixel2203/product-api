package com.example.firstrestapi;

import com.example.firstrestapi.util.PriceHelper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

public class PriceHelperTests {
  @Test
  void correctDisplayPriceTestEuro() {
    PriceHelper priceHelperEuro = PriceHelper.Default();
    float[] testPrices = {300.99f,300f,300.1f};
    String[] expectation = {"300,99€", "300,00€", "300,10€"};

    for(int p = 0; p < testPrices.length; p++) {
      assert priceHelperEuro.buildPrice(testPrices[p]).equals(expectation[p]);
    }

  }
  @Test
  void correctDisplayPriceTestDollar() {
    PriceHelper priceHelperDollar = new PriceHelper("$", Strings.EMPTY, true);
    float[] testPrices = {300.99f,300f,300.1f};
    String[] expectation = {"$300.99", "$300.00", "$300.10"};

    for(int p = 0; p < testPrices.length; p++) {
      assert priceHelperDollar.buildPrice(testPrices[p]).equals(expectation[p]);
    }

  }


}
