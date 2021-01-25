package org.example.acceptance;

import org.example.Checkout;
import org.example.model.Item;
import org.example.model.PromotionalRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutAcceptanceTest {

    private Checkout checkout;
    private Item item1;
    private Item item2;
    private Item item3;


    @BeforeEach
    void setUp() {
        PromotionalRules promotionalRules = new PromotionalRules();
        checkout = new Checkout(promotionalRules);
        item1 = Item.builder().itemCode("001").itemName("Travel Card Holder").itemPrice(9.25).build();
        item2 = Item.builder().itemCode("002").itemName("Personalised cufflinks").itemPrice(45.00).build();
        item3 = Item.builder().itemCode("003").itemName("Kids T-shirt").itemPrice(19.95).build();
    }

    @Test
    void scenario1() {
        checkout.scan(item1);
        checkout.scan(item2);
        checkout.scan(item3);

        Double actualBasketCost = checkout.total();

        assertEquals(66.78, actualBasketCost);
    }

    @Test
    void scenario2() {
        checkout.scan(item1);
        checkout.scan(item3);
        checkout.scan(item1);

        Double actualBasketCost = checkout.total();

        assertEquals(36.95, actualBasketCost);
    }

    @Test
    void scenario3() {
        checkout.scan(item1);
        checkout.scan(item2);
        checkout.scan(item1);
        checkout.scan(item3);

        Double actualBasketCost = checkout.total();

        assertEquals(73.76, actualBasketCost);
    }

    @Test
    void scenario1WithoutPromotion() {
        Checkout checkout = new Checkout(null);

        checkout.scan(item1);
        checkout.scan(item2);
        checkout.scan(item3);
        Double actualBasketCost = checkout.total();

        assertEquals(74.2, actualBasketCost);
    }

    @Test
    void scenario2WithoutPromotion() {
        Checkout checkout = new Checkout(PromotionalRules.builder().build());
        checkout.scan(item1);
        checkout.scan(item3);
        checkout.scan(item1);

        Double actualBasketCost = checkout.total();

        assertEquals(38.45, actualBasketCost);
    }

    @Test
    void scenario3WithoutPromotion() {
        Checkout checkout = new Checkout(PromotionalRules.builder().build());

        checkout.scan(item1);
        checkout.scan(item2);
        checkout.scan(item1);
        checkout.scan(item3);

        Double actualBasketCost = checkout.total();

        assertEquals(83.45, actualBasketCost);
    }
}
