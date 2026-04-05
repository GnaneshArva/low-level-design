
package com.food.delivery;

import java.util.List;

public class NearestAgentStrategy implements DeliveryStrategy {

    public DeliveryAgent assign(List<DeliveryAgent> agents) {
        for (DeliveryAgent agent : agents) {
            if (agent.isAvailable()) {
                agent.assign();
                return agent;
            }
        }
        throw new RuntimeException("No agent available");
    }
}
