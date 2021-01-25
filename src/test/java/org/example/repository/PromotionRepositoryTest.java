package org.example.repository;

import org.example.model.ReducedPricePromotions;
import org.example.model.TotalCostPromotions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromotionRepositoryTest {

    private PromotionRepository promotionRepository;

    @Test
    void shouldReadFileAndMapToReducePricePromotion() {
        promotionRepository = new PromotionRepository("reducePricePromotions.json");
        ReducedPricePromotions expectedPromotions = getReducedPricePromotion("001", 8.50, 2);

        List<ReducedPricePromotions> actualPromotions = promotionRepository.getReducedPricePromotions();

        assertEquals(1, actualPromotions.size());
        assertEquals(expectedPromotions.getItemCode(), actualPromotions.get(0).getItemCode());
        assertEquals(expectedPromotions.getOfferPrice(), actualPromotions.get(0).getOfferPrice());
        assertEquals(expectedPromotions.getMinimumQuantity(), actualPromotions.get(0).getMinimumQuantity());
    }

    @Test
    void shouldReadFileAndMapToTotalCostPromotion() {
        promotionRepository = new PromotionRepository("finalPricePromotions.json");
        TotalCostPromotions expectedPromotions = getTotalCostPromotionRule(150, 25.00);
        List<TotalCostPromotions> actualPromotions = promotionRepository.getTotalPricePromotions();


        assertEquals(1, actualPromotions.size());
        assertEquals(expectedPromotions.getMinimumAmount(), actualPromotions.get(0).getMinimumAmount());
        assertEquals(expectedPromotions.getOfferPercent(), actualPromotions.get(0).getOfferPercent());
    }

    private ReducedPricePromotions getReducedPricePromotion(String itemCode, Double offerPrice, Integer quantity) {
        return ReducedPricePromotions
                .builder()
                .itemCode(itemCode)
                .minimumQuantity(quantity)
                .offerPrice(offerPrice)
                .build();
    }

    private TotalCostPromotions getTotalCostPromotionRule(Integer minimumAmount, Double offerPercent) {
        return TotalCostPromotions
                .builder()
                .minimumAmount(minimumAmount)
                .offerPercent(offerPercent)
                .build();
    }

}