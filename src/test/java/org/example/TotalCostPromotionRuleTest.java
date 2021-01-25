package org.example;

import org.example.model.BasketDetails;
import org.example.model.TotalCostPromotions;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotalCostPromotionRuleTest {

    @Mock
    private PromotionRepository promotionRepository;

    private TotalCostPromotionRule totalCostPromotionRule;

    @BeforeEach
    void setUp() {
        totalCostPromotionRule = new TotalCostPromotionRule(promotionRepository);
    }

    @Test
    @DisplayName("Apply total cost promotion when basket cost is above specified value")
    void applyTotalCostOfferSuccessfully() {
        BasketDetails givenBasket = BasketDetails.builder().basketTotal(50.00).build();

        when(promotionRepository.getTotalPricePromotions()).thenReturn(List.of(getTotalCostPromotionRule(35, 50.00)));

        totalCostPromotionRule.applyPromotions(givenBasket);

        assertEquals(25.00, givenBasket.getBasketTotal());
        verify(promotionRepository, times(1)).getTotalPricePromotions();

    }

    @Test
    @DisplayName("Apply total cost promotion when basket cost is equal to specified value")
    void applyTotalCostOffer() {
        BasketDetails givenBasket = BasketDetails.builder().basketTotal(50.00).build();

        when(promotionRepository.getTotalPricePromotions()).thenReturn(List.of(getTotalCostPromotionRule(50, 50.00)));

        totalCostPromotionRule.applyPromotions(givenBasket);

        assertEquals(25.00, givenBasket.getBasketTotal());
        verify(promotionRepository, times(1)).getTotalPricePromotions();

    }

    @Test
    @DisplayName("Should not change basketTotal if the value of minimum cart amount is less")
    void doNotApplyTotalCostDiscountMinimumAmountNotMet() {
        BasketDetails givenBasket = BasketDetails.builder().basketTotal(20.00).build();

        when(promotionRepository.getTotalPricePromotions()).thenReturn(List.of(getTotalCostPromotionRule(35, 50.00)));

        totalCostPromotionRule.applyPromotions(givenBasket);

        assertEquals(20.00, givenBasket.getBasketTotal());
        verify(promotionRepository, times(1)).getTotalPricePromotions();

    }

    @Test
    @DisplayName("Should not apply promotion or change basket total if offer is missing")
    void doNotApplyTotalCostDiscountIsNotAvailable() {
        BasketDetails givenBasket = BasketDetails.builder().basketTotal(5.1).build();

        when(promotionRepository.getTotalPricePromotions()).thenReturn(Collections.emptyList());

        totalCostPromotionRule.applyPromotions(givenBasket);

        assertEquals(5.1, givenBasket.getBasketTotal());
        verify(promotionRepository, times(1)).getTotalPricePromotions();

    }

    private TotalCostPromotions getTotalCostPromotionRule(Integer minimumAmount, Double offerPercent) {
        return TotalCostPromotions
                .builder()
                .minimumAmount(minimumAmount)
                .offerPercent(offerPercent)
                .build();
    }

}