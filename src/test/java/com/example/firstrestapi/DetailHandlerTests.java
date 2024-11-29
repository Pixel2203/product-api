package com.example.firstrestapi;

import com.example.firstrestapi.dto.ProductDetail;
import com.example.firstrestapi.dto.ProductTeaser;
import com.example.firstrestapi.handler.DetailHandler;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DetailHandlerTests {
    @Test
    void testInjectDetailsInCorrectOrder(){
        DetailHandler detailHandler = new DetailHandler();
        List<ProductDetail> details = new ArrayList<>();
        details.add(new ProductDetail("tet", "nice"));
        details.add(new ProductDetail("testdw", "12"));
        details.add(new ProductDetail("tes", "Wow"));
        details.add(new ProductDetail("testueiwoueow", "12"));
        details.add(new ProductDetail("test", "This is a good thing"));

        List<ProductDetail> expected = new ArrayList<>();
        expected.add(new ProductDetail("tes", "Wow"));
        expected.add(new ProductDetail("tet", "nice"));
        expected.add(new ProductDetail("testdw", "12"));
        expected.add(new ProductDetail("testueiwoueow", "12"));
        expected.add(new ProductDetail("test", "This is a good thing"));

        ProductTeaser teaser = new ProductTeaser(3,"",12,12);

        detailHandler.injectTranslatedDetailsIntoProduct(teaser, details);

        assert teaser.getDetails().equals(expected);
    }
}
