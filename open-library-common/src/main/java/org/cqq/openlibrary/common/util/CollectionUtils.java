package org.cqq.openlibrary.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Collection utils
 *
 * @author Qingquan.Cong
 */
public class CollectionUtils {

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
    public static <T, C extends Collection<? super T>> C addAll(C coll, T... elements) {
        Collections.addAll(coll, elements);
        return coll;
    }

    // ====================================== Help method ======================================

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
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

    public static void main(String[] args) {
        deepForeachTest();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class DeepForeachNode {
        private String id;
        private List<DeepForeachNode> children;
    }

    private static void deepForeachTest() {
        DeepForeachNode deepForeachNode = new DeepForeachNode("A", Arrays.asList(
                new DeepForeachNode("A1", Arrays.asList(
                        new DeepForeachNode("A11", null),
                        new DeepForeachNode("A12", null)
                )),
                new DeepForeachNode("A2", null)
        ));

        deepForeach(Collections.singleton(deepForeachNode), DeepForeachNode::getChildren, (path, element) -> {
            System.out.println();
        });
    }
}
