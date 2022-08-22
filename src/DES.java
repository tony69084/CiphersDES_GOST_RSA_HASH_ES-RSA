import java.io.UnsupportedEncodingException;
import java.util.Scanner;
public class DES {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner in = new Scanner(System.in);
        System.out.println("Задание 1. Алгоритм шифрования DES.\nВведите сообщение, которое необходимо зашифровать: ");
        String input = in.nextLine();
        System.out.println("Введите ключ(Отчество): ");
        String K = in.nextLine();
        String L0 = input.substring(0, 4);
        String R0 = input.substring(4, 8);
        String BinaryL0 = ToBinary(L0);
        String BinaryR0 = ToBinary(R0);
        String BinaryK = ToBinary(K);
        System.out.println("\nПеревод исходного сообщения в двоичный код:\n" + L0 + ": " + BinaryL0 + "\n" + R0 + ": " + BinaryR0);
        String resp = pert(BinaryR0, BinaryL0);
        System.out.println("Результат после перестановки: " + top(resp));
        String resrL0 = resp.substring(0, 32);
        String resrR0 = resp.substring(32, 64);
        System.out.println("Разбиваем блоки пополам \nL0: " + top(resrL0) + "\n" + "R0: " + top(resrR0));
        String RasresR0 = ras(resrR0);
        System.out.println("Расширяем подблок R0 в 48-битовый подблок: \nR0: " + top1(RasresR0));
        System.out.println("\nПеревод ключа в двоичный код:\n" + K + ": " + BinaryK);
        String resK = del(BinaryK);
        System.out.println("Получаем ключ раунда: " + top1(resK));
        String resOT = xor(RasresR0, resK);
        System.out.println("Результат отбеливания: " + top1(resOT));
        String resSB = Sblocks(resOT);
        System.out.println("Результат выполнения 8-ми операций подстановки с помощью S-блоков: " + top(resSB));
        String resP1 = pert2(resSB);
        System.out.println("Результат после прямой перестановки 32-ух выходных битов из S-блоков: " + top(resP1));
        String resmod2 = xor(resP1, resrL0);
        System.out.println("Результат операции поразрядного суммирования R0 и L0: " + top(resmod2));
        System.out.println("Записываем полученные подблоки: \nL1=R0:" + top(resrR0) + "\n" + "R1: " + top(resmod2));
        System.out.println("Конкатенация полученных последовательностей имеет вид: " + top(resrR0+resmod2));
        System.out.println("Результат после конечной перестановки: " + top2(pert3(resrR0+resmod2)));
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

    public static String pert(String BinaryR0, String BinaryL0) {
        String result = "";
        String res = "";
        String R0 = BinaryR0.replaceAll("\\s+", "");
        String L0 = BinaryL0.replaceAll("\\s+", "");
        result = L0 + R0;
        int[] Table1 =
                {
                        58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
                        62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
                        57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
                        61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
                };
        for (int i = 0; i < result.length(); i++) {
            res += String.valueOf(result.charAt(Table1[i] - 1));
        }
        return res;
    }

    public static String ras(String BinaryR0) {
        String result = "";
        String R0 = BinaryR0.replaceAll("\\s+", "");
        int[] Table2 =
                {
                        32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
                        12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21,
                        22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1,
                };
        for (int i = 0; i < Table2.length; i++) {
            result += String.valueOf(R0.charAt(Table2[i] - 1));
        }
        return result;
    }

    public static String del(String BinaryK) {
        String result = "";
        String res = "";
        String K = BinaryK.replaceAll("\\s+", "");
        for (int i = 0; i < K.length(); i++) {
            if ((i + 1) % 8 != 0) {
                result += String.valueOf(K.charAt(i));
            }
        }
        for (int i = 0; i < result.length(); i++) {
            if (i == result.length() - 2) {
                continue;
            }
            if ((i + 1) % 8 != 0) {
                res += String.valueOf(result.charAt(i));
            }
        }
        return res;
    }

    public static String xor(String RasR0, String BinaryK) {
        int[] res = new int[RasR0.length()];
        for (int i = 0; i < RasR0.length(); i++) {
            res[i] = Integer.parseInt(String.valueOf(RasR0.charAt(i))) ^ Integer.parseInt(String.valueOf(BinaryK.charAt(i)));
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            strBuilder.append(res[i]);
        }
        String res1 = strBuilder.toString();
        return res1;
    }

    public static String Sblocks(String RKX) {
        String result = "";
        String res2 = "";
        String res3 = "";
        int res4 = 0;
        int res5 = 0;
        for (int i = 0; i < RKX.length(); i++) {
            result += String.valueOf(RKX.charAt(i));
            if ((i + 1) % 6 == 0) {
                result += " ";
            }
        }
        String[] RKX1 = result.split(" ");
        String[] res = new String[RKX1.length];
        int[][][] S = {
                new int[][]{
                        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                },
                new int[][]{
                        {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                        {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                        {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                },
                new int[][]{
                        {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                        {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                        {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                },
                new int[][]{
                        {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                        {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                        {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                },
                new int[][]{
                        {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                        {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                        {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                },
                new int[][]{
                        {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                        {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                        {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                },
                new int[][]{
                        {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                        {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                        {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                },
                new int[][]{
                        {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                        {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                        {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                }
        };
        for (int j = 0; j < S.length; j++) {
            res2 = "";
            res3 = "";
            for (int k = 0; k < RKX1[0].length(); k++) {
                if (k == 0 || k == RKX1[0].length() - 1) {
                    res2 += String.valueOf(RKX1[j].charAt(k));
                } else
                    res3 += String.valueOf(RKX1[j].charAt(k));
            }
            res4 = Integer.parseInt(res2, 2);
            res5 = Integer.parseInt(res3, 2);
            res[j] = Integer.toBinaryString(S[j][res4][res5]);
            while (res[j].length() != 4) {
                res[j] = "0" + res[j];
            }
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            strBuilder.append(res[i]);
        }
        String res8 = strBuilder.toString();
        return res8;
    }

    public static String pert2(String R0) {
        String result = "";
        String res = "";
        int[] Table1 =
                {
                        16, 7, 20, 21, 29, 12, 28, 17,
                        1, 15, 23, 26, 5, 18, 31, 10,
                        2, 8, 24, 14, 32, 27, 3, 9,
                        19, 13, 30, 6, 22, 11, 4, 25
                };

        for (int i = 0; i < Table1.length; i++) {
            res += String.valueOf(R0.charAt(Table1[i] - 1));
        }
        return res;
    }

    public static String pert3(String R0) {
        String result = "";
        String res = "";
        int[] Table1 = {
                40, 8, 48, 16, 56, 24, 64, 32,
                39, 7, 47, 15, 55, 23, 63, 31,
                38, 6, 46, 14, 54, 22, 62, 30,
                37, 5, 45, 13, 53, 21, 61, 29,
                36, 4, 44, 12, 52, 20, 60, 28,
                35, 3, 43, 11, 51, 19, 59, 27,
                34, 2, 42, 10, 50, 18, 58, 26,
                33, 1, 41, 9, 49, 17, 57, 25
        };

        for (int i = 0; i < Table1.length; i++) {
            res += String.valueOf(R0.charAt(Table1[i] - 1));
        }
        return res;
    }
    public static String top1(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result += String.valueOf(str.charAt(i));
            if ((i + 1) % 6 == 0) {
                result += " ";
            }
        }
        return result;
    }
    public static String top2(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result += String.valueOf(str.charAt(i));
            if ((i + 1) % 8 == 0) {
                result += " ";
            }
        }
        return result;
    }
}
