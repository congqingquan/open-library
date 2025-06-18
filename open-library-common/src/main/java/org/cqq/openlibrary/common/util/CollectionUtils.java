package org.cqq.openlibrary.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Collection utils
 *
 * @author Qingquan
 */
public class CollectionUtils {
    
    private CollectionUtils() {
    }
    
    // ====================================== Build method ======================================
    
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... elements) {
        return addAll(new ArrayList<>(elements.length), elements);
    }
    
    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(T... elements) {
        return addAll(new LinkedList<>(), elements);
    }
    
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... elements) {
        return addAll(new HashSet<>(elements.length), elements);
    }
    
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
        return addAll(new LinkedHashSet<>(elements.length), elements);
    }
    
    @SafeVarargs
    public static <T> TreeSet<T> newTreeSet(Comparator<? super T> comparator, T... elements) {
        return addAll(new TreeSet<>(comparator), elements);
    }
    
    @SafeVarargs
    public static <T, C extends Collection<? super T>> C addAll(C coll, T... elements) {
        Collections.addAll(coll, elements);
        return coll;
    }
    
    public static <T, C extends Collection<? super T>> C addAll(C coll, Collection<? extends T> elements) {
        coll.addAll(elements);
        return coll;
    }
    
    // ====================================== Helper method ======================================
    
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    public static <T> void deepForeach(Collection<? extends T> collection,
                                       Function<? super T, Collection<? extends T>> downField,
                                       BiConsumer<LinkedList<T>, T> action) {
        if (isEmpty(collection)) {
            return;
        }
        for (T element : collection) {
            Collection<? extends T> downColl = downField.apply(element);
            if (isEmpty(downColl)) {
                continue;
            }
            LinkedList<T> path = newLinkedList();
            action.accept(path, element);
            deepForeachRecursion(path, element, downField, action);
        }
    }
    
    public static <T> void deepForeachRecursion(LinkedList<T> path, T element,
                                                Function<? super T, Collection<? extends T>> downField,
                                                BiConsumer<LinkedList<T>, T> action) {
        Collection<? extends T> downColl = downField.apply(element);
        if (isEmpty(downColl)) {
            return;
        }
        path.addLast(element);
        for (T downElement : downColl) {
            action.accept(path, downElement);
            deepForeachRecursion(path, downElement, downField, action);
        }
        path.removeLast();
    }
    
    public static <E, C extends Collection<E>> boolean contains(C coll, Predicate<E> containPredicate) {
        if (isEmpty(coll)) {
            return false;
        }
        for (E element : coll) {
            if (containPredicate.test(element)) {
                return true;
            }
        }
        return false;
    }
    
    public static <T, K, C extends Collection<T>> Collection<T> intersectionSet(Collection<? extends T> coll,
                                                                                Collection<? extends T> anotherColl,
                                                                                Function<? super T, ? extends K> compareKey,
                                                                                Supplier<C> container) {
        coll = coll == null ? container.get() : coll;
        anotherColl = anotherColl == null ? container.get() : anotherColl;
        
        // swap
        Collection<? extends T> temp;
        if (anotherColl.size() < coll.size()) {
            temp = coll;
            coll = anotherColl;
            anotherColl = temp;
        }
        
        Map<? extends K, ? extends T> anotherCollMap = anotherColl.stream().collect(Collectors.toMap(compareKey, Function.identity()));
        C collector = container.get();
        for (T element : coll) {
            if (anotherCollMap.containsKey(compareKey.apply(element))) {
                collector.add(element);
            }
        }
        return collector;
    }
    
    public static <T, K, C extends Collection<T>> Collection<T> differenceSet(Collection<? extends T> coll,
                                                                              Collection<? extends T> anotherColl,
                                                                              Function<? super T, ? extends K> compareKey,
                                                                              boolean compareWithEachOther,
                                                                              Supplier<C> container) {
        coll = coll == null ? container.get() : coll;
        anotherColl = anotherColl == null ? container.get() : anotherColl;
        
        Map<K, T> collector = new HashMap<>();
        
        Map<? extends K, ? extends T> anotherCollMap = anotherColl.stream().collect(Collectors.toMap(compareKey, Function.identity()));
        for (T collElement : coll) {
            K key = compareKey.apply(collElement);
            if (anotherCollMap.get(key) == null) {
                collector.put(key, collElement);
            }
        }
        
        if (!compareWithEachOther) {
            return collector.values();
        }
        
        Map<? extends K, ? extends T> collMap = coll.stream().collect(Collectors.toMap(compareKey, Function.identity()));
        for (T anotherElement : anotherColl) {
            K key = compareKey.apply(anotherElement);
            if (collMap.get(key) == null && !collector.containsKey(key)) {
                collector.put(key, anotherElement);
            }
        }
        return collector.values();
    }
    
    public static <T, C extends Collection<T>> Collection<T> unionSet(Collection<? extends T> coll,
                                                                      Collection<? extends T> anotherColl,
                                                                      Supplier<C> container) {
        coll = coll == null ? container.get() : coll;
        anotherColl = anotherColl == null ? container.get() : anotherColl;
        
        C collector = container.get();
        collector.addAll(coll);
        
        return CollectionUtils.addAll(collector, anotherColl);
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompareResult<T, CT> {
        
        private Collection<CT> newEls;
        
        // one new element <-> some old elements
        private Map<CT, Collection<T>> existsInBothEls;
        
        private Collection<T> deletedEls;
    }
    
    public static <T, CT> CompareResult<T, CT> compare(Collection<T> coll,
                                                       Collection<CT> comparedColl,
                                                       BiPredicate<? super T, ? super CT> comparePredicate) {
        
        coll = coll == null ? newHashSet() : addAll(newArrayList(), coll);
        comparedColl = comparedColl == null ? newArrayList() : comparedColl;
        
        // compare
        Collection<CT> newEls = newArrayList();
        Map<CT, Collection<T>> existsInBothElementsMap = new HashMap<>();
        for (CT newElement : comparedColl) {
            Collection<T> existsInBothElementList = coll.stream().filter(oldElement -> comparePredicate.test(oldElement, newElement)).toList();
            if (isNotEmpty(existsInBothElementList)) {
                existsInBothElementsMap.put(newElement, existsInBothElementList);
            } else {
                newEls.add(newElement);
            }
            for (T existsInBothElement : existsInBothElementList) {
                coll.remove(existsInBothElement);
            }
        }
        Collection<T> deletedEls = newArrayList();
        deletedEls.addAll(coll);
        
        return new CompareResult<>(newEls, existsInBothElementsMap, deletedEls);
    }
    
    public static <T> Collection<Collection<T>> cartesianProduct(Collection<Collection<? extends T>> collections) {
        Collection<Collection<T>> initialState = newArrayList(newArrayList());
        if (isEmpty(collections)) {
            return initialState;
        }
        return collections.stream().reduce(initialState, (prev, curr) -> {
            if (isEmpty(curr)) {
                return prev;
            }
            Collection<Collection<T>> res = new ArrayList<>();
            for (Collection<T> pt : prev) {
                for (T ct : curr) {
                    res.add(addAll(new ArrayList<>(pt), ct));
                }
            }
            return res;
        }, (left, right) -> {
            Collection<Collection<T>> res = new ArrayList<>(left);
            res.addAll(right);
            return res;
        });
    }
    
    // ====================================== Collection method ======================================
    
    public static <T> Optional<T> getLast(Collection<T> collection) {
        return collection.isEmpty() ? Optional.empty() : collection.stream().skip(collection.size() - 1).findFirst();
    }
    
    // ====================================== List method ======================================
    
    public static <T> int indexOf(List<? extends T> list, BiPredicate<Integer, ? super T> predicate) {
        int idx = -1;
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(i, list.get(i))) {
                idx = i;
                break;
            }
        }
        return idx;
    }
    
    public static <T> void moveFirst(List<T> list, BiPredicate<Integer, ? super T> from) {
        int removeIndex = indexOf(list, from);
        if (removeIndex == -1) {
            return;
        }
        list.add(0, list.remove(removeIndex));
    }
    
    public static <T> void moveLast(List<T> list, BiPredicate<Integer, ? super T> from) {
        int removeIndex = indexOf(list, from);
        if (removeIndex == -1) {
            return;
        }
        T remove = list.remove(removeIndex);
        list.add(list.size(), remove);
    }
    
    // cur < target: insert current element after target element
    // cur > target: insert current element before target element
    public static <T> void move(List<T> list, BiPredicate<Integer, ? super T> from, BiPredicate<Integer, ? super T> to) {
        if (isEmpty(list)) {
            return;
        }
        int removeIndex = indexOf(list, from);
        int insertIndex = indexOf(list, to);
        if (removeIndex == -1 || insertIndex == -1) {
            return;
        }
        if (insertIndex == removeIndex) {
            return;
        }
        T remove = list.remove(removeIndex);
        list.add(insertIndex, remove);
    }
}
