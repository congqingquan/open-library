package org.cqq.openlibrary.mybatis.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ServiceImpl extension
 *
 * @author Qingquan
 */
public abstract class ServiceImplExt<M extends BaseMapperExt<T>, T> extends ServiceImpl<M, T> implements IServiceExt<T> {
    
    @Override
    public <X extends Throwable> T getById(Serializable id, X exception) throws X {
        return super.baseMapper.selectById(id, exception);
    }
    
    @Override
    public <E> IPage<E> page(IPage<T> pageParam, Wrapper<T> queryWrapper, Supplier<E> typeSupplier) {
        return super.baseMapper.selectPage(pageParam, queryWrapper, typeSupplier);
    }
    
    @Override
    public <E> IPage<E> page(IPage<T> pageParam, Wrapper<T> queryWrapper, Function<T, E> mapper) {
        return super.baseMapper.selectPage(pageParam, queryWrapper, mapper);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1, Supplier<E> typeSupplier) {
        return super.baseMapper.selectList(c1, v1, typeSupplier);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1,
                            SFunction<T, ?> c2, Object v2,
                            Supplier<E> typeSupplier) {
        return super.baseMapper.selectList(c1, v1, c2, v2, typeSupplier);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1,
                            SFunction<T, ?> c2, Object v2,
                            SFunction<T, ?> c3, Object v3,
                            Supplier<E> typeSupplier) {
        return super.baseMapper.selectList(c1, v1, c2, v2, c3, v3, typeSupplier);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1,
                            Function<T, E> mapper) {
        return super.baseMapper.selectList(c1, v1, mapper);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1,
                            SFunction<T, ?> c2, Object v2,
                            Function<T, E> mapper) {
        return super.baseMapper.selectList(c1, v1, c2, v2, mapper);
    }
    
    @Override
    public <E> List<E> list(SFunction<T, ?> c1, Object v1,
                            SFunction<T, ?> c2, Object v2,
                            SFunction<T, ?> c3, Object v3,
                            Function<T, E> mapper) {
        return super.baseMapper.selectList(c1, v1, c2, v2, c3, v3, mapper);
    }
    
    @Override
    public T getOne(SFunction<T, ?> c1, Object v1) {
        return super.baseMapper.selectOne(c1, v1);
    }
    
    @Override
    public T getOne(SFunction<T, ?> c1, Object v1,
                    SFunction<T, ?> c2, Object v2) {
        return super.baseMapper.selectOne(c1, v1, c2, v2);
    }
    
    @Override
    public T getOne(SFunction<T, ?> c1, Object v1,
                    SFunction<T, ?> c2, Object v2,
                    SFunction<T, ?> c3, Object v3) {
        return super.baseMapper.selectOne(c1, v1, c2, v2, c3, v3);
    }
    
    @Override
    public Long count(SFunction<T, ?> c1, Object v1) {
        return super.baseMapper.selectCount(c1, v1);
    }
    
    @Override
    public Long count(SFunction<T, ?> c1, Object v1,
                      SFunction<T, ?> c2, Object v2) {
        return super.baseMapper.selectCount(c1, v1, c2, v2);
    }
    
    @Override
    public Long count(SFunction<T, ?> c1, Object v1,
                      SFunction<T, ?> c2, Object v2,
                      SFunction<T, ?> c3, Object v3) {
        return super.baseMapper.selectCount(c1, v1, c2, v2, c3, v3);
    }
    
    @Override
    public boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return super.baseMapper.exists(eqc1, eqv1, excludeC, excludedV);
    }
    
    @Override
    public boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> eqc2, Object eqv2,
                          SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return super.baseMapper.exists(eqc1, eqv1, eqc2, eqv2, excludeC, excludedV);
    }
    
    @Override
    public boolean exists(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> eqc2, Object eqv2,
                          SFunction<T, ?> eqc3, Object eqv3,
                          SFunction<T, ?> excludeC, Collection<?> excludedV) {
        return super.baseMapper.exists(eqc1, eqv1, eqc2, eqv2, eqc3, eqv3, excludeC, excludedV);
    }
    
    @Override
    public <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                  SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                  X exception) throws X {
        super.baseMapper.existsThrow(eqc1, eqv1, excludeC, excludedV, exception);
    }
    
    @Override
    public <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                  SFunction<T, ?> eqc2, Object eqv2,
                                                  SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                  X exception) throws X {
        super.baseMapper.existsThrow(eqc1, eqv1, eqc2, eqv2, excludeC, excludedV, exception);
    }
    
    @Override
    public <X extends Throwable> void existsThrow(SFunction<T, ?> eqc1, Object eqv1,
                                                  SFunction<T, ?> eqc2, Object eqv2,
                                                  SFunction<T, ?> eqc3, Object eqv3,
                                                  SFunction<T, ?> excludeC, Collection<?> excludedV,
                                                  X exception) throws X {
        super.baseMapper.existsThrow(eqc1, eqv1, eqc2, eqv2, eqc3, eqv3, excludeC, excludedV, exception);
    }
    
    @Override
    public boolean update(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> setC, Object setV) {
        return super.baseMapper.update(eqc1, eqv1, setC, setV) > 0;
    }
    
    @Override
    public boolean update(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> eqc2, Object eqv2,
                          SFunction<T, ?> setC, Object setV) {
        return super.baseMapper.update(eqc1, eqv1, eqc2, eqv2, setC, setV) > 0;
    }
    
    @Override
    public boolean update(SFunction<T, ?> eqc1, Object eqv1,
                          SFunction<T, ?> eqc2, Object eqv2,
                          SFunction<T, ?> eqc3, Object eqv3,
                          SFunction<T, ?> setC, Object setV) {
        return super.baseMapper.update(eqc1, eqv1, eqc2, eqv2, eqc3, eqv3, setC, setV) > 0;
    }
    
    @Override
    public boolean updateIn(SFunction<T, ?> inc1, Collection<?> inv1,
                            SFunction<T, ?> setC, Object setV) {
        return super.baseMapper.updateIn(inc1, inv1, setC, setV) > 0;
    }
    
    @Override
    public boolean remove(SFunction<T, ?> c1, Object v1) {
        return super.baseMapper.delete(c1, v1) > 0;
    }
    
    @Override
    public boolean remove(SFunction<T, ?> c1, Object v1,
                          SFunction<T, ?> c2, Object v2) {
        return super.baseMapper.delete(c1, v1, c2, v2) > 0;
    }
    
    @Override
    public boolean remove(SFunction<T, ?> c1, Object v1,
                          SFunction<T, ?> c2, Object v2,
                          SFunction<T, ?> c3, Object v3) {
        return super.baseMapper.delete(c1, v1, c2, v2, c3, v3) > 0;
    }
    
    @Override
    public boolean removeIn(SFunction<T, ?> inc1, Collection<?> inv1) {
        return super.baseMapper.deleteIn(inc1, inv1) > 0;
    }
}
