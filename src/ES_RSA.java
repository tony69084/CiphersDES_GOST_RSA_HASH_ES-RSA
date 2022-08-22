import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Locale;
public class ES_RSA {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner in = new Scanner(System.in);
        String ALF = " АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";
        System.out.println("Задание 5. Электронная цифровая подпись.\nВведите хэшируемое сообщение: ");
        String input = in.nextLine();
        System.out.println("Введите значение р: ");
        int p = in.nextInt();
        System.out.println("Введите значение q: ");
        int q = in.nextInt();
        System.out.println("Введите значение H0: ");
        int H0 = in.nextInt();
        int n = p * q;
        int hash = Hash(input, ALF, H0,n);
        System.out.println("Следовательно, хеш-образ равен " + hash + ". Найдем закрытый и открытый ключи:");
        int fi = (p - 1) * (q - 1);
        int d = 23;
        int e = pick_e(fi, d);
        System.out.println("Получаем, (" + e + "," + n + ")- открытый ключ");
        System.out.println("(" + d + "," + n + ")- закрытый ключ");
        int res1 = Encrypt(hash, d, n);
        System.out.println("Тогда ЭЦП сообщения, состоящего из фамилии, будет равно");
        System.out.println("s = " + res1);
        System.out.println("Для проверки ЭЦП, используя открытый ключ  (" + e + "," + n + "):");
        int res2 = Decrypt(res1, e, n);
        System.out.println("H = " + res2);
        System.out.println("Так как хеш-образ сообщения совпадает с найденным значением H, то подпись признается подлинной.");
    }
    public static int Hash(String input, String ALF, int H0, int n){
        int[] H = new int[input.length()+1];
        H[0] = H0;
        input = input.toUpperCase(Locale.ROOT);
        for (int i = 1; i <= input.length(); i++){
            int index = ALF.indexOf(input.charAt(i-1));
            int d = (int)Math.pow((H[i-1]+index),2);
            H[i] = d % n;
            System.out.println("H" + i + " = " + H[i]);
        }
        return H[input.length()];
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
    public static int Encrypt(int input, int d, int n){
        BigInteger c = new BigInteger(String.valueOf(0));
        c = new BigInteger(String.valueOf(input));
        BigInteger e1 = new BigInteger(String.valueOf(d));
        BigInteger n1 = new BigInteger(String.valueOf(n));
        c = c.modPow(e1, n1);
        int  res = c.intValue();
        return res;
    }
    public static int Decrypt(int Cryptinput, int e, int n) {
        BigInteger text1 = new BigInteger(String.valueOf(Cryptinput));
        BigInteger n1 = new BigInteger(String.valueOf(n));
        BigInteger d1 = new BigInteger(String.valueOf(e));
        text1 = text1.modPow(d1, n1);
        int res = text1.intValue();
        return res;
    }
}
