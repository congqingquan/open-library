package org.cqq.openlibrary.common.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Enumeration utils
 *
 * @author Qingquan
 */
public class EnumerationUtils {
    
    private EnumerationUtils() {
    }
    
    public static <T> void foreach(Enumeration<? extends T> enumeration, Consumer<? super T> consumer) {
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement());
        }
    }
    
    public static <T, C extends Collection<? super T>> C toCollection(Enumeration<? extends T> source,
                                                                      Supplier<C> containerSupplier) {
        return toCollection(source, Function.identity(), containerSupplier);
    }
    
    public static <T, E, C extends Collection<? super E>> C toCollection(Enumeration<? extends T> source,
                                                                         Function<? super T, ? extends E> mapping,
                                                                         Supplier<C> containerSupplier) {
        C container = containerSupplier.get();
        while (source.hasMoreElements()) {
            container.add(mapping.apply(source.nextElement()));
        }
        return container;
    }
}
