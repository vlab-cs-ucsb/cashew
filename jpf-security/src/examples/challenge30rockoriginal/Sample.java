package challenge30rockoriginal;
/*
 * Decompiled with CFR 0_110.
 */
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;

public class Sample {
    private SSSS mzSSSS = new SSSS("ZERO");
    private SSSS ewSSSS = new SSSS("ONE");
    private SSSS cvb;
    private Hashtable<String, Integer> lut = new Hashtable();
    private LinkedList<Object> post = new LinkedList();
    private Hashtable<String, HashSet<SSSS>> mSSSSTable = new Hashtable();

    public static void main(String[] args) {
    	long t = System.currentTimeMillis();
        int i;
        String ccc = "(0 | 1) & (2 | 3) & (4 | 5) & (6 | 7) & (8 | 9) & (10 | 11) & (12 | 13) & (14 | 15) & (16 | 17) & (18 | 19) & (20 | 21) & (22 | 23) & (24 | 25) & (26 | 27) & (28 | 29)";
        Scanner scanner = new Scanner(System.in);
        String[] ooo = new String[30];
        for (i = 0; scanner.hasNext() && i < 30; ++i) {
            ooo[i] = scanner.next();
        }
        while (i < 30) {
            ooo[i] = "0";
            ++i;
        }
        Sample cbmn = new Sample(ccc, ooo);
        
        if (cbmn.cvb != null)
        	System.out.println("cvb is not null");
//        System.out.println("cvb: ");
//        cbmn.printSample(cbmn.cvb,0);

//        System.out.println("mzSSSS: ");
//        cbmn.printSample(cbmn.mzSSSS,0);
//
//        System.out.println("ewSSSS: ");
//        cbmn.printSample(cbmn.ewSSSS,0);
//
//        System.out.println("lut: ");
//        System.out.println(cbmn.lut);
//        
//        System.out.println("post: ");
//        System.out.println(cbmn.post);
//        
//        System.out.println("mSSSSTable: ");
//        System.out.println(cbmn.mSSSSTable);
        
        
        long elapsed = System.currentTimeMillis() - t;
        System.out.println("Elapsed time: " + elapsed + " milliseconds.");
    }

    public Sample(String something, String[] aother) {
        for (int i = 0; i < aother.length; ++i) {
            this.lut.put(aother[i], i);
        }
        this.lut.put("ZERO", Integer.MAX_VALUE);
        this.lut.put("ONE", Integer.MAX_VALUE);
        this.parseSomething(something);
        this.cvb = this.constructSample();
    }

    private int getSomething(String op) {
        switch (op) {
            case "~": {
                return 0;
            }
            case "&": {
                return 1;
            }
            case "^": {
                return 2;
            }
            case "|": {
                return 3;
            }
            case ">": {
                return 4;
            }
        }
        return 5;
    }

    private void parseSomething(String something) {
        Stack<String> opStack = new Stack<String>();
        String regex = "((?<=%1$s)|(?=%1$s))";
        String delimiters = "[~&^|>()]";
        for (String element : something.split(String.format(regex, delimiters))) {
            String op;
            String str = element.trim();
            if (str.isEmpty()) continue;
            if (str.equals("(")) {
                opStack.push(str);
                continue;
            }
            if (str.equals(")")) {
                String string = op = opStack.empty() ? "(" : (String)opStack.pop();
                while (!op.equals("(")) {
                    this.post.add(op);
                    op = (String)opStack.pop();
                }
                continue;
            }
            if (str.equals("~") || str.equals("&") || str.equals("^") || str.equals("|") || str.equals(">")) {
                while (!opStack.empty() && this.getSomething((String)opStack.peek()) <= this.getSomething(str)) {
                    op = (String)opStack.pop();
                    this.post.add(op);
                }
                opStack.push(str);
                continue;
            }
            SSSS varSSSS = this.buildSSSS(str, this.mzSSSS, this.ewSSSS);
            this.post.add(varSSSS);
        }
        while (!opStack.empty()) {
            String op = (String)opStack.pop();
            this.post.add(op);
        }
    }

