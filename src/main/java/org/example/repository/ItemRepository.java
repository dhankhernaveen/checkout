package org.example.repository;

import com.google.gson.Gson;
import org.example.model.Item;
import org.example.model.Items;
import org.example.util.FileReader;

import java.util.Objects;
import java.util.Optional;

public class ItemRepository {

    private final Gson gson;
    private final String fileName;

    public ItemRepository(Gson gson, String fileName) {
        this.gson = gson;
        this.fileName = fileName;
    }

    //using file for now
    public Items getAllItems() {
        return getItemsFromItemsCatalogue();
    }

    public boolean isItemPresent(String itemCode) {
        long count = getItemsFromItemsCatalogue()
                .getItems()
                .stream()
                .filter(item -> itemCode.equals(item.getItemCode())).count();
        return count >= 1;
    }

    public Double getPrice(String itemCode) {
        Optional<Item> itemWithMatchingCode = getItemsFromItemsCatalogue()
                .getItems()
                .stream()
                .filter(item -> itemCode.equals(item.getItemCode())).findFirst();

        if (itemWithMatchingCode.isPresent() && Objects.nonNull(itemWithMatchingCode.get().getItemPrice())) {
            return itemWithMatchingCode.get().getItemPrice();
        } else
            throw new RuntimeException(String.format("Price missing or price requested for invalid item with itemCode %s", itemCode));
    }

    private Items getItemsFromItemsCatalogue() {
        String items = FileReader.readFile(fileName);
        return gson.fromJson(items, Items.class);
    }
}

