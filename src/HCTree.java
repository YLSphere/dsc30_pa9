/*
 * Name: Yin Lam Lai
 * PID: A15779757
 */

import java.io.*;
import java.util.Stack;
import java.util.PriorityQueue;

/**
 * The Huffman Coding Tree
 */
public class HCTree {
    // alphabet size of extended ASCII
    private static final int NUM_CHARS = 256;
    // number of bits in a bytef
    private static final int BYTE_BITS = 8;

    // the root of HCTree
    private HCNode root;
    // the leaves of HCTree that contain all the symbols
    private HCNode[] leaves = new HCNode[NUM_CHARS];


    /**
     * The Huffman Coding Node
     */
    protected class HCNode implements Comparable<HCNode> {

        byte symbol; // the symbol contained in this HCNode
        int freq; // the frequency of this symbol
        HCNode c0, c1, parent; // c0 is the '0' child, c1 is the '1' child

        /**
         * Initialize a HCNode with given parameters
         *
         * @param symbol the symbol contained in this HCNode
         * @param freq   the frequency of this symbol
         */
        HCNode(byte symbol, int freq) {
            this.symbol = symbol;
            this.freq = freq;
        }

        /**
         * Getter for symbol
         *
         * @return the symbol contained in this HCNode
         */
        byte getSymbol() {
            return this.symbol;
        }

        /**
         * Setter for symbol
         *
         * @param symbol the given symbol
         */
        void setSymbol(byte symbol) {
            this.symbol = symbol;
        }

        /**
         * Getter for freq
         *
         * @return the frequency of this symbol
         */
        int getFreq() {
            return this.freq;
        }

        /**
         * Setter for freq
         *
         * @param freq the given frequency
         */
        void setFreq(int freq) {
            this.freq = freq;
        }

        /**
         * Getter for '0' child of this HCNode
         *
         * @return '0' child of this HCNode
         */
        HCNode getC0() {
            return c0;
        }

        /**
         * Setter for '0' child of this HCNode
         *
         * @param c0 the given '0' child HCNode
         */
        void setC0(HCNode c0) {
            this.c0 = c0;
        }

        /**
         * Getter for '1' child of this HCNode
         *
         * @return '1' child of this HCNode
         */
        HCNode getC1() {
            return c1;
        }

        /**
         * Setter for '1' child of this HCNode
         *
         * @param c1 the given '1' child HCNode
         */
        void setC1(HCNode c1) {
            this.c1 = c1;
        }

        /**
         * Getter for parent of this HCNode
         *
         * @return parent of this HCNode
         */
        HCNode getParent() {
            return parent;
        }

        /**
         * Setter for parent of this HCNode
         *
         * @param parent the given parent HCNode
         */
        void setParent(HCNode parent) {
            this.parent = parent;
        }

        /**
         * Check if the HCNode is leaf (has no children)
         *
         * @return if it's leaf, return true. Otherwise, return false.
         */
        boolean isLeaf() {
            if (this.getC1() == null && this.getC0() == null) {
                return true;
            }
            return false;
        }

        /**
         * String representation
         *
         * @return string representation
         */
        public String toString() {
            return "Symbol: " + this.symbol + "; Freq: " + this.freq;
        }

        /**
         * Compare two nodes
         *
         * @param o node to compare
         * @return int positive if this node is greater
         */

        @Override
        public int compareTo(HCNode o) {
            if (this.getFreq() > o.getFreq()) {
                return 1;
            } else if (this.getFreq() < o.getFreq()) {
                return -1;
            } else {
                if ((this.getSymbol() & 0xff) < (o.getSymbol() & 0xff)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * Returns the root node
     *
     * @return root node
     */
    public HCNode getRoot() {
        return root;
    }

    /**
     * Sets the root node
     *
     * @param root node to set
     */
    public void setRoot(HCNode root) {
        this.root = root;
    }

    /**
     * Builds a Huffman tree from frequency array
     *
     * @param freq frequency array of characters
     */
    public void buildTree(int[] freq) {
        PriorityQueue<HCNode> priorityQueue = new PriorityQueue<>();

        for (int n = 0; n < freq.length; n++) {
            if (freq[n] != 0) {
                byte symbolByte = (byte) n;
                leaves[n] = new HCNode(symbolByte, freq[n]);
                priorityQueue.add(leaves[n]);
            }

        }


        while (priorityQueue.size() > 1) {

            HCNode pop1 = priorityQueue.poll();
            HCNode pop2 = priorityQueue.poll();
            HCNode together = new HCNode(pop1.getSymbol(), pop1.getFreq() + pop2.getFreq());

            together.setC0(pop1);
            together.setC1(pop2);
            pop1.setParent(together);
            pop2.setParent(together);

            setRoot(together);
            priorityQueue.add(together);
        }



    }

    /**
     * Encodes a given symbol in bytes with a known huffman tree
     *
     * @param symbol encoded symbol
     * @param out bit stream output for encoding
     * @throws IOException
     */
    public void encode(byte symbol, BitOutputStream out) throws IOException {
        HCNode starter = leaves[symbol & 0xff];
        Stack<Integer> stack = new Stack<>();
        while (starter != getRoot()) {
            if (starter.getParent().getC0().equals(starter)) {
                stack.push(0);
                starter = starter.getParent();
            } else if (starter.getParent().getC1().equals(starter)) {
                stack.push(1);
                starter = starter.getParent();
            }
        }


        while (!stack.isEmpty()) {
            out.writeBit(stack.pop());
        }


    }

    /**
     * Decodes incoming bit stream into a single symbol in bytes from known huffman tree
     *
     * @param in bit stream input
     * @return Symbol in bytes
     * @throws IOException
     */
    public byte decode(BitInputStream in) throws IOException {
        HCNode start = root;

        while (!start.isLeaf()) {
            int bit = in.readBit();
            if (bit == 1) {
                start = start.getC1();
            } else {
                start = start.getC0();
            }
        }
        return start.getSymbol();
    }

    /**
     * Encodes huffman tree into bit stream output
     *
     * @param node Root of huffman tree
     * @param out Bitstream output
     * @throws IOException
     */
    public void encodeHCTree(HCNode node, BitOutputStream out) throws IOException {
        if (node != null) {
            if (node.isLeaf()) {
                out.writeBit(1);
                System.out.println(node.toString());
                out.writeByte(node.getSymbol());
            } else {
                out.writeBit(0);

            }
            encodeHCTree(node.getC0(), out);
            encodeHCTree(node.getC1(), out);
        }
    }

    /**
     * Decodes huffman tree
     *
     * @param in bitstream input
     * @return
     * @throws IOException
     */
    public HCNode decodeHCTree(BitInputStream in) throws IOException {
        int read = in.readBit();
        if (read == 1) {
            byte byteRead = in.readByte();
            HCNode nodeReturned = new HCNode(byteRead, 1);
            leaves[byteRead & 0xff] = nodeReturned;
            return nodeReturned;

        } else if (read == 0) {
            HCNode parent = new HCNode((byte) 1, 1);
            HCNode child0 = decodeHCTree(in);
            HCNode child1 = decodeHCTree(in);
            child1.setParent(parent);
            child0.setParent(parent);
            parent.setC0(child0);
            parent.setC1(child1);
            setRoot(parent);
            return parent;
        }
        return null;
    }

}
