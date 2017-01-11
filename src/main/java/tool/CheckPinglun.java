package tool;

/**
 * Created by GreendaMi on 2017/1/11.
 */

public class CheckPinglun {
    static String[] words = {"逼","草","操","fuck","FUCK","傻","病"};

    public static boolean isOk(String content){
        for (String s: words) {
            if(content.contains(s)){
                return false;
            }
        }
        return true;
    }
}
