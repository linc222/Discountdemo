package org.example.discountdemo.service.strategy;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategyFactory {
    private final Map<Integer, CalculationStrategy> strategyMap = new HashMap<>();

    public StrategyFactory(List<CalculationStrategy> strategies) {
        strategies.forEach(s -> strategyMap.put(s.getType(), s));
    }

    public CalculationStrategy get(Integer type) {
        return strategyMap.get(type);
    }
}