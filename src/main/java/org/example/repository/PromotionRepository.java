package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.ReducedPricePromotions;
import org.example.model.TotalCostPromotions;
import org.example.util.FileReader;

import java.util.List;

public class PromotionRepository {

    private Gson gson;
    private String promotionType;

    public PromotionRepository(String promotionFileName) {
        this.promotionType = promotionFileName;
        gson = new Gson();
    }

    public List<ReducedPricePromotions> getReducedPricePromotions() {
        String quantityPromotionJson = FileReader.readFile(promotionType);
        return gson.fromJson(quantityPromotionJson, new TypeToken<List<ReducedPricePromotions>>() {
        }.getType());
    }

    public List<TotalCostPromotions> getTotalPricePromotions() {
        String quantityPromotionJson = FileReader.readFile(promotionType);
        return gson.fromJson(quantityPromotionJson, new TypeToken<List<TotalCostPromotions>>() {
        }.getType());
    }

}
