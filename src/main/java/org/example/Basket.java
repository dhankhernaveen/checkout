package org.example;

import com.google.gson.Gson;
import org.apache.commons.math3.util.Precision;
import org.example.model.Item;
import org.example.model.PromotionalRules;
import org.example.repository.ItemRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Basket {

    private final ItemService itemService;
    private final BasketCostCalculator basketCostCalculator;
    private final Map<String, Integer> itemsInCart;
    private final static Logger LOG = Logger.getLogger(Basket.class.getName());


    public Basket() {
        itemService = new ItemService(new ItemRepository(new Gson(), "items.json"));
        basketCostCalculator = new BasketCostCalculator(itemService);
        itemsInCart = new LinkedHashMap<>();
    }

    public Basket(ItemService itemService, BasketCostCalculator basketCostCalculator) {
        this.itemService = itemService;
        this.basketCostCalculator = basketCostCalculator;
        itemsInCart = new LinkedHashMap<>();
    }

    public void addItemToCart(Item item) {
        if (itemService.itemDoesNotExists(item.getItemCode())) {
            LOG.warning("Item scanned is missing from Items catalogue.");
            return;
        }
        addOrUpdateItemInCart(item);
    }

    public Map<String, Integer> getItemsInCart() {
        return itemsInCart;
    }

    private void addOrUpdateItemInCart(Item item) {
        if (itemsInCart.containsKey(item.getItemCode())) {
            Integer quantity = itemsInCart.get(item.getItemCode()) + 1;
            itemsInCart.replace(item.getItemCode(), quantity);
        } else {
            itemsInCart.put(item.getItemCode(), 1);
        }
    }

    public Double calculateCost(PromotionalRules promotionalRules) {
        Double totalCartCost = basketCostCalculator.calculateBasketCost(itemsInCart, promotionalRules);
        return Precision.round(totalCartCost, 2);
    }
}
