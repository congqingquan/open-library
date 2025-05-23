package org.cqq.openlibrary.storage;

import java.util.function.Supplier;

/**
 * Storage provider
 *
 * @author Qingquan
 */
@FunctionalInterface
public interface StorageProvider extends Supplier<Storage> {
}
