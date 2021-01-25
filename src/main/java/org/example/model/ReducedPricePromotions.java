package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReducedPricePromotions {

    private String itemCode;
    private Integer minimumQuantity;
    private Double offerPrice;
}
