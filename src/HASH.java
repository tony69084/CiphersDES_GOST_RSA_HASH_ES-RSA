import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Locale;
public class HASH {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner in = new Scanner(System.in);
        String ALF = " АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";
        System.out.println("Задание 4. Функция хеширования.\nВведите хэшируемое сообщение: ");
        String input = in.nextLine();
        System.out.println("Введите значение р: ");
        int p = in.nextInt();
        System.out.println("Введите значение q: ");
        int q = in.nextInt();
        System.out.println("Введите значение H0: ");
        int H0 = in.nextInt();
        int n = p * q;
        int H = Hash(input,ALF,H0,n);
        System.out.println("Следовательно, хеш-образ сообщения «КОВАЛЕВ» равно " + H);
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
}
