package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Weight random
 *
 * <p>
 * Range:    0 ~ 100%
 * Interval: [0, 25%) / [25%, 50%) / [50%, 100%)
 * </p>
 *
 * @author Qingquan
 */
public class WeightRandom<T> {
    
    private final TreeMap<Double, T> weightMap = new TreeMap<>();
    
    private WeightRandom() {}
    
    public static <T> WeightRandom<T> create(LinkedHashMap<? extends Number, T> weighMap, boolean fair) {
        
        Collection<? extends Map.Entry<? extends Number, T>> entries = weighMap.entrySet();
        if (fair) {
            ArrayList<Map.Entry<? extends Number, T>> tempEntries = new ArrayList<>(entries);
            Collections.shuffle(tempEntries);
            entries = tempEntries;
        }
        
        WeightRandom<T> weightRandom = new WeightRandom<>();
        
        for (Map.Entry<? extends Number, T> entry : entries) {
            weightRandom.add(
                    entry.getKey().doubleValue(),
                    entry.getValue()
            );
        }
        
        return weightRandom;
    }
    
  
    private WeightRandom<T> add(Double weight, T value) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight cannot less than 0 or equal 0");
        }
        weight = weight + Optional.ofNullable(weightMap.lastEntry()).map(Map.Entry::getKey).orElse(0.0);
        weightMap.put(weight, value);
        return this;
    }
    
    public WeightRandom<T> clear() {
        this.weightMap.clear();
        return this;
    }
    
    public T get() {
        // nextDouble > [0, 1)，所以最大不会等于一个区间的右端点，符合了存储特性
        double randomWeight = this.weightMap.lastKey() * ThreadLocalRandom.current().nextDouble();
        // inclusive false 不匹配右区间，符合了存储特性
        SortedMap<Double, T> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }
}