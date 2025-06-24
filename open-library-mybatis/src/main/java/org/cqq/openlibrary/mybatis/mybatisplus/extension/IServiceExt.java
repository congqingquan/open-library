package org.cqq.openlibrary.mybatis.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * IService extension
 *
 * @author Qingquan
 */
public interface IServiceExt<T> extends IService<T> {
    
    // ================================ getById ================================
    
    <X extends Throwable> T getById(Serializable id, X exception) throws X;
    
    // ================================ page ================================
    
    <E> IPage<E> page(IPage<T> pageParam, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper, Supplier<E> typeSupplier);
    
    <E> IPage<E> page(IPage<T> pageParam, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper, Function<T, E> mapper);
    
    // ================================ list ================================
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1, Supplier<E> typeSupplier);
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1,
                     SFunction<T, ?> c2, Object v2,
                     Supplier<E> typeSupplier);
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1,
                     SFunction<T, ?> c2, Object v2,
                     SFunction<T, ?> c3, Object v3,
                     Supplier<E> typeSupplier);
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1,
                     Function<T, E> mapper);
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1,
                     SFunction<T, ?> c2, Object v2,
                     Function<T, E> mapper);
    
    <E> List<E> list(SFunction<T, ?> c1, Object v1,
                     SFunction<T, ?> c2, Object v2,
                     SFunction<T, ?> c3, Object v3,
                     Function<T, E> mapper);
    
    // ================================ getOne ================================
    
    T getOne(SFunction<T, ?> c1, Object v1);
    
    T getOne(SFunction<T, ?> c1, Object v1,
             SFunction<T, ?> c2, Object v2);
    
    T getOne(SFunction<T, ?> c1, Object v1,
             SFunction<T, ?> c2, Object v2,
             SFunction<T, ?> c3, Object v3);
    
    // ================================ count ================================
    
    Long count(SFunction<T, ?> c1, Object v1);
    
    Long count(SFunction<T, ?> c1, Object v1,
               SFunction<T, ?> c2, Object v2);
    
    Long count(SFunction<T, ?> c1, Object v1,
               SFunction<T, ?> c2, Object v2,
               SFunction<T, ?> c3, Object v3);
    
    
    // ================================ exists ================================
    
    boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> excludeC, Collection<?> excludedV);
    
    
    boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> eqc2, Object eqv2,
                   SFunction<T, ?> excludeC, Collection<?> excludedV);
    
    boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> eqc2, Object eqv2,
                   SFunction<T, ?> eqc3, Object eqv3,
                   SFunction<T, ?> excludeC, Collection<?> excludedV);
    
    <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                           SFunction<T, ?> excludeC, Collection<?> excludedV,
                                           X exception) throws X;
    
    <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                           SFunction<T, ?> eqc2, Object eqv2,
                                           SFunction<T, ?> excludeC, Collection<?> excludedV,
                                           X exception) throws X;
    
    <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                           SFunction<T, ?> eqc2, Object eqv2,
                                           SFunction<T, ?> eqc3, Object eqv3,
                                           SFunction<T, ?> excludeC, Collection<?> excludedV,
                                           X exception) throws X;
    
    // ================================ update ================================
    
    boolean update(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> setC, Object setV);
    
    boolean update(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> eqc2, Object eqv2,
                   SFunction<T, ?> setC, Object setV);
    
    boolean update(SFunction<T, ?> eqc1, Object eqv1,
                   SFunction<T, ?> eqc2, Object eqv2,
                   SFunction<T, ?> eqc3, Object eqv3,
                   SFunction<T, ?> setC, Object setV);
    
    boolean updateIn(SFunction<T, ?> inc1, Collection<?> inv1,
                     SFunction<T, ?> setC, Object setV);
    
    // ================================ remove ================================
    
    boolean remove(SFunction<T, ?> c1, Object v1);
    
    boolean remove(SFunction<T, ?> c1, Object v1,
                   SFunction<T, ?> c2, Object v2);
    
    boolean remove(SFunction<T, ?> c1, Object v1,
                   SFunction<T, ?> c2, Object v2,
                   SFunction<T, ?> c3, Object v3);
    
    boolean removeIn(SFunction<T, ?> inc1, Collection<?> inv1);
}