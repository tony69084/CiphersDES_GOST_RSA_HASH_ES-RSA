import java.io.UnsupportedEncodingException;
import java.util.Scanner;
public class GOST {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner in = new Scanner(System.in);
        System.out.println("Задание 2. Алгоритм шифрования ГОСТ28147-89.\nВведите сообщение, которое необходимо зашифровать: ");
        String input = in.nextLine();
        System.out.println("Введите подключ (4 буквы): ");
        String X0 = in.nextLine();
        String L0 = input.substring(0, 4);
        String R0 = input.substring(4, 8);
        String BinaryL0 = ToBinary(L0);
        String BinaryR0 = ToBinary(R0);
        String BinaryX0 = ToBinary(X0);
        System.out.println("\nПеревод исходного сообщения в двоичный код:\n" + L0 + ": " + BinaryL0 + "\n" + R0 + ": " + BinaryR0);
        System.out.println("\nПеревод подключа в двоичный код:\n" + X0 + ": " + BinaryX0);
        String[] Summamod232 = mod232(BinaryR0, BinaryX0).split(" ");
        System.out.println("Сложение R0 и X0 по mod 2^32: " + mod232(BinaryR0, BinaryX0));
        String pot = tablica(Summamod232);
        System.out.println("Результат сложения R0 и X0 по mod 2^32 после преобразования в блоке подстановки: " + top(pot));
        String sdvigR0 = sdvig(pot);
        System.out.println("Значение функции f(R0,X0) после циклического сдвига: " + top(sdvigR0));
        String xor1 = xor(sdvigR0,BinaryL0);
        System.out.println("Результат сложения f(R0,X0) и L0 по mod 2: " + top(xor1));
        System.out.println("Ответ: ");
        System.out.println("L1=R0: " + top(BinaryR0.replaceAll("\\s+","")));
        System.out.println("R1: " + top(xor1));
    }

    public static String ToBinary(String NotBinary) throws UnsupportedEncodingException {
        byte[] ToBinary = NotBinary.getBytes("windows-1251");
        StringBuilder binary = new StringBuilder();
        for (byte b : ToBinary) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    public static String mod232(String BinaryR0, String BinaryX0) {
        String delimeter = "\\ ";
        String result = "";
        String subStr1 = String.join(" ", BinaryR0);
        String subStr2 = String.join(" ", BinaryX0);
        subStr1 = subStr1.replaceAll("\\s+", "");
        subStr2 = subStr2.replaceAll("\\s+", "");
        int[] sum = new int[subStr1.length()];
        int[] sum2 = new int[subStr1.length()];
        int b = 0;
        for (int d = subStr1.length() - 1; d >= 0; d--) {
            int c1 = Integer.parseInt(String.valueOf(subStr1.charAt(d))) + b;
            int c2 = Integer.parseInt(String.valueOf(subStr2.charAt(d)));
            b = 0;
            sum[d] = c1 + c2;
            if (sum[d] == 0) {
                sum2[d] = 0;
                continue;
            }
            if (sum[d] == 1) {
                sum2[d] = 1;
                continue;
            }
            if (sum[d] == 2) {
                sum2[d] = 0;
                b += 1;
                continue;
            }
            if (sum[d] == 3) {
                sum2[d] = 1;
                b += 1;
                continue;
            }
        }
        for (int i = 0; i < sum2.length; i++) {
            result += Integer.toString(sum2[i]);
            if ((i + 1) % 4 == 0) {
                result += " ";
            }
        }
        return result;
    }

    public static String tablica(String[] Summamod232) {
        String result = "";
        String result1 = "";
        int[] p = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] d = new int[Summamod232.length];
        int[] res = new int[Summamod232.length];
        for (int i = 0; i < Summamod232.length; i++) {
            d[i] = Integer.parseInt(Summamod232[i], 2);
        }
        int[][] table = {
                {1, 13, 4, 6, 7, 5, 14, 4},
                {15, 11, 11, 12, 13, 8, 11, 10},
                {13, 4, 10, 7, 10, 4, 4, 9},
                {0, 1, 0, 1, 1, 13, 12, 2},
                {5, 3, 7, 5, 0, 10, 6, 13},
                {7, 15, 2, 15, 8, 3, 13, 8},
                {10, 5, 1, 13, 9, 4, 15, 0},
                {4, 9, 13, 8, 15, 2, 10, 14},
                {9, 0, 3, 4, 14, 14, 2, 6},
                {2, 10, 6, 10, 4, 15, 3, 11},
                {3, 14, 8, 9, 6, 12, 8, 1},
                {14, 7, 5, 14, 12, 7, 1, 12},
                {6, 6, 9, 0, 11, 6, 0, 7},
                {11, 8, 12, 3, 2, 0, 7, 15},
                {8, 2, 15, 11, 5, 9, 5, 5},
                {12, 12, 14, 2, 3, 11, 9, 3}
        };
        for (int j = 0; j < Summamod232.length; j++) {
            res[j] = table[d[j]][p[j] - 1];
        }
        for (int b = 0; b < Summamod232.length; b++) {
            result1 = Integer.toBinaryString(res[b]);
            while (result1.length() != 4) {
                result1 = "0" + result1;
            }
            result += result1;
        }
        return result;
    }

    public static String sdvig(String pot) {
        String[] sd = new String[pot.length()];
        for (int i = 0; i < pot.length();i++){
            while (i+11 <= pot.length()-1) {
                sd[i] = String.valueOf(pot.charAt(i + 11));
                i++;
            }
            int a =0;
            sd[i] = String.valueOf(pot.charAt(i-21));
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < sd.length; i++) {
            strBuilder.append(sd[i]);
        }
        String res = strBuilder.toString();
        return res;
    }
    public static String xor(String sdvigR0, String BinaryL0) {
        String L0 = String.join(" ", BinaryL0);
        L0 = L0.replaceAll("\\s+", "");
        int[] res = new int[sdvigR0.length()];
        for (int i = 0; i < sdvigR0.length();i++){
            res[i] = Integer.parseInt(String.valueOf(sdvigR0.charAt(i)))^Integer.parseInt(String.valueOf(L0.charAt(i)));
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            strBuilder.append(res[i]);
        }
        String res1 = strBuilder.toString();
        return res1;
    }
    public static String top(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result += String.valueOf(str.charAt(i));
            if ((i + 1) % 4 == 0) {
                result += " ";
            }
        }
        return result;
    }

}
