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
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Tree utils
 *
 * @author CongQingquan
 */
public class TreeUtils {
    
    private TreeUtils() {
    }
    
    /**
     * 树化方法的参数封装接口
     */
    public interface ToTreeParam<T, ID> {
        
        /**
         * 获取id提取器 (id & pid同类型)
         */
        Function<T, ID> getIdExtractor();
        
        /**
         * 获取pid提取器 (id & pid同类型)
         */
        Function<T, ID> getPidExtractor();
        
        /**
         * 节点排序比较器
         */
        Comparator<? super T> getComparator();
        
        /**
         * 获取设置子节点的BiConsumer
         */
        BiConsumer<T, Collection<T>> getSettingChildrenBiConsumer();
        
        /**
         * 获取默认的根元素断言 (test: pid == null)
         */
        default Predicate<T> getRootPredicate() {
            return (n) -> this.getPidExtractor().apply(n) == null;
        }
    }
    
    /**
     * 树化
     *
     * @param param 参数封装类
     */
    public static <T, ID> TreeSet<T> toTree(Collection<T> nodes, ToTreeParam<T, ID> param) {
        if (param == null) {
            throw new IllegalArgumentException("To tree param cannot be null");
        }
        return toTree(nodes, param.getIdExtractor(), param.getPidExtractor(), param.getRootPredicate(),
                param.getComparator(), param.getSettingChildrenBiConsumer());
    }
    
    /**
     * 树化
     * 1. id data type === parent id data type
     * 2. 默认的，root node: pidExtractor.apply(node) == null & child node: pidExtractor.apply(node) != null
     *
     * @param nodes           节点集合
     * @param idExtractor     id提取器 (id & pid同类型)
     * @param pidExtractor    pid提取器 (id & pid同类型)
     * @param <T>             节点类型
     * @param <ID>            id数据类型 (id & pid同类型)
     * @param isRoot          根节点断言
     * @param comparator      节点排序比较器
     * @param settingChildren 设置子节点
     *                        1. 为什么用 set 的方式而不是 get 的方式？
     *                        考虑 get 的 children collection 可能为 null 的情况，初始化后无法进行 children 数据的写回，希望调用者提前写初始化操作
     *                        2. 调用者在初始化 children 容器时，根据容器底层使用的数据结构，可能会影响到 children 节点根据 comparator 的排序顺序
     */
    public static <T, ID> TreeSet<T> toTree(final Collection<T> nodes,
                                            final Function<T, ID> idExtractor,
                                            final Function<T, ID> pidExtractor,
                                            final Predicate<T> isRoot,
                                            final Comparator<? super T> comparator,
                                            final BiConsumer<T, Collection<T>> settingChildren) {
        if (idExtractor == null || pidExtractor == null) {
            throw new IllegalArgumentException("Extractor cannot be null");
        }
        if (isRoot == null) {
            throw new IllegalArgumentException("Root node predicate cannot be null");
        }
        final TreeSet<T> roots = new TreeSet<>(comparator);
        if (CollectionUtils.isEmpty(nodes)) {
            return roots;
        }
        final Map<ID, Collection<T>> childrenMapping = new HashMap<>(16);
        nodes.forEach(node -> {
            if (isRoot.test(node)) {
                roots.add(node);
                return;
            }
            childrenMapping.computeIfAbsent(pidExtractor.apply(node), (id) -> new TreeSet<>(comparator)).add(node);
        });
        
        final Deque<T> stack = new LinkedList<>();
        roots.forEach(root -> {
            stack.add(root);
            while (stack.size() > 0) {
                T currentNode = stack.pop();
                ID currentNodeId = idExtractor.apply(currentNode);
                Collection<T> cc = childrenMapping.get(currentNodeId);
                if (CollectionUtils.isEmpty(cc)) {
                    continue;
                }
                for (T cn : cc) {
                    stack.push(cn);
                }
                settingChildren.accept(currentNode, cc);
            }
        });
        return roots;
    }
    
    /**
     * 扁平化树形数据
     *
     * @param nodes             节点集合
     * @param childrenExtractor 子节点列表提取器
     * @param container         结果容器
     */
    public static <T, C extends Collection<? super T>> C flatMap(Collection<T> nodes,
                                                                 Function<T, Collection<? extends T>> childrenExtractor,
                                                                 Supplier<C> container) {
        C col = container.get();
        final Deque<T> stack = new LinkedList<>(nodes);
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
    
    public static void main(String[] args) {
        toTreeTest();
//        flatMapTest();
    }
    
    private static void toTreeTest() {
        // 组装数据
        @Data
        class Node {
            String id;
            String parentId;
            List<Node> children;
            
            public Node(String id, String parentId, List<Node> children) {
                this.id = id;
                this.parentId = parentId;
                this.children = children;
            }
        }
        
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
        
        // 1. 不使用参数封装
        TreeSet<Node> tree = TreeUtils.toTree(roots, Node::getId, Node::getParentId,
                (node) -> node.getParentId() == null,
                Comparator.comparing(Node::getId).reversed(),
                (node, cns) -> {
                    List<Node> cl = node.getChildren();
                    if (cl == null) {
                        node.setChildren(new ArrayList<>(cns));
                        return;
                    }
                    cl.addAll(cns);
                });
        tree.first();
        System.out.println(tree);
        
        // 2. 使用参数封装
        class DefaultToTreeParam implements ToTreeParam<Node, String> {
            @Override
            public Function<Node, String> getIdExtractor() {
                return Node::getId;
            }
            
            @Override
            public Function<Node, String> getPidExtractor() {
                return Node::getParentId;
            }
            
            @Override
            public Predicate<Node> getRootPredicate() {
                return (n) -> n.getId().length() == 2;
            }
            
            @Override
            public Comparator<? super Node> getComparator() {
                return Comparator.comparing(Node::getId).reversed();
            }
            
            @Override
            public BiConsumer<Node, Collection<Node>> getSettingChildrenBiConsumer() {
                return (node, cns) -> {
                    List<Node> cl = node.getChildren();
                    if (cl == null) {
                        node.setChildren(new ArrayList<>(cns));
                        return;
                    }
                    cl.addAll(cns);
                };
            }
        }
        
        Collection<Node> tree2 = TreeUtils.toTree(children, new DefaultToTreeParam());
        
        System.out.println(tree2);
    }
    
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Node {
        private String id;
        private String parentId;
        private List<SubNode> children;
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    static class SubNode extends Node {
        public SubNode(String id, String parentId, List<SubNode> children) {
            super(id, parentId, children);
        }
    }
    
    private static void flatMapTest() {
        
        LinkedList<SubNode> roots = new LinkedList<>();
        roots.add(new SubNode("A", null, new ArrayList<>(Arrays.asList(
                new SubNode("A1", null, new ArrayList<>(Arrays.asList(
                        new SubNode("A11", null, null)
                )))
        ))));
        roots.add(new SubNode("B", null, new ArrayList<>(Arrays.asList(
                new SubNode("B1", null, null)
        ))));
        roots.add(new SubNode("C", null, null));
        
        LinkedHashSet<Node> nodes = TreeUtils.flatMap(roots, SubNode::getChildren, LinkedHashSet::new);
        System.out.println(nodes);
    }
}
