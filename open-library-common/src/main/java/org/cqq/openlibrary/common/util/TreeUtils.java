package org.cqq.openlibrary.common.util;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.func.checked.CheckedBiConsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Tree utils
 *
 * @author Qingquan
 */
@Slf4j
public class TreeUtils {
    
    private TreeUtils() {
    }
    
    // ====================================== Create ======================================
    
    /**
     * 树化
     * 1. id data type === parent id data type
     * 2. 默认的，root node: pidExtractor.apply(node) == null & child node: pidExtractor.apply(node) != null
     *
     * @param nodes             节点集合
     * @param idExtractor       id 提取器 (id & pid同类型)
     * @param pidExtractor      pid 提取器 (id & pid同类型)
     * @param isRoot            根节点断言
     * @param childrenExtractor children 提取器
     * @param initChildren      初始化 children(当 node.children == null 时调用)
     * @param comparator        节点排序比较器
     */
    public static <T, ID> TreeSet<T> toTree(Collection<? extends T> nodes,
                                            Function<? super T, ID> idExtractor,
                                            Function<? super T, ID> pidExtractor,
                                            Predicate<? super T> isRoot,
                                            Function<? super T, Collection<? super T>> childrenExtractor,
                                            Consumer<? super T> initChildren,
                                            Comparator<? super T> comparator) {
        if (idExtractor == null || pidExtractor == null || childrenExtractor == null) {
            throw new IllegalArgumentException("Extractor cannot be null");
        }
        if (isRoot == null) {
            throw new IllegalArgumentException("Root node predicate cannot be null");
        }
        TreeSet<T> roots = new TreeSet<>(comparator);
        if (CollectionUtils.isEmpty(nodes)) {
            return roots;
        }
        Map<ID, TreeSet<T>> childrenMapping = new HashMap<>(16);
        for (T node : nodes) {
            if (isRoot.test(node)) {
                roots.add(node);
                continue;
            }
            childrenMapping.computeIfAbsent(pidExtractor.apply(node), (id) -> new TreeSet<>(comparator)).add(node);
        }
        Deque<T> stack = new LinkedList<>();
        for (T root : roots) {
            stack.add(root);
            while (!stack.isEmpty()) {
                T currentNode = stack.pop();
                ID currentNodeId = idExtractor.apply(currentNode);
                TreeSet<T> currentNodeChildren = childrenMapping.get(currentNodeId);
                
                // prev init children
                // Collection<? super T> existsChildren = childrenExtractor.apply(currentNode);
                // if (existsChildren == null) {
                //     initChildren.accept(currentNode);
                //     existsChildren = childrenExtractor.apply(currentNode);
                // }
                
                if (CollectionUtils.isEmpty(currentNodeChildren)) {
                    continue;
                }
                
                // lazy init children
                Collection<? super T> existsChildren = childrenExtractor.apply(currentNode);
                if (existsChildren == null) {
                    initChildren.accept(currentNode);
                    existsChildren = childrenExtractor.apply(currentNode);
                }
                
                existsChildren.addAll(currentNodeChildren);
                for (T cn : currentNodeChildren) {
                    stack.push(cn);
                }
            }
        }
        return roots;
    }
    
    /**
     * 扁平化树形数据
     *
     * @param nodes             节点集合
     * @param childrenExtractor 子节点列表提取器
     * @param container         结果容器
     */
    public static <T, C extends Collection<? super T>> C flat(Collection<? extends T> nodes,
                                                              Function<? super T, Collection<? extends T>> childrenExtractor,
                                                              Supplier<C> container) {
        C col = container.get();
        if (CollectionUtils.isEmpty(nodes)) {
            return col;
        }
        
        Deque<T> stack = new LinkedList<>(nodes);
        while (!stack.isEmpty()) {
            T pop = stack.pop();
            col.add(pop);
            Collection<? extends T> children = childrenExtractor.apply(pop);
            if (CollectionUtils.isNotEmpty(children)) {
                stack.addAll(children);
            }
        }
        return col;
    }
    
    /**
     * 树节点类型转换
     *
     * @param sourceNodes                 源节点集合
     * @param mapping                     类型转换过程
     * @param sourceNodeChildrenExtractor 源节点子节点列表提取器
     * @param initTargetNodeChildren      初始、返回目标节点的子节点容器
     * @param container                   目标列表容器
     */
    public static <T, R, C extends Collection<? super R>> C convert(Collection<? extends T> sourceNodes,
                                                                    BiFunction<T, LinkedList<? extends R>, R> mapping,
                                                                    Function<? super T, Collection<? extends T>> sourceNodeChildrenExtractor,
                                                                    Function<? super R, Collection<? super R>> initTargetNodeChildren,
                                                                    Supplier<C> container) {
        C col = container.get();
        if (CollectionUtils.isEmpty(sourceNodes)) {
            return col;
        }
        
        for (T sourceNode : sourceNodes) {
            col.add(
                    convertRecursion(
                            sourceNode, mapping, sourceNodeChildrenExtractor, initTargetNodeChildren, new LinkedList<>()
                    )
            );
        }
        return col;
    }
    
