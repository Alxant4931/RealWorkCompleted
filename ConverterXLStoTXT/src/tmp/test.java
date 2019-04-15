package tmp;

/**
 * Created by Alxant on 10.03.2018.
 */
public class test {
    public static void main(String[] args) {
//        String number = "300";
//        String number = "300.0";
        String number = "300.00";



        System.out.println(Integer.parseInt(number.replace(".0", "")));
    }
}
