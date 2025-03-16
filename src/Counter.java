import java.util.HashMap;
import java.util.Map;

public class Counter {
    public static String typeOfGrape = null;
    public static int countGrapes = 0;
    public static boolean isFiled = false;
    public static Map<String, Integer> grapes  = new HashMap(){{
        put("пшениця", 425);
        put("кукурудза", 208);
        put("жито", 300);
        put("ячмінь", 350);
        put("овес", 375);
        put("соняшник", 103);
        put("соя", 215);
        put("цукрові буряки", 113);
        put("конопля", 500);
    }};

    Counter(){
    }
    public void Count(){
        if(grapes.containsKey(typeOfGrape)){
            if(countGrapes <= grapes.get(typeOfGrape)){
                countGrapes++;
                System.out.println(countGrapes);
            } else {
                isFiled = true;
            }
        }
    }
    public static int percentOfDoneWork() {
        if (typeOfGrape == null || !grapes.containsKey(typeOfGrape)) return 0;
        double progress = (double) countGrapes / grapes.get(typeOfGrape) * 100;
        return (int) progress;
    }

}
