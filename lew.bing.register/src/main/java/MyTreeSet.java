

public class MyTreeSet<T extends Comparable<T>> {




    public MyTreeSet() {

    }

    private class Tree {
        // 不设root节点,null节点是哨兵
        private Node root;
        private Node nullNode;

        public Tree() {
            nullNode = new Node(Color.BLACK, null);
            root = nullNode;
        }

        public void addNode(T value) {
            // 创造node
            // 新建的node默认为黑色，但要调整
            Node node = new Node(Color.RED, value);
            // node左右都是nil
            node.setrChild(nullNode);
            node.setlChild(nullNode);
            // 寻找合适的插入位置
            Node x = root;
            Node y = nullNode;
            while (x != nullNode) {
                y = x;
                if (node.compareTo(x) < 0) {
                    // 左边遍历
                    x = x.lChild;
                } else {
                    x = x.rChild;
                }
            }
            node.setParent(y);
            if (y == nullNode) {
                root = node;
            } else {
                if (node.compareTo(y) < 0) {
                    y.setlChild(node);
                } else {
                    y.setrChild(node);
                }
            }
            fixInsertTree(node);
        }

        private void fixInsertTree(Node node) {
            // 修复插入的树
            // 有哪些情况呢
            // 第一种情况，node是根节点
            if (node.getParent() == nullNode) {
                node.setColor(Color.BLACK);
                root = node;
                return;
            }
            // 第二种情况，父亲是黑色的不用管，不写

            Node p = node.getParent();
            if (p.getColor() == Color.RED) {
                // 第三种情况，父亲是红色，叔叔也是红色
                Node pp = p.parent;
                // 分两种，父亲是爷爷的左节点或右节点
                Node s;
                if (p == pp.lChild) {
                    s = pp.rChild;
                    if (fixInsertTreeOfColor(p,s,pp)) {
                        // 旋转
                        // 先判断node是p的左节点还是右节点
                        if (p.getlChild() == node) {
                            // 将p的值与pp的值互换
                            T v = p.value;
                            p.value = pp.value;
                            pp.value = v;
                            // 将pp的左节点变成node
                            pp.setlChild(node);
                            // 将p的左节点变成p的右节点
                            p.setlChild(p.getrChild());
                            // 将p的右节点变成pp的右节点
                            p.setrChild(pp.getrChild());
                            // 将pp的右节点变成p
                            pp.setrChild(p);
                            // node的父节点是pp
                            node.setParent(pp);
                        } else {
                            // 先做一次调整成上面的情况
                            // p跟node对调
                            // pp的左节点是node
                            pp.setlChild(node);
                            p.setParent(node);
                            node.setParent(pp);
                            p.setrChild(node.getlChild());
                            node.setlChild(p);
                            // 调整p
                            fixInsertTree(p);
                        }
                    }
                } else {
                    s = pp.lChild;
                    if (fixInsertTreeOfColor(p,s,pp)) {
                        if (p.getrChild() == node) {
                            // 旋转一次就够
                            // 将p的值与pp的值互换
                            T v = p.value;
                            p.value = pp.value;
                            pp.value = v;
                            // 将pp的左节点变成node
                            pp.setrChild(node);
                            // 将p的左节点变成p的右节点
                            p.setrChild(p.getlChild());
                            // 将p的右节点变成pp的右节点
                            p.setlChild(pp.getlChild());
                            // 将pp的右节点变成p
                            pp.setrChild(p);
                            // node的父节点是pp
                            node.setParent(pp);
                        } else {
                            // 先做一次调整成上面的情况
                            // p跟node对调
                            // pp的右节点是node
                            pp.setrChild(node);
                            p.setParent(node);
                            node.setParent(pp);
                            p.setlChild(node.getrChild());
                            node.setrChild(p);
                            // 调整p
                            fixInsertTree(p);
                        }
                    }
                }

            }
        }

        private boolean fixInsertTreeOfColor(Node p, Node s, Node pp) {
            if (s.getColor() == Color.RED) {
                p.setColor(Color.BLACK);
                s.setColor(Color.BLACK);
                pp.setColor(Color.RED);
                fixInsertTree(pp);
                return false;
            } else {
                return true;
            }
        }

    }



    private class Node implements Comparable<Node> {

        private Color color;
        private T value;

        private Node lChild;
        private Node rChild;
        private Node parent;

        public Node(Color color, T value) {
            this.color = color;
            this.value = value;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getlChild() {
            return lChild;
        }

        public void setlChild(Node lChild) {
            this.lChild = lChild;
        }

        public Node getrChild() {
            return rChild;
        }

        public void setrChild(Node rChild) {
            this.rChild = rChild;
        }

        @Override
        public int compareTo(Node o) {
            if (o.getValue() == null) {
                return 1;
            }
            if (this.getValue() == null) {
                return -1;
            }
            return this.getValue().compareTo(o.value);
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    private enum Color {
        RED, BLACK;
    }

}
