import java.util.HashMap;
import java.util.Map;

public class Counter {
    public static String typeOfGrape = null;
    public static int countGrapes = 0;
    public static boolean isFiled = false;
    private static final int STEP = 100;
    public static double sotka = 1;
    public static Map<String, Double> grapes  = new HashMap(){{
        put("пшениця", 425.0 * STEP);
        put("кукурудза", 8.0 * STEP);
        put("жито", 440.0 * STEP);
        put("ячмінь", 350.0 * STEP);
        put("овес", 375.0 * STEP);
        put("соняшник", 6.0 * STEP);
        put("соя", 48.0 * STEP);
        put("цукрові буряки", 11.0 * STEP);
        put("конопля", 70.0 * STEP);
    }};

    Counter(){
    }
    public void Count(){
        if(grapes.containsKey(typeOfGrape) && !Application.getAreaField().getText().isEmpty() && !Application.getAreaField().hasFocus()) {
            if(countGrapes <= grapes.get(typeOfGrape)){
                countGrapes++;
                System.out.println(countGrapes);
            } else {
                isFiled = true;
            }
        }
    }
    public static int percentOfDoneWork() {
        if ((Application.getDropDownMenu().getSelectedItem().equals("нічого не обрано") && !Application.getAreaField().hasFocus() && !Application.getAreaField().getText().isEmpty()) || !grapes.containsKey(typeOfGrape)) return 0;
        double progress = (double) countGrapes / grapes.get(typeOfGrape) * 100;
        return (int) progress;
    }
    /*public static void restartCounter(){
        isFiled = false;
        progress = 0;
        countGrapes = 0;
        typeOfGrape = null;
    }*/
    public static void setSotka(double sotka_) {
        sotka = sotka_;
    }
    public static void updateMap() {
        grapes.clear();
        grapes.put("пшениця", 425.0 * sotka * STEP);
        grapes.put("кукурудза", 8.0 * sotka * STEP);
        grapes.put("жито", 440.0 * sotka * STEP);
        grapes.put("ячмінь", 350.0 * sotka * STEP);
        grapes.put("овес", 375.0 * sotka * STEP);
        grapes.put("соняшник", 6.0 * sotka * STEP);
        grapes.put("соя", 48.0 * sotka * STEP);
        grapes.put("цукрові буряки", 11.0 * sotka * STEP);
        grapes.put("конопля", 70.0 * sotka * STEP);
    }
}
