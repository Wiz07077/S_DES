package S_DES;


public class functions {
    //辅助函数
    static int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    static int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
    
    static int[] LeftShift1 = { 2, 3, 4, 5, 1 };
    static int[] LeftShift2 = { 3, 4, 5, 1, 2 };

    static int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };
    static int[] IP_1 = { 4, 1, 3, 5, 7, 2, 8, 6 };

    static int[] FEPBox = { 4, 1, 2, 3, 2, 3, 4, 1 };

    static int[][] SBox1 = { {1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2} };
    static int[][] SBox2 = { {0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3} };

    static int[] SPBox = { 2, 4, 3, 1 };

    //置换函数
    public static String permute(String input, int[] permuteTable, int n) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < n; i++) {
            output.append(input.charAt(permuteTable[i] - 1));
        }
        return output.toString();
    }

    //左移函数
    public static String leftShift(String key, int[] shiftTable) {
        StringBuilder shifted = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            shifted.append(key.charAt(shiftTable[i] - 1));
        }
        return shifted.toString();
    }

    //初始密钥生成
    public static String[] generateKeys(String key) {
        key = permute(key, P10, 10); //P10置换
        String left = key.substring(0, 5);
        String right = key.substring(5, 10);

        left = leftShift(left, LeftShift1);
        right = leftShift(right, LeftShift1);

        String combined = left + right;
        String K1 = permute(combined, P8, 8);   //P8置换

        left = leftShift(left, LeftShift2);
        right = leftShift(right, LeftShift2);

        combined = left + right;
        String K2 = permute(combined, P8, 8);

        return new String[] { K1, K2 };
    }


    public static String sbox(String input, int[][] SBox) {
        int row = (input.charAt(0) - '0') * 2 + (input.charAt(3) - '0');
        int col = (input.charAt(1) - '0') * 2 + (input.charAt(2) - '0');
        int value = SBox[row][col];
        return String.format("%2s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    //轮函数
    public static String F(String right, String subkey) {
        String expandedRight = permute(right, FEPBox, 8);
        long expandedRightBits = Long.parseLong(expandedRight, 2);
        long subkeyBits = Long.parseLong(subkey, 2);
        long xorResult = expandedRightBits ^ subkeyBits;

        String xorResultStr = String.format("%8s", Long.toBinaryString(xorResult)).replace(' ', '0');
        String leftXor = xorResultStr.substring(0, 4);
        String rightXor = xorResultStr.substring(4, 8);
        String sboxOutput = sbox(leftXor, SBox1) + sbox(rightXor, SBox2);

        return permute(sboxOutput, SPBox, 4);
    }

    //加密函数
    public static String encrypt(String plaintext, String[] keys) {
        plaintext = permute(plaintext, IP, 8);

        String left = plaintext.substring(0, 4);
        String right = plaintext.substring(4, 8);

        String fOutput = F(right, keys[0]);
        long leftBits = Long.parseLong(left, 2);
        long fOutputBits = Long.parseLong(fOutput, 2);
        long xorResult = leftBits ^ fOutputBits;

        left = right;
        right = String.format("%4s", Long.toBinaryString(xorResult)).replace(' ', '0');

        fOutput = F(right, keys[1]);
        leftBits = Long.parseLong(left, 2);
        fOutputBits = Long.parseLong(fOutput, 2);
        xorResult = leftBits ^ fOutputBits;

        String combined = String.format("%4s", Long.toBinaryString(xorResult)).replace(' ', '0') + right;
        return permute(combined, IP_1, 8);
    }


    //解密函数
    public static String decrypt(String ciphertext, String[] keys) {
        return encrypt(ciphertext, new String[] { keys[1], keys[0] });
    }
}
