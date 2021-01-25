package org.example;

import org.example.model.BasketDetails;

public interface PromotionRule {

    void applyPromotions(BasketDetails basketDetails);
}
