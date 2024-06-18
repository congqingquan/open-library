package org.cqq.openlibrary.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
 * @author CongQingquan
 */
public class TreeUtils {
    
    private TreeUtils() {
    }
    
    // ====================================== Generate ======================================
    
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
            while (stack.size() > 0) {
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
        while (stack.size() > 0) {
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
     * 搜索链路上的所有父级节点(若根据 pidExtractor 检测到父级节点同时在 childNodes 中，则该节点会被忽略)
     *
     * @param nodes        节点集合
     * @param childNodes   子节点集合
     * @param idExtractor  id 提取器 (id & pid同类型)
     * @param pidExtractor pid 提取器 (id & pid同类型)
     * @param isTopRoot    顶级根节点断言
     * @param container    结果容器
     */
    public static <T, ID, C extends Collection<? super T>> C searchParentNodes(Collection<? extends T> nodes,
                                                                               Collection<? extends T> childNodes,
                                                                               Function<? super T, ID> idExtractor,
                                                                               Function<? super T, ID> pidExtractor,
                                                                               Predicate<? super T> isTopRoot,
                                                                               Supplier<C> container) {
        C collector = container.get();
        if (CollectionUtils.isEmpty(nodes) || CollectionUtils.isEmpty(childNodes)) {
            return null;
        }
        Map<ID, ? extends T> nodesMap = nodes.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
        Map<ID, ? extends T> searchCacheMap = childNodes.stream().collect(Collectors.toMap(idExtractor, Function.identity()));
        for (T childNode : childNodes) {
            searchParentNodesRecursion(nodesMap, searchCacheMap, childNode, idExtractor, pidExtractor, isTopRoot, collector);
        }
        return collector;
    }
    
    private static <T, ID, C extends Collection<? super T>> void searchParentNodesRecursion(Map<ID, ? extends T> nodesMap,
                                                                                            Map<ID, ? extends T> searchCacheMap,
                                                                                            T node,
                                                                                            Function<? super T, ID> idExtractor,
                                                                                            Function<? super T, ID> pidExtractor,
                                                                                            Predicate<? super T> isTopRoot,
                                                                                            C collector) {
        ID pid = pidExtractor.apply(node);
        T parentNode = nodesMap.get(pid);
        if (isTopRoot.test(node)) {
            return;
        } else if (parentNode == null) {
            throw new RuntimeException(String.format("No parent node in nodes map. The current node id [%s], parent id [%s]", idExtractor.apply(node), pid));
        }
        
        if (searchCacheMap.get(pid) == null) {
            collector.add(parentNode);
        }
        
        searchParentNodesRecursion(nodesMap, searchCacheMap, parentNode, idExtractor, pidExtractor, isTopRoot, collector);
    }
    
    /**
     * 搜索节点
     *
     * @param nodes             节点列表
     * @param childrenExtractor children 提取器
     * @param predicate         断言节点
     */
    public static <T> Optional<T> searchNode(Collection<? extends T> nodes,
                                          Function<? super T, Collection<? extends T>> childrenExtractor,
                                          BiPredicate<LinkedList<? extends T>, ? super T> predicate) {
        AtomicReference<Optional<T>> res = new AtomicReference<>(Optional.empty());
        breakableForeach(nodes, childrenExtractor, (pathNodes, node) -> {
            boolean test = predicate.test(pathNodes, node);
            if (test) {
                res.set(Optional.ofNullable(node));
            }
            return test;
        });
        return res.get();
    }
    
    /**
     * 搜索所有符合的节点
     *
     * @param nodes             节点列表
     * @param childrenExtractor children 提取器
     * @param predicate         断言节点
     */
    public static <T> List<T> searchAllNode(Collection<? extends T> nodes,
                                             Function<? super T, Collection<? extends T>> childrenExtractor,
                                             BiPredicate<LinkedList<? extends T>, ? super T> predicate) {
        List<T> resultNodes = new ArrayList<>();
        foreach(nodes, childrenExtractor, (pathNodes, node) -> {
            boolean test = predicate.test(pathNodes, node);
            if (test) {
                resultNodes.add(node);
            }
        });
        return resultNodes;
    }
    
    /**
     * 迭代
     *
     * @param nodes             节点列表
     * @param childrenExtractor children 提取器
     * @param consumer          消费节点
     */
    public static <T> void foreach(Collection<? extends T> nodes,
                                   Function<? super T, Collection<? extends T>> childrenExtractor,
                                   BiConsumer<LinkedList<? extends T>, ? super T> consumer) {
        breakableForeach(nodes, childrenExtractor, (pathNodes, node) -> {
            consumer.accept(pathNodes, node);
            return false;
        });
    }
    
    /**
     * 可中断的迭代(当 consumer 返回 true 时终止迭代)
     *
     * @param nodes             节点列表
     * @param childrenExtractor children 提取器
     * @param consumer          终止断言
     */
    public static <T> void breakableForeach(Collection<? extends T> nodes,
                                            Function<? super T, Collection<? extends T>> childrenExtractor,
                                            BiPredicate<LinkedList<? extends T>, ? super T> consumer) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        for (T node : nodes) {
            if (breakableForeachRecursion(node, new LinkedList<>(), childrenExtractor, consumer)) {
                return;
            }
        }
    }
    
    private static <T> boolean breakableForeachRecursion(T node,
                                                         LinkedList<T> pathNodes,
                                                         Function<? super T, Collection<? extends T>> childrenExtractor,
                                                         BiPredicate<LinkedList<? extends T>, ? super T> consumer) {
        if (consumer.test(pathNodes, node)) {
            return true;
        }
        Collection<? extends T> children = childrenExtractor.apply(node);
        if (CollectionUtils.isEmpty(children)) {
            return false;
        }
        pathNodes.add(node);
        for (T child : children) {
            if (breakableForeachRecursion(child, pathNodes, childrenExtractor, consumer)) {
                return true;
            }
        }
        return false;
    }
    
    
    private static class Test {
        public static void main(String[] args) {
//            toTreeTest();
//            flatMapTest();
//            convertTest();
//            searchParentNodeTest();
            searchNodeTest();
//            foreachTest();
        }
        
        @Data
        static class Node {
            String id;
            String parentId;
            List<Node> children;
            
            public Node(String id, String parentId, List<Node> children) {
                this.id = id;
                this.parentId = parentId;
                this.children = children;
            }
        }
        
        private static TreeSet<Node> toTreeTest() {
            // 组装数据
            LinkedList<Node> roots = new LinkedList<>();
            roots.add(new Node("A", null, null));
            roots.add(new Node("B", null, null));
            roots.add(new Node("C", null, null));
            
            
            LinkedList<Node> children = new LinkedList<>();
            children.add(new Node("A1", "A", null));
            children.add(new Node("A11", "A1", null));
            children.add(new Node("A2", "A", null));
            children.add(new Node("B1", "B", null));
            children.add(new Node("C1", "C", null));
            children.add(new Node("C11", "C1", null));
            
            roots.addAll(children);
            
            TreeSet<Node> tree = TreeUtils.toTree(
                    roots,
                    Node::getId,
                    Node::getParentId,
                    (node) -> node.getParentId() == null,
                    Node::getChildren, node -> node.setChildren(new ArrayList<>()),
                    Comparator.comparing(Node::getId).reversed()
            );
            System.out.println(tree);
            
            return tree;
        }
        
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class FlatNode {
            private String id;
            private String parentId;
            private List<SubFlatNode> children;
        }
        
        @Data
        @EqualsAndHashCode(callSuper = true)
        static class SubFlatNode extends FlatNode {
            public SubFlatNode(String id, String parentId, List<SubFlatNode> children) {
                super(id, parentId, children);
            }
        }
        
        private static void flatMapTest() {
            
            LinkedList<SubFlatNode> roots = new LinkedList<>();
            roots.add(new SubFlatNode("A", null, new ArrayList<>(Arrays.asList(
                    new SubFlatNode("A1", null, new ArrayList<>(Arrays.asList(
                            new SubFlatNode("A11", null, null)
                    )))
            ))));
            roots.add(new SubFlatNode("B", null, new ArrayList<>(Arrays.asList(
                    new SubFlatNode("B1", null, null)
            ))));
            roots.add(new SubFlatNode("C", null, null));
            
            LinkedHashSet<FlatNode> nodes = TreeUtils.flat(roots, SubFlatNode::getChildren, LinkedHashSet::new);
            System.out.println(nodes);
        }
        
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class ConvertNode {
            private String id;
            private String parentId;
            private List<ConvertNode> children;
        }
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class AnotherConvertNode {
            private String id;
            private String parentId;
            private String path;
            private List<AnotherConvertNode> children;
        }
        
        
        private static void convertTest() {
            LinkedList<ConvertNode> roots = new LinkedList<>();
            roots.add(new ConvertNode("A", null, new ArrayList<>(Arrays.asList(
                    new ConvertNode("A1", null, new ArrayList<>(Arrays.asList(
                            new ConvertNode("A11", null, null)
                    )))
            ))));
            roots.add(new ConvertNode("B", null, new ArrayList<>(Arrays.asList(
                    new ConvertNode("B1", null, null)
            ))));
            roots.add(new ConvertNode("C", null, null));
            
            List<AnotherConvertNode> nodes = TreeUtils.convert(
                    roots,
                    (node, pathNodes) -> {
                        AnotherConvertNode anotherConvertNode = new AnotherConvertNode();
                        anotherConvertNode.setId(node.getId());
                        anotherConvertNode.setParentId(node.getParentId());

                        List<AnotherConvertNode> temp = new ArrayList<>(pathNodes);
                        temp.add(anotherConvertNode);
                        anotherConvertNode.setPath(temp.stream().map(AnotherConvertNode::getId).collect(Collectors.joining(",")));
                        return anotherConvertNode;
                    },
                    ConvertNode::getChildren,
                    r -> {
                        r.setChildren(new ArrayList<>());
                        return r.getChildren();
                    },
                    ArrayList::new);
            
            System.out.println(nodes);
        }
        
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class SearchParentNode {
            private String id;
            private String parentId;
            private List<SearchParentNode> children;
        }
        
        private static void searchParentNodeTest() {
            LinkedList<SearchParentNode> nodes = new LinkedList<>();
            
            nodes.addAll(Arrays.asList(
                    new SearchParentNode("A", null, null),
                    new SearchParentNode("A1", "A", null),
                    new SearchParentNode("A11", "A1", null)
            ));
            
            nodes.addAll(Arrays.asList(
                    new SearchParentNode("B", null, null),
                    new SearchParentNode("B1", "B", null)
            ));
            
            
            Collection<SearchParentNode> childNodes = Arrays.asList(
                    new SearchParentNode("A11", "A1", null),
                    new SearchParentNode("B1", "B", null)
            );
            
            // 1. 所有出缺失的父节点
            Collection<SearchParentNode> searchParentNodes = searchParentNodes(
                    nodes,
                    childNodes,
                    SearchParentNode::getId,
                    SearchParentNode::getParentId,
                    node -> node.getParentId() == null,
                    ArrayList::new);
            
            // 2. 汇总
            childNodes = new ArrayList<>(childNodes);
            childNodes.addAll(searchParentNodes);
            
            // 3. 转为树
            TreeSet<SearchParentNode> tree = toTree(
                    childNodes,
                    SearchParentNode::getId,
                    SearchParentNode::getParentId,
                    node -> node.getParentId() == null,
                    SearchParentNode::getChildren,
                    node -> node.setChildren(new ArrayList<>()),
                    Comparator.comparing(SearchParentNode::getId)
            );
            
            System.out.println(tree);
        }
        
        private static void searchNodeTest() {
            TreeSet<Node> tree = toTreeTest();

            Optional<Node> res = searchNode(tree, Node::getChildren, (nodes, node) -> {
                System.out.printf("Node [%s] Path nodes: [%s]%n", node.getId(), nodes.stream().map(Node::getId).collect(Collectors.joining("-")));
                return "A1".equals(node.getId());
            });
            System.out.println(res);
        }
        
        private static void foreachTest() {
            TreeSet<Node> tree = toTreeTest();
            
            System.out.println("======================= foreach =======================");
            
            foreach(tree, Node::getChildren, (nodes, node) -> {
                System.out.printf("Node [%s] Path nodes: [%s]%n", node.getId(), nodes.stream().map(Node::getId).collect(Collectors.joining("-")));
            });
            
            System.out.println("======================= breakableForeach =======================");
            
            breakableForeach(tree, Node::getChildren, (nodes, node) -> {
                System.out.printf("Node [%s] Path nodes: [%s]%n", node.getId(), nodes.stream().map(Node::getId).collect(Collectors.joining("-")));
                return "B1".equals(node.getId());
            });
        }
    }
}
