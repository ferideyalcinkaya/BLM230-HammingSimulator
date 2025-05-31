package mimari;

import java.util.Arrays;

public class HammingCode {
    private final int m;
    private final int r;
    private final int n;

    public HammingCode(int dataBits) {
        if (dataBits < 1) {
            throw new IllegalArgumentException("dataBits en az 1 olmalı; şu an: " + dataBits);
        }
        this.m = dataBits;
        this.r = calcParityBits(m);
        this.n = m + r + 1;
    }

    private int calcParityBits(int m) {
        int r = 0;
        while ((1L << r) < m + r + 1) {
            r++;
        }
        return r;
    }

    private boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    public int[] encode(int[] data) {
        if (data == null || data.length != m) {
            throw new IllegalArgumentException("encode: data.length = " +
                    (data == null ? "null" : data.length) +
                    ", ancak beklenen m = " + m);
        }

        int[] code = new int[n];
        for (int di = 0, i = 0; i < n - 1; i++) {
            if (isPowerOfTwo(i + 1)) continue;
            code[i] = data[di++];
        }

        for (int p = 0; p < r; p++) {
            int pPos = (1 << p) - 1;
            int count = 0;
            for (int i = pPos; i < n - 1; i += (pPos + 1) * 2) {
                for (int j = 0; j <= pPos && i + j < n - 1; j++) {
                    if (code[i + j] == 1) count++;
                }
            }
            code[pPos] = count % 2;
        }

        int sum = 0;
        for (int i = 0; i < n - 1; i++) {
            sum += code[i];
        }
        code[n - 1] = sum % 2;

        return code;
    }

    public int getErrorPosition(int[] received) {
        if (received == null || received.length != n) {
            throw new IllegalArgumentException("getErrorPosition: received.length = " +
                    (received == null ? "null" : received.length) +
                    ", ancak beklenen n = " + n);
        }

        int syndrome = 0;
        for (int p = 0; p < r; p++) {
            int pPos = (1 << p) - 1;
            int count = 0;
            for (int i = pPos; i < n - 1; i += (pPos + 1) * 2) {
                for (int j = 0; j <= pPos && i + j < n - 1; j++) {
                    if (received[i + j] == 1) count++;
                }
            }
            if (count % 2 != 0) {
                syndrome |= (1 << p);
            }
        }
        return syndrome;
    }

    public int[] decode(int[] received) {
        if (received == null || received.length != n) {
            throw new IllegalArgumentException("decode: received.length = " +
                    (received == null ? "null" : received.length) +
                    ", ancak beklenen n = " + n);
        }

        int syndrome = getErrorPosition(received);

        int sum = 0;
        for (int i = 0; i < n - 1; i++) {
            sum += received[i];
        }
        boolean overallError = (sum % 2) != received[n - 1];

        if (syndrome != 0 && !overallError) {
            return null;
        }

        if (syndrome > 0 && syndrome <= n && overallError) {
            received[syndrome - 1] ^= 1;
        }

        int[] dataOut = new int[m];
        for (int di = 0, i = 0; i < n - 1; i++) {
            if (isPowerOfTwo(i + 1)) continue;
            dataOut[di++] = received[i];
        }
        return dataOut;
    }

    public boolean isDoubleError(int[] received) {
        if (received == null || received.length != n) {
            throw new IllegalArgumentException("isDoubleError: received.length = " +
                    (received == null ? "null" : received.length) +
                    ", ancak beklenen n = " + n);
        }

        int syndrome = getErrorPosition(received);

        int sum = 0;
        for (int i = 0; i < n - 1; i++) {
            sum += received[i];
        }
        boolean overallError = (sum % 2) != received[n - 1];

        return (syndrome != 0 && !overallError);
    }

    public int getDataBitCount() {
        return m;
    }

    public int getParityBitCount() {
        return r;
    }

    public int getTotalCodeLength() {
        return n;
    }

    public static void main(String[] args) {
        int[] data = {1, 0, 1, 1, 0, 0, 1, 0};
        HammingCode hc = new HammingCode(data.length);
        int[] encoded = hc.encode(data);

        encoded[2] ^= 1;
        int errorPos = hc.getErrorPosition(encoded);
        boolean doubleErr = hc.isDoubleError(encoded);
        int[] decoded = hc.decode(encoded);

        System.out.println("Original data: " + Arrays.toString(data));
        System.out.println("Encoded: " + Arrays.toString(encoded));
        System.out.println("Detected error position: " + errorPos);
        System.out.println("Is double error? " + doubleErr);
        System.out.println("Decoded data: " + Arrays.toString(decoded));
    }
}
