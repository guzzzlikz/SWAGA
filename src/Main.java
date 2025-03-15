import org.opencv.core.Core;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OperateCamera camera = new OperateCamera();
        camera.run();
        /*Application application = new Application();
        application.run();*/
    }
}