    private SSSS constructSample() {
        ListIterator<Object> iter = this.post.listIterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof SSSS) continue;
            String op = (String)obj;
            System.out.println("Evaluating: " + op);
            int operandNum = op.equals("~") ? 1 : 2;
            iter.remove();
            SSSS result = null;
            SSSS[] operand = new SSSS[operandNum];
            for (int i = 0; i < operandNum; ++i) {
                Object prev;
                Object object = prev = iter.hasPrevious() ? iter.previous() : null;
                if (prev == null || !(prev instanceof SSSS)) {
                    return null;
                }
                operand[i] = (SSSS)prev;
                iter.remove();
            }
            result = operandNum == 1 ? this.operateSample(op, operand[0], this.mzSSSS) : this.operateSample(op, operand[1], operand[0]);
            iter.add(result);
        }
        Object root = this.post.removeFirst();
        if (!this.post.isEmpty() || !(root instanceof SSSS)) {
            return null;
        }
        return (SSSS)root;
    }

    private SSSS evaluateSomething(String op, boolean x, boolean y) {
        boolean val = false;
        switch (op) {
            case "~": {
                val = !x;
                break;
            }
            case "&": {
                val = x & y;
                break;
            }
            case "^": {
                val = x ^ y;
                break;
            }
            case "|": {
                val = x | y;
                break;
            }
            case ">": {
                val = !x | y;
            }
        }
        if (val) {
            return this.ewSSSS;
        }
        return this.mzSSSS;
    }

    private SSSS buildSSSS(String varkkk, SSSS fPPP, SSSS tPPP) {
        HashSet<SSSS> lllSet;
        if (fPPP == tPPP) {
            return fPPP;
        }
        if (!this.mSSSSTable.containsKey(varkkk)) {
            lllSet = new HashSet<SSSS>();
            this.mSSSSTable.put(varkkk, lllSet);
        }
        lllSet = this.mSSSSTable.get(varkkk);
        for (SSSS lll : lllSet) {
            if (!lll.hasSamePPPren(fPPP, tPPP)) continue;
            return lll;
        }
        SSSS lll2 = new SSSS(varkkk);
        lll2.setFAPPP(fPPP);
        lll2.setTRPPP(tPPP);
        lllSet.add(lll2);
        return lll2;
    }

    private SSSS operateSample(String op, SSSS lll1, SSSS lll2) {
        if (lll1 == null || lll2 == null) {
            return null;
        }
        String varkkk1 = lll1.getGGGkkk();
        SSSS fPPP1 = lll1.getFAPPP();
        SSSS tPPP1 = lll1.getTRPPP();
        String varkkk2 = lll2.getGGGkkk();
        SSSS fPPP2 = lll2.getFAPPP();
        SSSS tPPP2 = lll2.getTRPPP();
        Integer id1 = this.lut.get(varkkk1);
        Integer id2 = this.lut.get(varkkk2);
        if (id1 == null || id2 == null) {
            return null;
        }
        int idInt1 = id1;
        int idInt2 = id2;
        if (idInt1 == Integer.MAX_VALUE && idInt2 == Integer.MAX_VALUE) {
            boolean b1 = lll1 != this.mzSSSS;
            boolean b2 = lll2 != this.mzSSSS;
            return this.evaluateSomething(op, b1, b2);
        }
        if (idInt1 == idInt2) {
            SSSS fPPP = this.operateSample(op, fPPP1, fPPP2);
            SSSS tPPP = this.operateSample(op, tPPP1, tPPP2);
            return this.buildSSSS(varkkk1, fPPP, tPPP);
        }
        if (id1 < id2) {
            SSSS fPPP = this.operateSample(op, fPPP1, lll2);
            SSSS tPPP = this.operateSample(op, tPPP1, lll2);
            return this.buildSSSS(varkkk1, fPPP, tPPP);
        }
        SSSS fPPP = this.operateSample(op, lll1, fPPP2);
        SSSS tPPP = this.operateSample(op, lll1, tPPP2);
        return this.buildSSSS(varkkk2, fPPP, tPPP);
    }

    private void printSample(SSSS lll, int indent) {
        int i;
        if (lll == null) {
            return;
        }
        for (i = 0; i < indent; ++i) {
            System.out.print('\t');
        }
        System.out.print("GGG kkk: " + lll.getGGGkkk());
        System.out.println("\tSSSS Number: " + Integer.toString(lll.getSSSSNumber()));
        for (i = 0; i < indent + 1; ++i) {
            System.out.print('\t');
        }
        System.out.println("Zero Branch: ");
        this.printSample(lll.getFAPPP(), indent + 1);
        for (i = 0; i < indent + 1; ++i) {
            System.out.print('\t');
        }
        System.out.println("One Branch: ");
        this.printSample(lll.getTRPPP(), indent + 1);
    }
}

