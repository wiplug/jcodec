package org.jcodec.common.io;

import static junit.framework.Assert.assertTrue;
import gnu.trove.list.array.TIntArrayList;

import java.io.IOException;
import java.io.PrintStream;

import junit.framework.Assert;

/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 * 
 * Table-based prefix VLC reader
 * 
 * @author The JCodec project
 * 
 */
public class VLC {

    private int[] codes;
    private int[] codeSizes;

    private int[] values;
    private int[] valueSizes;

    public VLC(int[] codes, int[] codeSizes) {
        this.codes = codes;
        this.codeSizes = codeSizes;

        invert();
    }

    public VLC(String... codes) {
        TIntArrayList _codes = new TIntArrayList();
        TIntArrayList _codeSizes = new TIntArrayList();
        for (String string : codes) {
            _codes.add(Integer.parseInt(string, 2) << (32 - string.length()));
            _codeSizes.add(string.length());
        }
        this.codes = _codes.toArray();
        this.codeSizes = _codeSizes.toArray();

        invert();
    }

    private void invert() {
        TIntArrayList values = new TIntArrayList();
        TIntArrayList valueSizes = new TIntArrayList();
        invert(0, 0, 0, values, valueSizes);
        this.values = values.toArray();
        this.valueSizes = valueSizes.toArray();
    }

    private int invert(int startOff, int level, int prefix, TIntArrayList values, TIntArrayList valueSizes) {

        int tableEnd = startOff + 256;
        values.fill(startOff, tableEnd, -1);
        valueSizes.fill(startOff, tableEnd, 0);

        int prefLen = level << 3;
        for (int i = 0; i < codeSizes.length; i++) {
            if ((codeSizes[i] <= prefLen) || (level > 0 && (codes[i] >>> (32 - prefLen)) != prefix))
                continue;

            int pref = codes[i] >>> (32 - prefLen - 8);
            int code = pref & 0xff;
            int len = codeSizes[i] - prefLen;
            if (len <= 8) {
                for (int k = 0; k < (1 << (8 - len)); k++) {
                    values.set(startOff + code + k, i);
                    valueSizes.set(startOff + code + k, len);
                }
            } else {
                if (values.get(startOff + code) == -1) {
                    values.set(startOff + code, tableEnd);
                    tableEnd = invert(tableEnd, level + 1, pref, values, valueSizes);
                }
            }
        }

        return tableEnd;
    }

    public int readVLC(InBits in) throws IOException {

        int code = 0, len = 0, overall = 0;
        for (int i = 0; len == 0; i++) {
            int string = in.checkNBit(8);
            int ind = string + code;
            code = values[ind];
            len = valueSizes[ind];

            int bits = len != 0 ? len : 8;
            overall = (overall << bits) | string;
            in.skip(bits);

            if (code == -1)
                throw new RuntimeException("Invalid code prefix " + binary(overall, (i << 3) + bits));
        }

        return code;
    }

    private String binary(int string, int len) {
        char[] symb = new char[len];
        for (int i = 0; i < len; i++) {
            symb[i] = (string & (1 << (len - i - 1))) != 0 ? '1' : '0';
        }
        return new String(symb);
    }

    public void writeVLC(OutBits out, int code) throws IOException {
        out.writeNBit(codes[code] >>> (32 - codeSizes[code]), codeSizes[code]);
    }

    public void printTable(PrintStream ps) {
        for (int i = 0; i < values.length; i++) {
            ps.println(i + ": " + extracted(i) + " (" + valueSizes[i] + ") -> " + values[i]);

        }
    }

    private String extracted(int num) {

        String str = Integer.toString(num & 0xff, 2);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8 - str.length(); i++)
            builder.append('0');
        builder.append(str);
        return builder.toString();
    }
}
