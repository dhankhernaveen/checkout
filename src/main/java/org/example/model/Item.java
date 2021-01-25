package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class Item {

    private String itemCode;
    private String itemName;

    // we should use BigDecimal for price
    private Double itemPrice;
}
