package org.example;

import com.google.gson.Gson;
import org.example.model.Item;
import org.example.model.Items;
import org.example.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRepositoryTest {

    @Spy
    Gson gson;

    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemRepository(gson, "items.json");
    }

    @Test
    @DisplayName("Should fetch all the items from catalogue json")
    void shouldMapAndGetAllTheItems() {

        List<Item> allItems = List.of(
                Item.builder().itemCode("001").itemName("Travel Card Holder").itemPrice(9.25).build(),
                Item.builder().itemCode("002").itemName("Personalised cufflinks").itemPrice(45.00).build(),
                Item.builder().itemCode("003").itemName("Kids T-shirt").itemPrice(19.95).build()
        );

        Items actualItems = itemRepository.getAllItems();
        Assertions.assertEquals(allItems.size(), actualItems.getItems().size());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when fetching item list fails")
    @CsvSource({"fileNotFound.json", "test,json"})
    void shouldThrowException(String fileName) {
        itemRepository = new ItemRepository(gson, fileName);
        assertThrows(RuntimeException.class, () -> itemRepository.getAllItems());
    }

    @Test
    @DisplayName("Should return true when item is present in catalogue")
    void shouldReturnTrueWhenItemIsPresent() {
        assertTrue(itemRepository.isItemPresent("001"));
    }

    @Test
    @DisplayName("Should return false when item is not present in catalogue")
    void shouldReturnFalseWhenItemIsNotPresent() {
        assertFalse(itemRepository.isItemPresent("004"));
    }

    @Test
    @DisplayName("Should return false when item is not present in catalogue")
    void shouldReturnFalseWhenCatalogueIsEmpty() {
        itemRepository = new ItemRepository(gson, "itemsMissingDetails.json");

        assertFalse(itemRepository.isItemPresent("testItem"));
    }

    @Test
    @DisplayName("should return Item price")
    void shouldSuccessfullyReturnItemPrice() {
        assertEquals(9.25, itemRepository.getPrice("001"));
        assertEquals(45.00, itemRepository.getPrice("002"));
        assertEquals(19.95, itemRepository.getPrice("003"));
    }

    @Test
    @DisplayName("should throw exception when price requested for invalid Item code")
    void shouldFailWhenInvalidPriceRequested() {
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> itemRepository.getPrice("005"));

        assertTrue(actualException.getMessage().contains("Price missing or price requested for invalid item with itemCode"));

    }

    @Test
    @DisplayName("should throw exception when Item price is missing")
    void shouldFailIfItemIsMissingPriceDetails() {
        itemRepository = new ItemRepository(gson, "itemsWithMissingPrice.json");

        RuntimeException actualException = assertThrows(RuntimeException.class, () -> itemRepository.getPrice("001"));

        assertTrue(actualException.getMessage().contains("Price missing or price requested for invalid item with itemCode"));
    }
}