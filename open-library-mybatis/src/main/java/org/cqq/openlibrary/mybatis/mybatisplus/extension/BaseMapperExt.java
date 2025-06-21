package org.cqq.openlibrary.mybatis.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.cqq.openlibrary.common.util.ReturnableBeanUtils;
import org.cqq.openlibrary.mybatis.mybatisplus.helper.LambdaWrapperHelper;
import org.cqq.openlibrary.mybatis.mybatisplus.helper.LambdaWrapperHelper.Condition;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Mybatis base mapper extension
 *
 * @author Qingquan
 */
public interface BaseMapperExt<T> extends BaseMapper<T> {
    
    // ================================ selectById ================================
    
    default <X extends Throwable> T selectById(Serializable id, X exception) throws X {
        return Optional.ofNullable(selectById(id)).orElseThrow(() -> exception);
    }
    
    // ================================ selectPage ================================
    
    static <T> IPage<T> getNonPagedQueryPageParam() {
        return new Page<>(1, -1);
    }
    
    default <E> IPage<E> selectPage(IPage<T> pageParam, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper, Supplier<E> typeSupplier) {
        return selectPage(pageParam, queryWrapper, r -> ReturnableBeanUtils.copyProperties(r, typeSupplier.get()));
    }
    
    default <E> IPage<E> selectPage(IPage<T> pageParam, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper, Function<T, E> mapper) {
        IPage<T> page = selectPage(pageParam, queryWrapper);
        IPage<E> returnPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        returnPage.setRecords(
                page.getRecords().stream().map(mapper).toList()
        );
        return returnPage;
    }
    
