package org.example;

import org.example.model.Item;
import org.example.model.PromotionalRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutTest {

    @Mock
    PromotionalRules promotionalRules;

    @Mock
    Basket basket;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    private Checkout checkout;

    @BeforeEach
    void setUp() {
        checkout = new Checkout(promotionalRules, basket);
    }

    @Test
    @DisplayName("should successfully add valid item to cart")
    void shouldScanAndAddValidItemToBasket() {
        Item givenItem = Item.builder().itemCode("123").itemPrice(20.5).build();
        checkout.scan(givenItem);

        verify(basket, times(1)).addItemToCart(itemCaptor.capture());
        assertEquals(givenItem, itemCaptor.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should fail to add when item id is missing")
    void shouldFailToScan(String itemCode) {
        Item givenItem = Item.builder().itemCode(itemCode).build();

        checkout.scan(givenItem);

        verifyNoMoreInteractions(basket);
    }

    @Test
    @DisplayName("should fail to add when item is null")
    void shouldFailToScanWhenItemIsNull() {
        Item givenItem = null;

        checkout.scan(givenItem);

        verifyNoMoreInteractions(basket);
    }

    @Test
    @DisplayName("Should return the total pricr to be payed by customer")
    void shouldReturnTotalPrice() {
        Double expectedCost = 99.9;
        given(basket.calculateCost(promotionalRules)).willReturn(expectedCost);

        Double actual = checkout.total();
        verify(basket, times(1)).calculateCost(promotionalRules);

        assertEquals(expectedCost, actual);
    }

}