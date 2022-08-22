import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Locale;
public class RSA {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner in = new Scanner(System.in);
        String ALF = " АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";
        System.out.println("Задание 3. Алгоритм шифрования RSA.\nВведите шифруемое сообщение: ");
        String input = in.nextLine();
        System.out.println("Введите значение р: ");
        int p = in.nextInt();
        System.out.println("Введите значение q: ");
        int q = in.nextInt();
        int n = p * q;
        int fi = (p - 1) * (q - 1);
        int d = 25;
        int e = pick_e(fi,d);
        System.out.println("Получаем, (" + e + "," + n + ")- открытый ключ");
        System.out.println("(" + d +"," + n + ")- закрытый ключ");
        System.out.println("Зашифруем сообщение, используя открытый ключ (" + e + "," + n + "):");
        int res1[] = Encrypt(input, ALF, e, n);
        System.out.println("C1 = " + res1[0]);
        System.out.println("C2 = " + res1[1]);
        System.out.println("C3 = " + res1[2]);
        System.out.println("Расшифруем сообщение, используя закрытый ключ (" + d + "," + n + "):");
        int res2[] = Decrypt(res1, ALF, d, n);
        System.out.println("M1 = " + res2[0]);
        System.out.println("M2 = " + res2[1]);
        System.out.println("M3 = " + res2[2]);
    }

    public static int pick_e(int fi, int d) {
        double e = 2;
        for (int y = 1; y <= fi; y++) {
            e = (double) ((fi * y) + 1) / d;
            if (e == Math.round(e)) {
                break;
            } else {
                continue;
            }
        }
        return ((int) e);
    }

    public static int[] Encrypt(String input, String ALF, int e, int n){
        BigInteger c = new BigInteger(String.valueOf(0));
        int[] res = new int[input.length()];
        input = input.toUpperCase(Locale.ROOT);
        for (int i = 0; i < input.length(); i++){
            int index = ALF.indexOf(input.charAt(i));
            c = new BigInteger(String.valueOf(index));
            BigInteger e1 = new BigInteger(String.valueOf(e));
            BigInteger n1 = new BigInteger(String.valueOf(n));
            c = c.modPow(e1, n1);
            res[i] = c.intValue();
        }
        return res;
    }
    public static int[] Decrypt(int[] Cryptinput, String ALF, int d, int n) {
        int[] res = new int[Cryptinput.length];
        for (int i = 0; i < Cryptinput.length; i++){
            BigInteger text1 = new BigInteger(String.valueOf(Cryptinput[i]));
            BigInteger n1 = new BigInteger(String.valueOf(n));
            BigInteger d1 = new BigInteger(String.valueOf(d));
            text1 = text1.modPow(d1, n1);
            res[i] = text1.intValue();
        }
        return res;
    }
}
