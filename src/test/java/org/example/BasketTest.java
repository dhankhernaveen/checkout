package org.example;

import org.example.model.Item;
import org.example.model.PromotionalRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketTest {

    @Mock
    ItemService itemService;

    @Mock
    BasketCostCalculator basketCostCalculator;

    @Captor
    ArgumentCaptor<Map<String, Integer>> basketItems;

    private Basket basket;

    @Mock
    PromotionalRules promotionalRules;

    @BeforeEach
    void setup() {
        basket = new Basket(itemService, basketCostCalculator);
    }

    @Test
    @DisplayName("Should successfully add item in cart")
    void addItemToCart() {
        Item item = Item.builder().itemCode("testItem").build();
        given(itemService.itemDoesNotExists(item.getItemCode())).willReturn(false);

        Map<String, Integer> expectedItemsInCart = Map.of("testItem", 1);

        basket.addItemToCart(item);

        Map<String, Integer> actualItemsInCart = basket.getItemsInCart();

        assertEquals(expectedItemsInCart, actualItemsInCart);

    }

    @Test
    @DisplayName("Should not add item in cart which is not found in catalouge")
    void shouldNotItemToCart() {
        Item item = Item.builder().itemCode("notFound").build();
        given(itemService.itemDoesNotExists(item.getItemCode())).willReturn(true);

        Map<String, Integer> expectedItemsInCart = Collections.emptyMap();

        basket.addItemToCart(item);

        Map<String, Integer> actualItemsInCart = basket.getItemsInCart();

        assertEquals(expectedItemsInCart, actualItemsInCart);

    }


    @Test
    @DisplayName("Should update quantity in cart when same item is added cart")
    void addItemAndUpdateQuantity() {
        Item testItem = Item.builder().itemCode("testItem").build();
        given(itemService.itemDoesNotExists(testItem.getItemCode())).willReturn(false);


        Map<String, Integer> expectedItemsInCart = Map.of("testItem", 2);

        basket.addItemToCart(testItem);
        basket.addItemToCart(testItem);

        Map<String, Integer> actualItemsInCart = basket.getItemsInCart();

        assertEquals(expectedItemsInCart, actualItemsInCart);

    }


    @Test
    @DisplayName("Should successfully add multiple items in cart")
    void addMultipleItemsToCart() {
        Item item1 = Item.builder().itemCode("item1").build();
        Item item2 = Item.builder().itemCode("item2").build();
        Item item3 = Item.builder().itemCode("item3").build();

        given(itemService.itemDoesNotExists(item1.getItemCode())).willReturn(false);
        given(itemService.itemDoesNotExists(item2.getItemCode())).willReturn(false);
        given(itemService.itemDoesNotExists(item3.getItemCode())).willReturn(false);

        Map<String, Integer> expectedItemsInCart = Map.of("item1", 2,
                "item2", 1,
                "item3", 1);

        basket.addItemToCart(item1);
        basket.addItemToCart(item1);
        basket.addItemToCart(item2);
        basket.addItemToCart(item3);

        Map<String, Integer> actualItemsInCart = basket.getItemsInCart();

        assertEquals(expectedItemsInCart, actualItemsInCart);

    }

    @Test
    @DisplayName("should get basket total based on items in cart and promotions")
    void basketTotal() {
        Item item1 = Item.builder().itemCode("item1").build();
        Item item2 = Item.builder().itemCode("item2").build();

        given(itemService.itemDoesNotExists(item1.getItemCode())).willReturn(false);
        given(itemService.itemDoesNotExists(item2.getItemCode())).willReturn(false);
        Map<String, Integer> expectedCartItems = Map.of("item1", 1,
                "item2", 1);

        basket.addItemToCart(item1);
        basket.addItemToCart(item2);

        basket.calculateCost(promotionalRules);

        verify(basketCostCalculator, times(1))
                .calculateBasketCost(basketItems.capture(), any());
        assertEquals(expectedCartItems, basketItems.getValue());
    }

    @ParameterizedTest
    @CsvSource({"73.755,73.76","18.999,19.00","5.001,5.00"})
    @DisplayName("should return calculated basket total cost and round off the deciaml points to 2")
    void shouldReturnBasketTotalCost(Double actualCost, Double expectedCost) {
        when(basketCostCalculator.calculateBasketCost(basketItems.capture(), any())).thenReturn(actualCost);
        Double actualPriceToBePaidByCustomer = basket.calculateCost(promotionalRules);
        assertEquals(expectedCost, actualPriceToBePaidByCustomer);

    }
}