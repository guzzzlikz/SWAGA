import org.opencv.core.Core;
public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //!!!НЕ ВИДАЛЯТИ НІ В ЯКОМУ РАЗІ, ВСЯ ПРОГРАМА НА ЦЬОМУ ПРАЦЮЄ!!!
        Application.run();
    }
}
