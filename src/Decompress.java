/*
 * Name: TODO
 * PID: TODO
 */

import java.io.*;

/**
 * Decompress the first given file to the second given file using Huffman coding
 * 
 * @author Yin Lam Lai
 * @since A15779757
 */
public class Decompress {
    private static final int EXP_ARG = 2; // number of expected arguments

    public static void main(String[] args) throws IOException {

        // Check if the number of arguments is correct
        if (args.length != EXP_ARG) {
            System.out.println("Invalid number of arguments.\n" +
            "Usage: ./decompress <infile outfile>.\n");
            return;
        }

        FileInputStream inFile = new FileInputStream(args[0]);
        DataInputStream in = new DataInputStream(inFile);
        BitInputStream bitIn = new BitInputStream(in);

        FileOutputStream outFile = new FileOutputStream(args[1]);
        DataOutputStream out = new DataOutputStream(outFile);

        int byteCount = in.readInt();
        HCTree tree = new HCTree();
        tree.decodeHCTree(bitIn);

        for (int n = 0; n < byteCount; n++) {
            out.writeByte(tree.decode(bitIn));
        }


        inFile.close();
        in.close();
        outFile.close();
        out.close();
        return;
    }
}
