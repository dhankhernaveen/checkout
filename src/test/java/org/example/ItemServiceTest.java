package org.example;

import org.example.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    ItemService itemService;

    @Mock
    ItemRepository itemRepository;

    @Test
    void shouldReturnTrueWhenItemExists() {
        given(itemRepository.isItemPresent("123")).willReturn(true);

        boolean actual = itemService.itemDoesNotExists("123");

        assertFalse(actual);
    }

    @Test
    void shouldReturnFalseWhenItemDoesNotExists() {
        given(itemRepository.isItemPresent("123")).willReturn(false);

        boolean actual = itemService.itemDoesNotExists("123");

        assertTrue(actual);
    }

    @Test
    void shouldReturnItemPrice() {
        given(itemRepository.getPrice("123")).willReturn(11.22);

        Double actual = itemService.getItemPrice("123");

        assertEquals(11.22, actual);
    }

    @Test
    void shouldPropagateExceptionFromRepository() {
        given(itemRepository.getPrice("123")).willThrow(new RuntimeException("Price missing for item code"));

        Assertions.assertThrows(RuntimeException.class, () -> itemService.getItemPrice("123"));
    }

}