    // ================================ selectList ================================
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   Supplier<E> typeSupplier) {
        return selectList(c1, v1, null, null, null, null, typeSupplier);
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   Supplier<E> typeSupplier) {
        return selectList(c1, v1, c2, v2, null, null, typeSupplier);
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   SFunction<T, ?> c3, Object v3,
                                   Supplier<E> typeSupplier) {
        return selectList(
                c1, v1,
                c2, v2,
                c3, v3,
                e -> ReturnableBeanUtils.copyProperties(e, typeSupplier.get())
        );
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   Function<T, E> mapper) {
        return selectList(c1, v1, null, null, mapper);
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   Function<T, E> mapper) {
        return selectList(c1, v1, c2, v2, null, null, mapper);
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   SFunction<T, ?> c3, Object v3,
                                   Function<T, E> mapper) {
        return selectList(
                LambdaWrapperHelper.createQuery(
                        new LambdaWrapperHelper.Condition<>(c1, v1, SqlKeyword.EQ),
                        new Condition<>(c2, v2, SqlKeyword.EQ),
                        new Condition<>(c3, v3, SqlKeyword.EQ)
                )
        ).stream().map(mapper).toList();
    }
    
    // ================================ selectOne ================================
    
    default T selectOne(SFunction<T, ?> c1, Object v1) {
        return selectOne(c1, v1, null, null);
    }
    
    default T selectOne(SFunction<T, ?> c1, Object v1,
                        SFunction<T, ?> c2, Object v2) {
        return selectOne(c1, v1, c2, v2, null, null);
    }
    
    default T selectOne(SFunction<T, ?> c1, Object v1,
                        SFunction<T, ?> c2, Object v2,
                        SFunction<T, ?> c3, Object v3) {
        return selectOne(
                LambdaWrapperHelper.createQuery(
                        new Condition<>(c1, v1, SqlKeyword.EQ),
                        new Condition<>(c2, v2, SqlKeyword.EQ),
                        new Condition<>(c3, v3, SqlKeyword.EQ)
                )
        );
    }
    
    // ================================ selectCount ================================
    
    default Long selectCount(SFunction<T, ?> c1, Object v1) {
        return selectCount(c1, v1, null, null);
    }
    
    default Long selectCount(SFunction<T, ?> c1, Object v1,
                             SFunction<T, ?> c2, Object v2) {
        return selectCount(c1, v1, c2, v2, null, null);
    }
    
    default Long selectCount(SFunction<T, ?> c1, Object v1,
                             SFunction<T, ?> c2, Object v2,
                             SFunction<T, ?> c3, Object v3) {
        return selectCount(
                LambdaWrapperHelper.createQuery(
                        new Condition<>(c1, v1, SqlKeyword.EQ),
                        new Condition<>(c2, v2, SqlKeyword.EQ),
                        new Condition<>(c3, v3, SqlKeyword.EQ)
                )
        );
    }
    
    
    // ================================ exists ================================
    
    default boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return exists(eqc1, eqv1, null, null, null, null, excludeC, excludedV);
    }
    
    default boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> eqc2, Object eqv2,
                           SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return exists(eqc1, eqv1, eqc2, eqv2, null, null, excludeC, excludedV);
    }
    
    default boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> eqc2, Object eqv2,
                           SFunction<T, ?> eqc3, Object eqv3,
                           SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return exists(
                LambdaWrapperHelper.createQuery(
                        new Condition<>(eqc1, eqv1, SqlKeyword.EQ),
                        new Condition<>(eqc2, eqv2, SqlKeyword.EQ),
                        new Condition<>(eqc3, eqv3, SqlKeyword.EQ),
                        new Condition<>(CollectionUtils.isNotEmpty(excludedV), excludeC, excludedV, SqlKeyword.NOT_IN)
                )
        );
    }
    
    default <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                   SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                   X exception) throws X {
        existsThrow(eqc1, eqv1, null, null, excludeC, excludedV, exception);
    }
    
    default <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                   SFunction<T, ?> eqc2, Object eqv2,
                                                   SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                   X exception) throws X {
        existsThrow(eqc1, eqv1, eqc2, eqv2, null, null, excludeC, excludedV, exception);
    }
    
    default <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                   SFunction<T, ?> eqc2, Object eqv2,
                                                   SFunction<T, ?> eqc3, Object eqv3,
                                                   SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                   X exception) throws X {
        if (exists(eqc1, eqv1, eqc2, eqv2, eqc3, eqv3, excludeC, excludedV)) {
            throw exception;
        }
    }
    
    // ================================ update ================================
    
    default Integer update(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> setC, Object setV) {
        return update(eqc1, eqv1, null, null, null, null, setC, setV);
    }
    
    default Integer update(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> eqc2, Object eqv2,
                           SFunction<T, ?> setC, Object setV) {
        return update(eqc1, eqv1, eqc2, eqv2, null, null, setC, setV);
    }
    
    default Integer update(SFunction<T, ?> eqc1, Object eqv1,
                           SFunction<T, ?> eqc2, Object eqv2,
                           SFunction<T, ?> eqc3, Object eqv3,
                           SFunction<T, ?> setC, Object setV) {
        LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
        LambdaWrapperHelper.appendCondition(
                updateWrapper,
                List.of(
                        new Condition<>(eqc1, eqv1, SqlKeyword.EQ),
                        new Condition<>(eqc2, eqv2, SqlKeyword.EQ),
                        new Condition<>(eqc3, eqv3, SqlKeyword.EQ)
                )
        );
        updateWrapper.set(setC, setV);
        return update(updateWrapper);
    }
    
    default Integer updateIn(SFunction<T, ?> inc1, Collection<?> inv1,
                             SFunction<T, ?> setC, Object setV) {
        if (CollectionUtils.isEmpty(inv1)) {
            return 0;
        }
        return update(
                new LambdaUpdateWrapper<T>()
                        .in(inc1, inv1)
                        .set(setC, setV)
        );
    }
    
    // ================================ delete ================================
    
    default Integer delete(SFunction<T, ?> c1, Object v1) {
        return delete(c1, v1, null, null);
    }
    
    default Integer delete(SFunction<T, ?> c1, Object v1,
                           SFunction<T, ?> c2, Object v2) {
        return delete(c1, v1, c2, v2, null, null);
    }
    
    default Integer delete(SFunction<T, ?> c1, Object v1,
                           SFunction<T, ?> c2, Object v2,
                           SFunction<T, ?> c3, Object v3) {
        return delete(
                LambdaWrapperHelper.createQuery(
                        new Condition<>(c1, v1, SqlKeyword.EQ),
                        new Condition<>(c2, v2, SqlKeyword.EQ),
                        new Condition<>(c3, v3, SqlKeyword.EQ)
                )
        );
    }
    
    default Integer deleteIn(SFunction<T, ?> inc1, Collection<?> inv1) {
        if (CollectionUtils.isEmpty(inv1)) {
            return 0;
        }
        return delete(
                new LambdaQueryWrapper<T>()
                        .in(inc1, inv1)
        );
    }
}