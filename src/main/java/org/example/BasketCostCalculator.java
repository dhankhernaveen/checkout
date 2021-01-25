package org.example;

import org.example.model.BasketDetails;
import org.example.model.BasketItem;
import org.example.model.PromotionalRules;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BasketCostCalculator {

    private final ItemService itemService;

    BasketCostCalculator(ItemService itemService) {
        this.itemService = itemService;
    }

    public Double calculateBasketCost(Map<String, Integer> itemsInCart, PromotionalRules promotionalRules) {
        BasketDetails basketDetails = mapPriceToItemsInCart(itemsInCart);

        if (Objects.isNull(promotionalRules) || Objects.isNull(promotionalRules.getPromotionRules()) || promotionalRules.getPromotionRules().isEmpty())
            return getTotalCostForItemsInCart(basketDetails.getBasketItems());

        promotionalRules
                .getPromotionRules()
                .forEach(promotionRule -> {
                    promotionRule.applyPromotions(basketDetails);
                });

        return basketDetails.getBasketTotal();
    }

    private BasketDetails mapPriceToItemsInCart(Map<String, Integer> itemsInCart) {

        List<BasketItem> allBasketItems = itemsInCart.entrySet()
                .stream()
                .map(cartItem -> {
                    Double itemPrice = itemService.getItemPrice(cartItem.getKey());
                    return BasketItem.builder()
                            .itemCode(cartItem.getKey())
                            .itemQuantity(cartItem.getValue())
                            .originalItemPrice(itemPrice)
                            .finalPrice(itemPrice)
                            .build();
                })
                .collect(Collectors.toList());

        return BasketDetails.builder().basketItems(allBasketItems).build();
    }

    static Double getTotalCostForItemsInCart(List<BasketItem> allBasketItems) {
        AtomicReference<Double> totalCost = new AtomicReference<>(0.0);

        allBasketItems.forEach(basketItem ->
                totalCost.updateAndGet(v -> v + basketItem.getFinalPrice() * basketItem.getItemQuantity()));

        return totalCost.get();
    }
}
