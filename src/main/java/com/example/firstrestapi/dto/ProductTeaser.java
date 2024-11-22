package com.example.firstrestapi.dto;

import com.example.firstrestapi.Database.mysql.models.BaseProductModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductTeaser extends BaseProductModel {
    private String displayName;
    private String displayPrice;
    private List<ProductDetail> details;
    private String languageModel;
    protected Float ratingAverage;
    public ProductTeaser(int id, String teaserImage, int categoryId, float price) {
        super(id, teaserImage, categoryId, price);
        displayName = "";
        displayPrice = "";
        details = new ArrayList<>();
    }
    public ProductTeaser(BaseProductModel baseProduct) {
        this(baseProduct.getId(), baseProduct.getImageUrl(), baseProduct.getCategoryId(), baseProduct.getPrice());
    }
    public ProductTeaser(int id, String teaserImage, int categoryId, float price, String displayName, String displayPrice, List<ProductDetail> details) {
        super(id, teaserImage, categoryId, price);
        this.displayName = displayName;
        this.displayPrice = displayPrice;
        this.details = details;
    }


    public void setRatingAverage(List<Rating> ratings) {
        if(ratings == null) { return; }
        float average = ratings.stream()
                .map(Rating::getRating)
                .reduce(0f, Float::sum);
        this.ratingAverage = average / ratings.size();
    }

    public static ProductTeaser copyFrom(ProductTeaser productTeaser){
        ProductTeaser dto = new ProductTeaser(productTeaser.getId(), productTeaser.getImageUrl(), productTeaser.getCategoryId(), productTeaser.getPrice());
        dto.setDetails(productTeaser.getDetails());
        dto.setDisplayName(productTeaser.getDisplayName());
        dto.setDisplayPrice(productTeaser.getDisplayPrice());
        return dto;
    }
}
