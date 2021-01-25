package org.example;

import org.example.model.BasketDetails;
import org.example.model.PromotionalRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketCostCalculatorTest {

    @Mock
    ItemService itemService;

    @Mock
    PromotionalRules promotionalRules;

    @Mock
    ReducedPricePromotionRule reducedPricePromotionRule;

    @Mock
    TotalCostPromotionRule totalCostPromotionRule;

    @InjectMocks
    BasketCostCalculator basketCostCalculator;

    @Captor
    ArgumentCaptor<BasketDetails> basketItemCaptor;

    @Test
    @DisplayName("Basket cost should be calculated using item price if no promotions are available")
    void verifyBasketItemPriceIsComingFromCatalogue() {
        String itemCode = "123";
        Map<String, Integer> itemsInCart = Map.of(itemCode, 2);
        when(itemService.getItemPrice(itemCode)).thenReturn(100.19);

        Double basketCost = basketCostCalculator.calculateBasketCost(itemsInCart, promotionalRules);

        verify(itemService, times(1)).getItemPrice(itemCode);
        assertEquals(200.38, basketCost);

    }

    @Test
    @DisplayName("Basket cost should be calculated using item price if no promotions list is empty")
    void ShouldReturnBasketTotalWithoutPromotion() {
        String itemCode = "testItem";
        Map<String, Integer> itemsInCart = Map.of(itemCode, 5);
        when(itemService.getItemPrice(itemCode)).thenReturn(2.0);

        Double basketCost = basketCostCalculator.calculateBasketCost(itemsInCart, PromotionalRules.builder().promotionRules(Collections.emptyList()).build());

        verify(itemService, times(1)).getItemPrice(itemCode);
        assertEquals(10.00, basketCost);

    }

    @Test
    @DisplayName("Basket cost should be calculated using promotions if available")
    void verifyBasketItemPriceIsComingPromotion() {
        PromotionalRules promotionalRulesForTest = PromotionalRules
                .builder()
                .promotionRules(List.of(reducedPricePromotionRule))
                .build();

        String itemCode = "999";
        Map<String, Integer> itemsInCart = Map.of(itemCode, 5);
        when(itemService.getItemPrice(itemCode)).thenReturn(11.09);

        basketCostCalculator.calculateBasketCost(itemsInCart, promotionalRulesForTest);

        verify(itemService, times(1)).getItemPrice(itemCode);
        verify(reducedPricePromotionRule, times(1)).applyPromotions(basketItemCaptor.capture());
    }


    @Test
    @DisplayName("Basket cost should be calculated using all promotion types applicable")
    void verifyEachPromotionRuleIsCalled() {
        PromotionalRules promotionalRulesForTest = PromotionalRules
                .builder()
                .promotionRules(List.of(reducedPricePromotionRule, totalCostPromotionRule))
                .build();

        Map<String, Integer> itemsInCart = Map.of("itemCode", 2);
        basketCostCalculator.calculateBasketCost(itemsInCart, promotionalRulesForTest);

        verify(itemService, times(1)).getItemPrice("itemCode");
        verify(reducedPricePromotionRule, times(1)).applyPromotions(any());
        verify(totalCostPromotionRule, times(1)).applyPromotions(any());

    }

}