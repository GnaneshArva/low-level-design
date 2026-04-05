
package com.food.delivery;

import java.util.List;

public interface DeliveryStrategy {
    DeliveryAgent assign(List<DeliveryAgent> agents);
}
