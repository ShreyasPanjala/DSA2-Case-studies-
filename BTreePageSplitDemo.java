import java.util.*;

public class BTreePageSplitDemo {

    static class BTreeNode {
        int[] keys;
        int keyCount;
        BTreeNode[] children;
        boolean isLeaf;

        BTreeNode(int order, boolean isLeaf) {
            this.keys = new int[order - 1];
            this.children = new BTreeNode[order];
            this.keyCount = 0;
            this.isLeaf = isLeaf;
        }
    }

    static class BTree {
        private BTreeNode root;
        private final int order;

        BTree(int order) {
            this.order = order;
            this.root = new BTreeNode(order, true);
        }

        public void insert(int key) {
            BTreeNode r = root;

            if (r.keyCount == order - 1) {
                BTreeNode newRoot = new BTreeNode(order, false);
                newRoot.children[0] = root;

                System.out.println("\nROOT FULL: Splitting root...");
                splitChild(newRoot, 0);

                root = newRoot;
                insertNonFull(root, key);
            } else {
                insertNonFull(r, key);
            }
        }

        private void insertNonFull(BTreeNode node, int key) {
            int i = node.keyCount - 1;

            if (node.isLeaf) {
                while (i >= 0 && key < node.keys[i]) {
                    node.keys[i + 1] = node.keys[i];
                    i--;
                }

                node.keys[i + 1] = key;
                node.keyCount++;

            } else {
                while (i >= 0 && key < node.keys[i]) {
                    i--;
                }

                i++;

                if (node.children[i].keyCount == order - 1) {
                    System.out.println("\nCHILD FULL: Splitting child before inserting " + key);
                    splitChild(node, i);

                    if (key > node.keys[i]) {
                        i++;
                    }
                }

                insertNonFull(node.children[i], key);
            }
        }

        private void splitChild(BTreeNode parent, int index) {
            BTreeNode fullChild = parent.children[index];

            int midIndex = (order - 1) / 2;
            int midKey = fullChild.keys[midIndex];

            BTreeNode rightNode = new BTreeNode(order, fullChild.isLeaf);

            rightNode.keyCount = fullChild.keyCount - midIndex - 1;

            for (int j = 0; j < rightNode.keyCount; j++) {
                rightNode.keys[j] = fullChild.keys[midIndex + 1 + j];
            }

            if (!fullChild.isLeaf) {
                for (int j = 0; j <= rightNode.keyCount; j++) {
                    rightNode.children[j] = fullChild.children[midIndex + 1 + j];
                }
            }

            fullChild.keyCount = midIndex;

            for (int j = parent.keyCount; j >= index + 1; j--) {
                parent.children[j + 1] = parent.children[j];
            }

            parent.children[index + 1] = rightNode;

            for (int j = parent.keyCount - 1; j >= index; j--) {
                parent.keys[j + 1] = parent.keys[j];
            }

            parent.keys[index] = midKey;
            parent.keyCount++;

            System.out.println("Page Split Done:");
            System.out.println("Promoted Key: " + midKey);
            System.out.println("Left Page: " + nodeKeys(fullChild));
            System.out.println("Right Page: " + nodeKeys(rightNode));
        }

        private String nodeKeys(BTreeNode node) {
            StringBuilder sb = new StringBuilder("[ ");
            for (int i = 0; i < node.keyCount; i++) {
                sb.append(node.keys[i]).append(" ");
            }
            sb.append("]");
            return sb.toString();
        }

        public void printTree() {
            System.out.println("\nB-Tree Structure:");
            printTree(root, "", true);
        }

        private void printTree(BTreeNode node, String indent, boolean isLast) {
            if (node == null) return;

            System.out.print(indent);

            if (isLast) {
                System.out.print("└── ");
                indent += "    ";
            } else {
                System.out.print("├── ");
                indent += "│   ";
            }

            System.out.println(nodeKeys(node));

            for (int i = 0; i <= node.keyCount; i++) {
                if (node.children[i] != null) {
                    printTree(node.children[i], indent, i == node.keyCount);
                }
            }
        }
    }

    public static void main(String[] args) {

        /*
         PostgreSQL case:
         Actual order m = 128.
         That means one page can hold 127 keys.

         For easy visible output, this demo uses order = 4.
         So each node can hold only 3 keys.
         This makes page splits clearly visible in text form.
        */

        BTree tree = new BTree(4);

        int[] keys = {
                10, 20, 30, 40, 50, 60, 70, 80, 90, 100
        };

        System.out.println("PostgreSQL B-Tree Page Split Simulation");
        System.out.println("--------------------------------------");

        for (int key : keys) {
            System.out.println("\nInserting key: " + key);
            tree.insert(key);
            tree.printTree();
        }

        System.out.println("\nFinal Explanation:");
        System.out.println("1. When a leaf page becomes full, it is split into two pages.");
        System.out.println("2. The middle key is promoted to the parent node.");
        System.out.println("3. If the parent is also full, the split propagates upward.");
        System.out.println("4. If the root splits, tree height increases by 1.");

        System.out.println("\nTime Complexity:");
        System.out.println("Search: O(log n)");
        System.out.println("Insert: O(log n)");
        System.out.println("Page Split Cost: O(m)");
        System.out.println("Worst-case cascading split: O(height)");

        System.out.println("\nDisk I/O Case:");
        System.out.println("Current height = 5");
        System.out.println("Top 3 levels cached");
        System.out.println("Disk levels accessed = 5 - 3 = 2");
        System.out.println("Single page split writes 2 pages and updates parent.");
        System.out.println("Worst case split can propagate to root.");
    }
}