package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@Setter
public class BasketDetails {

    private List<BasketItem> basketItems;

    @Builder.Default
    private Double basketTotal = 0.0;

}
