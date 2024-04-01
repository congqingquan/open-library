package org.cqq.openlibrary.common.component.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Executors
 *
 * @author Qingquan.Cong
 */
public class Executors {

    private final CopyOnWriteArrayList<Executor> executors;


    public Executors(Collection<Executor> executors) {
        this.executors = new CopyOnWriteArrayList<>(executors);
    }

    public void add(Executor executor) {
        executors.add(executor);
    }

    public void remove(Executor executor) {
        executors.remove(executor);
    }

    public void set(int index, Executor executor) {
        executors.set(index, executor);
    }

    // ========================== Match =========================

    public Optional<Executor> matchFirst(Object data) {
        for (Executor et : executors) {
            if (et.match(data)) {
                return Optional.of(et);
            }
        }
        return Optional.empty();
    }

    public Executor matchFirstThrow(Object data) {
        return matchFirst(data).orElseThrow(() -> new RuntimeException("No supported executor"));
    }

    public List<Executor> matchAll(Object data) {
        List<Executor> results = new ArrayList<>();
        for (Executor et : executors) {
            if (et.match(data)) {
                results.add(et);
            }
        }
        return results;
    }


    // ========================== Build func =========================

    public static Executors create(Executor... executors) {
        return new Executors(new CopyOnWriteArrayList<>(executors));
    }
}