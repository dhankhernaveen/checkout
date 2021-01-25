package org.example;


import org.apache.commons.lang3.StringUtils;
import org.example.model.Item;
import org.example.model.PromotionalRules;

import java.util.Objects;
import java.util.logging.Logger;

public class Checkout {

    private final Basket basket;
    private final PromotionalRules promotionalRules;
    private final static Logger LOG = Logger.getLogger(Checkout.class.getName());

    //mentioned in problem statement , to Initialize basket I will have to use basket = new Basket() in this constructor;
    public Checkout(PromotionalRules promotionalRules) {
        this.promotionalRules = promotionalRules;
        basket = new Basket();
    }

    //needed for Unit test to mock basket
    Checkout(PromotionalRules promotionalRules, Basket basket) {
        this.promotionalRules = promotionalRules;
        this.basket = basket;
    }


    public void scan(Item item) {
        if (Objects.isNull(item) || StringUtils.isEmpty(item.getItemCode())) {
            LOG.warning("Invalid Item Scanned - Missing Mandatory details");
            return;
        }
        basket.addItemToCart(item);
    }

    public Double total() {
        return basket.calculateCost(promotionalRules);
    }
}