    private static <T, R> R convertRecursion(T sourceNode,
                                             BiFunction<T, LinkedList<? extends R>, R> mapping,
                                             Function<? super T, Collection<? extends T>> sourceNodesChildrenExtractor,
                                             Function<? super R, Collection<? super R>> initTargetNodeChildren,
                                             LinkedList<R> pathNodes) {
        
        R target = mapping.apply(sourceNode, pathNodes);
        
        // pre init target children
        // Collection<? super R> targetNodeChildren = initTargetNodeChildren.apply(target);
        
        Collection<? extends T> sourceNodeChildren = sourceNodesChildrenExtractor.apply(sourceNode);
        if (CollectionUtils.isEmpty(sourceNodeChildren)) {
            return target;
        }
        
        // lazy init target children
        Collection<? super R> targetNodeChildren = initTargetNodeChildren.apply(target);
        
        pathNodes.addLast(target);
        for (T sourceChild : sourceNodeChildren) {
            targetNodeChildren.add(
                    convertRecursion(sourceChild, mapping, sourceNodesChildrenExtractor, initTargetNodeChildren,
                            pathNodes)
            );
        }
        pathNodes.removeLast();
        
        return target;
    }
    
    // ====================================== Search ======================================
    
    /**
     * 迭代
     *
     * @param nodes                    节点列表
     * @param idExtractor              id 提取器
     * @param childrenExtractor        children 提取器
     * @param consumer                 消费节点
     * @param circularReferenceHandler 循环引用处理器
     */
    public static <T, ID, X extends Throwable> void foreach(Collection<? extends T> nodes,
                                                            Function<? super T, ID> idExtractor,
                                                            Function<? super T, Collection<? extends T>> childrenExtractor,
                                                            BiConsumer<LinkedHashMap<ID, ? extends T>, ? super T> consumer,
                                                            CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> circularReferenceHandler) throws X {
        breakableForeach(nodes, idExtractor, childrenExtractor, (pathNodes, node) -> {
            consumer.accept(pathNodes, node);
            return false;
        }, circularReferenceHandler);
    }
    
    /**
     * 可中断的迭代(当 consumer 返回 true 时终止迭代)
     *
     * @param nodes                    节点列表
     * @param idExtractor              id 提取器
     * @param childrenExtractor        children 提取器
     * @param consumer                 终止断言
     * @param circularReferenceHandler 循环引用处理器
     */
    public static <T, ID, X extends Throwable> void breakableForeach(Collection<? extends T> nodes,
                                                                     Function<? super T, ID> idExtractor,
                                                                     Function<? super T, Collection<? extends T>> childrenExtractor,
                                                                     BiFunction<LinkedHashMap<ID, ? extends T>, ? super T, Boolean> consumer,
                                                                     CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> circularReferenceHandler) throws X {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        for (T node : nodes) {
            if (breakableForeachRecursion(node, idExtractor, childrenExtractor, new LinkedHashMap<>(), consumer, circularReferenceHandler)) {
                return;
            }
        }
    }
    
    private static <T, ID, X extends Throwable> boolean breakableForeachRecursion(T node,
                                                                                  Function<? super T, ID> idExtractor,
                                                                                  Function<? super T, Collection<? extends T>> childrenExtractor,
                                                                                  LinkedHashMap<ID, T> pathNodeMap,
                                                                                  BiFunction<LinkedHashMap<ID, ? extends T>, ? super T, Boolean> consumer,
                                                                                  CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> circularReferenceHandler) throws X {
        // 循环引用检测 (根据主键)
        ID nid = idExtractor.apply(node);
        if (pathNodeMap.get(nid) != null) {
            log.error("Circular reference detected. Path node id [{}], current node id [{}]", pathNodeMap.keySet(), nid);
            circularReferenceHandler.accept(pathNodeMap, node);
            return false;
        }
        
        // 消费当前节点
        if (consumer.apply(pathNodeMap, node)) {
            return true;
        }
        
        // 当前节点加入路径
        pathNodeMap.put(nid, node);
        
        // 迭代子节点
        Collection<? extends T> children = childrenExtractor.apply(node);
        if (CollectionUtils.isEmpty(children)) {
            return false;
        }
        for (T child : children) {
            if (breakableForeachRecursion(child, idExtractor, childrenExtractor, pathNodeMap, consumer, circularReferenceHandler)) {
                return true;
            }
            // 迭代其他兄弟节点前，需要将自己在节点路径中移除
            pathNodeMap.remove(idExtractor.apply(child));
        }
        return false;
    }
    
