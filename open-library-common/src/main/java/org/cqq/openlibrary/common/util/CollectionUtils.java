package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

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

    // ====================================== Test method ======================================

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
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
        List<Integer> strings = newLinkedList(1, 2, 3, 4, 5, 6, 7, 8);
//        move(strings, (i, s) -> s.equals("C"), (i, s) -> s.equals("B"));
        move(strings, (i, str) -> str.equals(1), (i, str) -> str.equals(3));
        System.out.println(strings);
    }
}
