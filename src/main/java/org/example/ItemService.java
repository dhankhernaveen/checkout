package org.example;

import org.example.repository.ItemRepository;

public class ItemService {

    ItemRepository itemRepository;

    ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public boolean itemDoesNotExists(String code) {
        return !itemRepository.isItemPresent(code);
    }

    public Double getItemPrice(String itemCode) {
        return itemRepository.getPrice(itemCode);
    }
}
