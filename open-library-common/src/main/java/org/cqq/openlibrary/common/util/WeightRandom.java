package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Weight random
 *
 * Range:    0 ~ 100%
 * Interval: [0, 25%) / [25%, 50%) / [50%, 100%)
 *
 * @author CongQingquan
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
    
    public static void main(String[] args) {
        LinkedHashMap<Double, String> map = new LinkedHashMap<>();
        map.put(1.0, "A");
        map.put(39.0, "B");
        map.put(60.0, "C");
        
        WeightRandom<String> wr = WeightRandom.create(map, false);
        
        int total = 100_000;
        Map<String, Integer> counter = new HashMap<>();
        for (int i = 0; i < total; i++) {
            String d = wr.get();
            if (d == null) {
                System.err.println("1111");
            }
            counter.compute(d, (key, val) ->  {
                return val == null ? 1 : ++val;
            });
        }
        
        AtomicInteger tc = new AtomicInteger();
        counter.forEach((k, v) -> {
            tc.addAndGet(v);
            System.out.println(k + " > " + ((double) v / total));
        });
        
        System.out.println(tc.get());
    }
    
}