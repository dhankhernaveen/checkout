package org.example;

import org.example.model.BasketDetails;
import org.example.model.TotalCostPromotions;
import org.example.repository.PromotionRepository;

import java.util.List;
import java.util.Optional;

public class TotalCostPromotionRule implements PromotionRule {

    private final PromotionRepository promotionRepository;

    public TotalCostPromotionRule() {
        promotionRepository = new PromotionRepository("totalCostPromotions.json");
    }

    public TotalCostPromotionRule(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void applyPromotions(BasketDetails basketDetails) {
        List<TotalCostPromotions> totalCostPromotions = promotionRepository.getTotalPricePromotions();

        Optional<TotalCostPromotions> discountPromotions = totalCostPromotions.stream()
                .filter(costDiscountPromotions -> basketDetails.getBasketTotal() >= costDiscountPromotions.getMinimumAmount())
                .findFirst();

        if (discountPromotions.isPresent()) {
            Double offerPercent = discountPromotions.get().getOfferPercent();
            basketDetails.setBasketTotal(basketDetails.getBasketTotal() - (basketDetails.getBasketTotal() * offerPercent / 100));
        }

    }
}
