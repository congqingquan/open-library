package org.cqq.openlibrary.common.persistent.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Param;
import org.cqq.openlibrary.common.util.ArrayUtils;
import org.cqq.openlibrary.common.util.ReturnableBeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Mybatis base mapper extension
 *
 * @author Qingquan
 */
public interface BaseMapperExt<T> extends BaseMapper<T> {
    
    // ================================ Page ================================
    
    default <E> IPage<E> selectPage(IPage<T> pageParam,
                                    @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                                    Supplier<E> typeSupplier) {
        
        IPage<T> page = selectPage(pageParam, queryWrapper);
        IPage<E> returnPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        returnPage.setRecords(
                page.getRecords().stream().map(r -> ReturnableBeanUtils.copyProperties(r, typeSupplier.get())).toList()
        );
        
        return returnPage;
    }
    
    default <E> IPage<E> selectPage(IPage<T> pageParam,
                                    @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                                    Function<T, E> typeMapping) {
        
        IPage<T> page = selectPage(pageParam, queryWrapper);
        IPage<E> returnPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        returnPage.setRecords(
                page.getRecords().stream().map(typeMapping).toList()
        );
        
        return returnPage;
    }
    
    // ================================ List ================================
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   Supplier<E> typeSupplier) {
        return selectList(
                c1, v1,
                e -> ReturnableBeanUtils.copyProperties(e, typeSupplier.get())
        );
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   Supplier<E> typeSupplier) {
        return selectList(
                c1, v1,
                c2, v2,
                e -> ReturnableBeanUtils.copyProperties(e, typeSupplier.get())
        );
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
                                   Function<T, E> typeMapping) {
        List<T> list = selectList(
                new LambdaQueryWrapper<T>().eq(c1, v1)
        );
        return list.stream().map(typeMapping).toList();
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   Function<T, E> typeMapping) {
        List<T> list = selectList(
                new LambdaQueryWrapper<T>().eq(c1, v1).eq(c2, v2)
        );
        return list.stream().map(typeMapping).toList();
    }
    
    default <E> List<E> selectList(SFunction<T, ?> c1, Object v1,
                                   SFunction<T, ?> c2, Object v2,
                                   SFunction<T, ?> c3, Object v3,
                                   Function<T, E> typeMapping) {
        List<T> list = selectList(
                new LambdaQueryWrapper<T>().eq(c1, v1).eq(c2, v2).eq(c3, v3)
        );
        return list.stream().map(typeMapping).toList();
    }
    
    // ================================ insert update ================================
    
    
    default Boolean insertOrUpdate(T entity) {
        return  Db.saveOrUpdate(entity);
    }
    
    default Boolean insertBatch(T... entities) {
        if (ArrayUtils.isEmpty(entities)) {
            return true;
        }
        return Db.saveBatch(List.of(entities), entities.length);
    }
    
    default Boolean updateBatch(T... entities) {
        if (ArrayUtils.isEmpty(entities)) {
            return true;
        }
        return Db.updateBatchById(List.of(entities), entities.length);
    }
    
    default Boolean insertOrUpdateBatch(T... entities) {
        if (ArrayUtils.isEmpty(entities)) {
            return true;
        }
        return Db.saveOrUpdateBatch(List.of(entities), entities.length);
    }
    
    // ================================ delete ================================
    
    default Integer delete(SFunction<T, ?> c1, Object v1) {
        return delete(
                new LambdaQueryWrapper<T>().eq(c1, v1)
        );
    }
    
    default Integer delete(SFunction<T, ?> c1, Object v1,
                           SFunction<T, ?> c2, Object v2) {
        return delete(
                new LambdaQueryWrapper<T>().eq(c1, v1).eq(c2, v2)
        );
    }
    
    default Integer delete(SFunction<T, ?> c1, Object v1,
                           SFunction<T, ?> c2, Object v2,
                           SFunction<T, ?> c3, Object v3) {
        return delete(
                new LambdaQueryWrapper<T>().eq(c1, v1).eq(c2, v2).eq(c3, v3)
        );
    }
}