package org.example;

import org.example.model.BasketDetails;
import org.example.model.BasketItem;
import org.example.model.ReducedPricePromotions;
import org.example.repository.PromotionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReducedPricePromotionRule implements PromotionRule {

    private final PromotionRepository promotionRepository;

    public ReducedPricePromotionRule() {
        promotionRepository = new PromotionRepository("quantityBasedPromotions.json");
    }

    public ReducedPricePromotionRule(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void applyPromotions(BasketDetails basketDetails) {
        List<BasketItem> updatedBasketItems = updateBasketItemPriceWithPromotions(basketDetails);
        basketDetails.setBasketTotal(BasketCostCalculator.getTotalCostForItemsInCart(updatedBasketItems));
    }

    private List<BasketItem> updateBasketItemPriceWithPromotions(BasketDetails basketDetails) {
        return basketDetails.getBasketItems()
                .stream()
                .peek(basketItem -> {
                    Double discountedPrice = getDiscountedPrice(basketItem.getItemCode(), basketItem.getItemQuantity());
                    if (Objects.nonNull(discountedPrice)) {
                        basketItem.setReducedPrice(discountedPrice);
                        basketItem.setFinalPrice(discountedPrice);
                    }
                }).collect(Collectors.toList());
    }

    private Double getDiscountedPrice(String itemCode, Integer quantity) {
        List<ReducedPricePromotions> reducedPricePromotions = promotionRepository.getReducedPricePromotions();

        Optional<ReducedPricePromotions> reducedPricePromotion = reducedPricePromotions
                .stream()
                .filter(offer -> itemCode.equals(offer.getItemCode()) &&
                        quantity >= offer.getMinimumQuantity())
                .findFirst();

        return reducedPricePromotion
                .map(ReducedPricePromotions::getOfferPrice)
                .orElse(null);

    }
}
