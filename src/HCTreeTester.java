import org.junit.Assert.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;



public class HCTreeTester {
    HCTree test;
    int[] freq = new int[256];

    @Before
    public void setup() {
        test = new HCTree();
        freq[10] = 1;
        freq[97] = 17;
        freq[98] = 8;
        freq[99] = 7;
        freq[100] = 14;
        freq[101] = 9;
        freq[102] = 1;
        test.buildTree(freq);
    }
    public void inorderTraversal(HCTree.HCNode node) {
        if (node != null) {
            inorderTraversal(node.getC0());

            System.out.println(node.toString());
            inorderTraversal(node.getC1());
        }

    }


    @org.junit.Test
    public void buildTree() {
        inorderTraversal(test.getRoot());
    }


}