import java.util.*;

public class WhatsAppAVLTree {

    static class Node {
        long timestamp;
        Node left, right;
        int height;

        Node(long timestamp) {
            this.timestamp = timestamp;
            this.height = 1;
        }
    }

    static class AVLTree {
        Node root;

        int height(Node node) {
            return node == null ? 0 : node.height;
        }

        int getBalance(Node node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }

        Node rotateRight(Node y) {
            Node x = y.left;
            Node temp = x.right;

            x.right = y;
            y.left = temp;

            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;

            return x;
        }

        Node rotateLeft(Node x) {
            Node y = x.right;
            Node temp = y.left;

            y.left = x;
            x.right = temp;

            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;

            return y;
        }

        Node insert(Node node, long timestamp) {
            if (node == null) {
                return new Node(timestamp);
            }

            if (timestamp < node.timestamp) {
                node.left = insert(node.left, timestamp);
            } else if (timestamp > node.timestamp) {
                node.right = insert(node.right, timestamp);
            } else {
                return node;
            }

            node.height = 1 + Math.max(height(node.left), height(node.right));

            int balance = getBalance(node);

            // LL Case
            if (balance > 1 && timestamp < node.left.timestamp) {
                return rotateRight(node);
            }

            // RR Case
            if (balance < -1 && timestamp > node.right.timestamp) {
                return rotateLeft(node);
            }

            // LR Case
            if (balance > 1 && timestamp > node.left.timestamp) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }

            // RL Case
            if (balance < -1 && timestamp < node.right.timestamp) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }

            return node;
        }

        void insert(long timestamp) {
            root = insert(root, timestamp);
        }

        long findOldestPending() {
            Node current = root;

            if (current == null) {
                throw new RuntimeException("Tree is empty");
            }

            while (current.left != null) {
                current = current.left;
            }

            return current.timestamp;
        }

        int countPointerHopsToOldest() {
            Node current = root;
            int hops = 0;

            while (current != null && current.left != null) {
                current = current.left;
                hops++;
            }

            return hops;
        }

        void inOrder(Node node) {
            if (node != null) {
                inOrder(node.left);
                System.out.print(node.timestamp + " ");
                inOrder(node.right);
            }
        }

        void displayTree(Node node, String indent, boolean isRight) {
            if (node != null) {
                displayTree(node.right, indent + "     ", true);

                System.out.println(indent +
                        (isRight ? " /--- " : " \\--- ") +
                        node.timestamp +
                        " (h=" + node.height + ")");

                displayTree(node.left, indent + "     ", false);
            }
        }
    }

    public static void main(String[] args) {

        AVLTree tree = new AVLTree();

        long[] timestamps = {
                1000, 2000, 3000, 4000, 5000,
                6000, 7000, 8000, 9000, 10000
        };

        System.out.println("Inserting WhatsApp delivered receipt timestamps:");
        for (long timestamp : timestamps) {
            System.out.println("Inserted: " + timestamp);
            tree.insert(timestamp);
        }

        System.out.println("\nAVL Tree Structure:");
        tree.displayTree(tree.root, "", true);

        System.out.println("\nIn-order Traversal:");
        tree.inOrder(tree.root);

        System.out.println("\n\nOldest Pending Receipt:");
        System.out.println(tree.findOldestPending());

        int hops = tree.countPointerHopsToOldest();
        System.out.println("\nPointer Hops to Find Oldest Pending:");
        System.out.println(hops);

        int costPerHop = 200;
        int totalTimeNs = hops * costPerHop;

        System.out.println("\nLookup Time:");
        System.out.println(hops + " × 200 ns = " + totalTimeNs + " ns");

        System.out.println("\nTime Complexity:");
        System.out.println("Insertion: O(log n)");
        System.out.println("Find Oldest Pending: O(log n)");
        System.out.println("Search: O(log n)");
        System.out.println("Height: O(log n)");
    }
}
