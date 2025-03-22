import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Counter {
    public static String typeOfGrape = null;
    public static int countGrapes = 0;
    public static double progress;
    public static boolean isFiled = false;
    private static final int STEP = 100;
    public static double sotka = 1;
    public static Map<String, Double> grapes  = new HashMap(){{
        put("wheat", 425.0 * STEP);
        put("corn", 8.0 * STEP);
        put("rye", 440.0 * STEP);
        put("barley", 350.0 * STEP);
        put("oat", 375.0 * STEP);
        put("sunflower", 6.0 * STEP);
        put("soy", 48.0 * STEP);
        put("sugar beets", 11.0 * STEP);
        put("hemp", 70.0 * STEP);
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
                String soundFilePath = "sounds\\succeed.wav";
                try {
                    File soundFile = new File(soundFilePath);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static int percentOfDoneWork() {
        if ((Application.getDropDownMenu().getSelectedItem().equals("nothing chosen") && !Application.getAreaField().hasFocus() && !Application.getAreaField().getText().isEmpty()) || !grapes.containsKey(typeOfGrape)) return 0;
        progress = (double) countGrapes / grapes.get(typeOfGrape) * 100;
        return (int) progress;
    }
    public static void restartCounter(){
        isFiled = false;
        countGrapes = 0;
        progress = 0;
    }
    public static void setSotka(double sotka_) {
        sotka = sotka_;
    }
    public static void updateMap() {
        grapes.clear();
        grapes.put("wheat", 425.0 * sotka * STEP);
        grapes.put("corn", 8.0 * sotka * STEP);
        grapes.put("rye", 440.0 * sotka * STEP);
        grapes.put("barley", 350.0 * sotka * STEP);
        grapes.put("oat", 375.0 * sotka * STEP);
        grapes.put("sunflower", 6.0 * sotka * STEP);
        grapes.put("soy", 48.0 * sotka * STEP);
        grapes.put("sugar beets", 11.0 * sotka * STEP);
        grapes.put("hemp", 70.0 * sotka * STEP);
    }

    public static boolean isIsFiled() {
        return isFiled;
    }
}
