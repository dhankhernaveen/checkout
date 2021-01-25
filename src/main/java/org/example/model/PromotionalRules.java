package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.PromotionRule;
import org.example.ReducedPricePromotionRule;
import org.example.TotalCostPromotionRule;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PromotionalRules {

    List<PromotionRule> promotionRules;

    public PromotionalRules() {
        promotionRules = new ArrayList<>();
        promotionRules.add(new ReducedPricePromotionRule());
        promotionRules.add(new TotalCostPromotionRule());
    }

}