    /**
     * 搜索链路上的所有父级节点
     *
     * @param nodes            节点集合
     * @param childNodes       子节点集合
     * @param idExtractor      id 提取器 (id & pid同类型)
     * @param pidExtractor     pid 提取器 (id & pid同类型)
     * @param isTopRoot        顶级根节点断言
     * @param container        结果容器
     * @param exceptionHandler 异常处理器
     */
    public static <T, ID, C extends Collection<? super T>, X extends Throwable> C searchParentNodes(Collection<? extends T> nodes,
                                                                                                    Collection<? extends T> childNodes,
                                                                                                    Function<? super T, ID> idExtractor,
                                                                                                    Function<? super T, ID> pidExtractor,
                                                                                                    Predicate<? super T> isTopRoot,
                                                                                                    Supplier<C> container,
                                                                                                    CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> exceptionHandler) throws X {
        C collector = container.get();
        if (CollectionUtils.isEmpty(nodes) || CollectionUtils.isEmpty(childNodes)) {
            return collector;
        }
        Map<ID, ? extends T> nodesMap = nodes.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
        Map<ID, T> addedNodeMap = childNodes.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
        for (T childNode : childNodes) {
            searchParentNodesRecursion(
                    nodesMap, addedNodeMap, new LinkedHashMap<>(),
                    childNode, idExtractor, pidExtractor, isTopRoot, collector, exceptionHandler
            );
        }
        return collector;
    }
    
    private static <T, ID, C extends Collection<? super T>, X extends Throwable> void searchParentNodesRecursion(Map<ID, ? extends T> nodesMap,
                                                                                                                 Map<ID, T> addedNodeMap,
                                                                                                                 LinkedHashMap<ID, T> pathNodeMap,
                                                                                                                 T node,
                                                                                                                 Function<? super T, ID> idExtractor,
                                                                                                                 Function<? super T, ID> pidExtractor,
                                                                                                                 Predicate<? super T> isTopRoot,
                                                                                                                 C collector,
                                                                                                                 CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> exceptionHandler) throws X {
        ID pid = pidExtractor.apply(node);
        T parentNode = nodesMap.get(pid);
        
        // 父节点判断
        // 1. 为根节点则结束递归
        if (isTopRoot.test(node)) {
            return;
        }
        // 2. 无效的父节点主键，
        else if (parentNode == null) {
            log.error("No parent node in nodes map. The current node id [{}], parent id [{}]", idExtractor.apply(node), pid);
            exceptionHandler.accept(pathNodeMap, node);
            return;
        }
        
        // 闭环检测
        if (pathNodeMap.get(pid) != null) {
            log.error("Circular reference detected. Path node id [{}], current node id [{}]", pathNodeMap.keySet(), idExtractor.apply(node));
            exceptionHandler.accept(pathNodeMap, node);
            return;
        }
        pathNodeMap.put(pid, parentNode);
        
        // 收集父节点
        if (addedNodeMap.get(pid) == null) {
            // 1. 收集父节点
            collector.add(parentNode);
            // 2. 缓存已添加的父节点，以防重复添加同个父节点
            addedNodeMap.put(pid, parentNode);
        }
        
        searchParentNodesRecursion(nodesMap, addedNodeMap, pathNodeMap, parentNode, idExtractor, pidExtractor, isTopRoot, collector, exceptionHandler);
    }
    
    /**
     * 搜索节点
     *
     * @param nodes                    节点列表
     * @param idExtractor              id 提取器
     * @param childrenExtractor        children 提取器
     * @param predicate                断言节点
     * @param circularReferenceHandler 循环引用处理器
     */
    public static <T, ID, X extends Throwable> Optional<T> searchNode(Collection<? extends T> nodes,
                                                                      Function<? super T, ID> idExtractor,
                                                                      Function<? super T, Collection<? extends T>> childrenExtractor,
                                                                      BiPredicate<LinkedHashMap<ID, ? extends T>, ? super T> predicate,
                                                                      CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> circularReferenceHandler) throws X {
        AtomicReference<T> res = new AtomicReference<>(null);
        breakableForeach(nodes, idExtractor, childrenExtractor, (pathNodeMap, node) -> {
            boolean test = predicate.test(pathNodeMap, node);
            if (test) {
                res.set(node);
            }
            return test;
        }, circularReferenceHandler);
        return Optional.ofNullable(res.get());
    }
    
    /**
     * 搜索所有符合的节点
     *
     * @param nodes                    节点列表
     * @param idExtractor              id 提取器
     * @param childrenExtractor        children 提取器
     * @param predicate                断言节点
     * @param circularReferenceHandler 循环引用处理器
     */
    public static <T, ID, X extends Throwable> List<T> searchAllNode(Collection<? extends T> nodes,
                                                                     Function<? super T, ID> idExtractor,
                                                                     Function<? super T, Collection<? extends T>> childrenExtractor,
                                                                     BiPredicate<LinkedHashMap<ID, ? extends T>, ? super T> predicate,
                                                                     CheckedBiConsumer<LinkedHashMap<ID, ? extends T>, T, X> circularReferenceHandler) throws X {
        List<T> resultNodes = new ArrayList<>();
        foreach(nodes, idExtractor, childrenExtractor, (pathNodeMap, node) -> {
            boolean test = predicate.test(pathNodeMap, node);
            if (test) {
                resultNodes.add(node);
            }
        }, circularReferenceHandler);
        return resultNodes;
    }
}
