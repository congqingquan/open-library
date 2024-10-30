package org.cqq.openlibrary.common.util;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Bean utils (Based on CGLIB. Support deep copy) (Cannot work on JDK17)
 *
 * @author Qingquan
 */
@Deprecated
public class BeanUtils {

    public static <S, T> T copy(S source, T target) {
        if (Objects.isNull(source)) {
            return null;
        }
        if (Objects.isNull(target)) {
            return null;
        }
        BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
        return target;
    }

    public static <S, T> ArrayList<T> copy2ArrayList(List<S> sources, Supplier<T> targetSupplier) {
        return copyCollection(sources, targetSupplier, () -> new ArrayList<>(sources.size()));
    }

    public static <S, T, TC extends Collection<T>> TC copyCollection(Collection<S> sources, Supplier<T> targetSupplier, Supplier<TC> collectorSupplier) {
        if (CollectionUtils.isEmpty(sources)) {
            return collectorSupplier.get();
        }
        TC collector = collectorSupplier.get();
        for (S source : sources) {
            T target = copy(source, targetSupplier.get());
            collector.add(target);
        }
        return collector;
    }

    // Note: BeanCopier(A > B) cannot work on BeanCopier(B > A), must create a new BeanCopier(B > A)
    public enum BeanCopierCache {

        INSTANCE;

        private final ConcurrentHashMap<String, BeanCopier> cache = new ConcurrentHashMap<>();

        public BeanCopier get(Class<?> sourceClass, Class<?> targetClass) {
            return get(sourceClass, targetClass, null);
        }

        public BeanCopier get(Class<?> sourceClass, Class<?> targetClass, Converter converter) {
            final String key = genKey(sourceClass, targetClass, converter);
            BeanCopier beanCopier = cache.get(key);
            if (beanCopier == null) {
                beanCopier = BeanCopier.create(sourceClass, targetClass, converter != null);
                cache.put(key, beanCopier);
            }
            return beanCopier;
        }

        private String genKey(Class<?> sourceClass, Class<?> targetClass, Converter converter) {
            StringBuilder key = new StringBuilder()
                    .append(sourceClass.getName())
                    .append('#')
                    .append(targetClass.getName());
            if (converter != null) {
                key.append('#').append(converter.getClass().getName());
            }
            return key.toString();
        }
    }
}