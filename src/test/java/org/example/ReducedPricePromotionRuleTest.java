package org.example;

import org.example.model.BasketDetails;
import org.example.model.BasketItem;
import org.example.model.ReducedPricePromotions;
import org.example.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReducedPricePromotionRuleTest {

    @Mock
    PromotionRepository promotionRepository;

    ReducedPricePromotionRule reducedPricePromotionRule;

    @BeforeEach
    void setUp() {
        reducedPricePromotionRule = new ReducedPricePromotionRule(promotionRepository);
    }

    @Test
    @DisplayName("apply promotion price to basket items")
    void shouldApplyPromotionPriceToBasket() {

        BasketDetails givenBasket = getGivenBasket("123", 20.00, 2);

        when(promotionRepository.getReducedPricePromotions()).thenReturn(List.of
                (getReducedPricePromotion("123", 15.00, 2)));

        reducedPricePromotionRule.applyPromotions(givenBasket);

        assertEquals(30.00, givenBasket.getBasketTotal());
        assertEquals(15.00, givenBasket.getBasketItems().get(0).getFinalPrice());
        assertEquals(15.00, givenBasket.getBasketItems().get(0).getReducedPrice());
        assertEquals(20.00, givenBasket.getBasketItems().get(0).getOriginalItemPrice());
        assertEquals(2, givenBasket.getBasketItems().get(0).getItemQuantity());
        assertEquals("123", givenBasket.getBasketItems().get(0).getItemCode());

    }

    @Test
    @DisplayName("apply promotion price to basket items only when both item code and minimum quantity is matching")
    void shouldApplyPromotionWithMatchingItemCodeAndQuantity() {

        BasketDetails givenBasket = getGivenBasket("123", 20.00, 1);

        when(promotionRepository.getReducedPricePromotions()).thenReturn(List.of
                (getReducedPricePromotion("123", 15.00, 2)));

        reducedPricePromotionRule.applyPromotions(givenBasket);

        assertEquals(20.00, givenBasket.getBasketTotal());
        assertEquals(20.00, givenBasket.getBasketItems().get(0).getFinalPrice());
        assertNull(givenBasket.getBasketItems().get(0).getReducedPrice());
        assertEquals(20.00, givenBasket.getBasketItems().get(0).getOriginalItemPrice());
        assertEquals(1, givenBasket.getBasketItems().get(0).getItemQuantity());
        assertEquals("123", givenBasket.getBasketItems().get(0).getItemCode());

    }


    @Test
    @DisplayName("should not change basket detail if reduce offer promotion is not available ")
    void shouldNotChangeBasketDetail() {

        BasketDetails givenBasket = getGivenBasket("nonDiscountProduct", 7.5, 1);

        when(promotionRepository.getReducedPricePromotions()).thenReturn(Collections.emptyList());

        reducedPricePromotionRule.applyPromotions(givenBasket);

        assertEquals(7.5, givenBasket.getBasketTotal());
        assertEquals(7.5, givenBasket.getBasketItems().get(0).getFinalPrice());
        assertNull(givenBasket.getBasketItems().get(0).getReducedPrice());
        assertEquals(7.5, givenBasket.getBasketItems().get(0).getOriginalItemPrice());
        assertEquals(1, givenBasket.getBasketItems().get(0).getItemQuantity());
        assertEquals("nonDiscountProduct", givenBasket.getBasketItems().get(0).getItemCode());

    }


    private BasketDetails getGivenBasket(String itemCode, Double price, Integer quantity) {
        return BasketDetails.builder().basketItems(List.of(BasketItem.builder()
                .itemCode(itemCode)
                .originalItemPrice(price)
                .finalPrice(price)
                .itemQuantity(quantity)
                .build())).basketTotal(price * quantity)
                .build();
    }

    private ReducedPricePromotions getReducedPricePromotion(String itemCode, Double offerPrice, Integer quantity) {
        return ReducedPricePromotions
                .builder()
                .itemCode(itemCode)
                .minimumQuantity(quantity)
                .offerPrice(offerPrice)
                .build();
    }

}