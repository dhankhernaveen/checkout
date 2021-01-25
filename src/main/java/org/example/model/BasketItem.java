package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasketItem {

    private String itemCode;
    private Integer itemQuantity;
    private Double originalItemPrice;
    private Double reducedPrice;
    private Double finalPrice;
}