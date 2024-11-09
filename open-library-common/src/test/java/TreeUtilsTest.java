import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cqq.openlibrary.common.util.TreeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Qingquan
 */
public class TreeUtilsTest {
    
    public static void main(String[] args) {
//            toTreeTest();
//            flatMapTest();
//            convertTest();
//            searchParentNodeTest();
        searchParentNodeTest2();
//            searchNodeTest();
//            foreachTest();
    }
    
    @Data
    @Accessors(chain = true)
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
        children.add(new Node("A11", "A11", null));
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
                // Comparator.comparing(Node::getId).reversed()
                Comparator.comparing(Node::getId)
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
                new SearchParentNode("A12", "A1", null),
                new SearchParentNode("B1", "B", null)
        );
        
        // 1. 所有缺失的父节点
        Collection<SearchParentNode> searchParentNodes = TreeUtils.searchParentNodes(
                nodes,
                childNodes,
                SearchParentNode::getId,
                SearchParentNode::getParentId,
                node -> node.getParentId() == null,
                ArrayList::new,
                (pathNodeMap, node) -> {
                    throw new RuntimeException(String.format("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet())));
                }
        );
        
        // 2. 汇总
        childNodes = new ArrayList<>(childNodes);
        childNodes.addAll(searchParentNodes);
        
        // 3. 转为树
        TreeSet<SearchParentNode> tree = TreeUtils.toTree(
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
    
    private static void searchParentNodeTest2() {
        // 组装数据
        SearchParentNode a = new SearchParentNode("A", null, null);
        
        SearchParentNode a1 = new SearchParentNode("A1", "A2", null);
        SearchParentNode a2 = new SearchParentNode("A2", "A1", null);
        
        SearchParentNode a11 = new SearchParentNode("A11", "A1", null);
        SearchParentNode a12 = new SearchParentNode("A21", "A2", null);
        
        LinkedList<SearchParentNode> nodes = new LinkedList<>();
        Collections.addAll(nodes, a, a1, a2);
        
        List<SearchParentNode> childNodes = new ArrayList<>();
        Collections.addAll(childNodes, a11, a12);
        
        // 1. 所有缺失的父节点
        Collection<SearchParentNode> searchParentNodes = TreeUtils.searchParentNodes(
                nodes,
                childNodes,
                SearchParentNode::getId,
                SearchParentNode::getParentId,
                node -> node.getParentId() == null,
                ArrayList::new,
                (pathNodeMap, node) -> {
                    throw new RuntimeException(String.format("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet())));
                }
        );
        
        System.out.println(searchParentNodes);
    }
    
    private static void searchNodeTest() {
        // 组装数据
        LinkedList<Node> tree = new LinkedList<>();
        Node a = new Node("A", null, null);
        
        Node a1 = new Node("A1", "A", null);
        Node a2 = new Node("A2", "A", null);
        
        Node a11 = new Node("A11", "A1", null);
        Node a12 = new Node("A12", "A1", null);
        
        a.children = List.of(a1, a2);
        a1.children = List.of(a11, a12);
        
        // 虽然与父级的 A12 主键相同，但是子节点集合中的各个节点与父节点是不同的，二者不是严格意义上的相同节点。
        // a12.children = List.of(new Node("A12", "A1", null));
        
        // 这种方式，才是严格意义的循环引用!
        // a12.children = List.of(a12);
        a12.children = List.of(a1);
        
        tree.add(a);
        
        //            Optional<Node> res = searchNode(tree, Node::getId, Node::getChildren, (nodes, node) -> {
        //                System.out.printf(
        //                        "Node [%s] Path nodes: [%s]%n",
        //                        node.getId(),
        //                        String.join("-", nodes.keySet())
        //                );
        //                return "A2".equals(node.getId());
        //            }, (pathNodeMap, node) -> {
        //                throw new RuntimeException(String.format("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet())));
        //            });
        //            System.out.println(res.orElse(null));
        
        List<Node> resList = TreeUtils.searchAllNode(tree, Node::getId, Node::getChildren, (nodes, node) -> {
            System.out.printf(
                    "Node [%s] Path nodes: [%s]%n",
                    node.getId(),
                    String.join("-", nodes.keySet())
            );
            return "A2".equals(node.getId());
        }, (pathNodeMap, node) -> {
            throw new RuntimeException(String.format("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet())));
        });
        System.out.println(resList);
    }
    
    private static void foreachTest() {
        // 组装数据
        LinkedList<Node> tree = new LinkedList<>();
        Node a = new Node("A", null, null);
        
        Node a1 = new Node("A1", "A", null);
        Node a2 = new Node("A2", "A", null);
        
        Node a11 = new Node("A11", "A1", null);
        Node a12 = new Node("A12", "A1", null);
        
        a.children = List.of(a1, a2);
        a1.children = List.of(a11, a12);
        
        // 虽然与父级的 A12 主键相同，但是子节点集合中的各个节点与父节点是不同的，二者不是严格意义上的相同节点。
        // a12.children = List.of(new Node("A12", "A1", null));
        
        // 这种方式，才是严格意义的循环引用!
        // a12.children = List.of(a12);
        a12.children = List.of(a1);
        
        tree.add(a);
        
        System.out.println("======================= foreach =======================");
        
        TreeUtils.foreach(tree, Node::getId, Node::getChildren, (nodes, node) -> {
            System.out.printf(
                    "Node [%s] Path nodes: [%s]%n",
                    node.getId(),
                    String.join("-", nodes.keySet())
            );
        }, (pathNodeMap, node) -> {
            throw new RuntimeException(String.format("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet())));
        });
        
        System.out.println("======================= breakableForeach =======================");
        
        TreeUtils.breakableForeach(tree, Node::getId, Node::getChildren, (nodes, node) -> {
            System.out.printf(
                    "Node [%s] Path nodes: [%s]%n",
                    node.getId(),
                    String.join("-", nodes.keySet())
            );
            return "A11".equals(node.getId());
        }, (pathNodeMap, node) -> {
            System.out.printf("Node [%s] Path nodes: [%s]%n", node.getId(), String.join("-", pathNodeMap.keySet()));
        });
    }
}

