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
import java.util.function.BiFunction;
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
    public static <T, ID> TreeSet<T> toTree(final Collection<? extends T> nodes,
                                            final Function<? super T, ID> idExtractor,
                                            final Function<? super T, ID> pidExtractor,
                                            final Predicate<? super T> isRoot,
                                            final Function<? super T, Collection<? super T>> childrenExtractor,
                                            final Consumer<? super T> initChildren,
                                            final Comparator<? super T> comparator) {
        if (idExtractor == null || pidExtractor == null || childrenExtractor == null) {
            throw new IllegalArgumentException("Extractor cannot be null");
        }
        if (isRoot == null) {
            throw new IllegalArgumentException("Root node predicate cannot be null");
        }
        final TreeSet<T> roots = new TreeSet<>(comparator);
        if (CollectionUtils.isEmpty(nodes)) {
            return roots;
        }
        final Map<ID, TreeSet<T>> childrenMapping = new HashMap<>(16);
        for (T node : nodes) {
            if (isRoot.test(node)) {
                roots.add(node);
                continue;
            }
            childrenMapping.computeIfAbsent(pidExtractor.apply(node), (id) -> new TreeSet<>(comparator)).add(node);
        }
        final Deque<T> stack = new LinkedList<>();
        for (T root : roots) {
            stack.add(root);
            while (stack.size() > 0) {
                T currentNode = stack.pop();
                ID currentNodeId = idExtractor.apply(currentNode);
                TreeSet<T> currentNodeChildren = childrenMapping.get(currentNodeId);
                if (CollectionUtils.isEmpty(currentNodeChildren)) {
                    continue;
                }
                for (T cn : currentNodeChildren) {
                    stack.push(cn);
                }
                Collection<? super T> existsChildren = childrenExtractor.apply(currentNode);
                if (existsChildren == null) {
                    initChildren.accept(currentNode);
                }
                existsChildren = childrenExtractor.apply(currentNode);
                existsChildren.addAll(currentNodeChildren);
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

    /**
     * 树节点类型转换
     *
     * @param sourceNodes                 源节点集合
     * @param sourceNodeChildrenExtractor 源节点子节点列表提取器
     * @param targetNodeChildrenExtractor 目标节点子节点列表提取器
     * @param mapping                     类型转换过程
     * @param container                   目标列表容器
     */
    public static <T, R, C extends Collection<? super R>> C convert(Collection<? extends T> sourceNodes,
                                                                    Function<? super T, Collection<? extends T>> sourceNodeChildrenExtractor,
                                                                    Function<? super R, Collection<? super R>> targetNodeChildrenExtractor,
                                                                    Consumer<? super R> initTargetChildren,
                                                                    BiFunction<T, LinkedList<? extends R>, R> mapping,
                                                                    Supplier<C> container) {
        C col = container.get();
        for (T sourceNode : sourceNodes) {
            col.add(
                    convertRecursion(
                            sourceNode, sourceNodeChildrenExtractor, targetNodeChildrenExtractor, initTargetChildren, new LinkedList<>(), mapping
                    )
            );
        }
        return col;
    }

    private static <T, R> R convertRecursion(T sourceNode,
                                             Function<? super T, Collection<? extends T>> sourceNodesChildrenExtractor,
                                             Function<? super R, Collection<? super R>> targetNodeChildrenExtractor,
                                             Consumer<? super R> initTargetChildren,
                                             LinkedList<R> pathNodes,
                                             BiFunction<T, LinkedList<? extends R>, R> mapping) {

        R target = mapping.apply(sourceNode, pathNodes);
        initTargetChildren.accept(target);

        Collection<? extends T> sourceNodeChildren = sourceNodesChildrenExtractor.apply(sourceNode);
        if (CollectionUtils.isEmpty(sourceNodeChildren)) {
            return target;
        }

        pathNodes.addLast(target);
        Collection<? super R> targetNodeChildren = targetNodeChildrenExtractor.apply(target);
        for (T sourceChild : sourceNodeChildren) {
            targetNodeChildren.add(
                    convertRecursion(sourceChild, sourceNodesChildrenExtractor, targetNodeChildrenExtractor, initTargetChildren,
                            pathNodes, mapping)
            );
        }
        pathNodes.removeLast();

        return target;
    }

    public static void main(String[] args) {
//        toTreeTest();
//        flatMapTest();
        convertTest();
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

        TreeSet<Node> tree = TreeUtils.toTree(
                roots,
                Node::getId,
                Node::getParentId,
                (node) -> node.getParentId() == null,
                Node::getChildren, node -> node.setChildren(new ArrayList<>()),
                Comparator.comparing(Node::getId).reversed()
        );
        System.out.println(tree);
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
                ConvertNode::getChildren,
                AnotherConvertNode::getChildren,
                r -> r.setChildren(new ArrayList<>()),
                (node, pathNodes) -> {
                    AnotherConvertNode anotherConvertNode = new AnotherConvertNode();
                    anotherConvertNode.setId(node.getId());
                    anotherConvertNode.setParentId(node.getParentId());

                    List<AnotherConvertNode> temp = new ArrayList<>(pathNodes);
                    temp.add(anotherConvertNode);
                    anotherConvertNode.setPath(temp.stream().map(AnotherConvertNode::getId).collect(Collectors.joining(",")));
                    return anotherConvertNode;
                }, ArrayList::new);

        System.out.println(nodes);
    }
}
