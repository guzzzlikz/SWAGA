import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

//Загальний клас камера
class Camera {
    int index;
    private VideoCapture capture;
    private Mat frame;
    private Mat greyLast;
    private String name;
    private boolean motionBool;

    public Camera(int index) {
        this.index = index;
        capture = new VideoCapture(index);
        frame = new Mat();
        greyLast = new Mat();
        name = "Camera" + index;
        motionBool = false;
    }

    public boolean isMotionBool() {
        return motionBool;
    }

    public void setMotionBool(boolean motionBool) {
        this.motionBool = motionBool;
    }

    public Mat getGreyLast() {
        return greyLast;
    }

    public void setGreyLast(Mat greyLast) {
        this.greyLast = greyLast;
    }

    public int getIndex() {
        return index;
    }

    public VideoCapture getCapture() {
        return capture;
    }

    public Mat getFrame() {
        return frame;
    }

    public String getName() {
        return name;
    }
}

public class OperateCamera {
    private List<Camera> list = new ArrayList<>();
    private boolean active = false;

    OperateCamera() {
        Camera cam1 = new Camera(0);
        //Camera cam2 = new Camera(1);
        list.add(cam1);
        //list.add(cam2);
        createWindow();
    }

    public boolean isMotionBool() {
        return motionBool;
    }
    //Запуск камери
    public void startCameras() {
        active = true;
        for (Camera c : list) {
            if (!c.getCapture().isOpened()) {
                System.out.println("Error: Could not open camera " + c.getIndex());
                return;
            }
        }
        processFrames();
    }
    //Зупинка камер, використовується для Application
    public void stopCameras() {
        active = false;
        removeCameras();
    }
    //Створення вікна (бажано не чіпати, я підрахую потім)
    private void createWindow() {
        int x = 0;
        int y = 0;
        for (Camera c : list) {
            HighGui.namedWindow(c.getName(), HighGui.WINDOW_NORMAL);
            HighGui.resizeWindow(c.getName(), 480, 270);
            HighGui.moveWindow(c.getName(), 680, y);
            y += 300;
        }
    }
    //Обробка кадрів, відображення на екрані
    private void processFrames() {
        if (!active) return;

        for (Camera c : list) {
            c.setMotionBool(false);
            c.getCapture().read(c.getFrame());
            if (c.getFrame().empty()) {
                System.out.println("Error: Could not capture frame from " + c.getName());
                continue;
            }
            process(c);
            Imgproc.putText(c.getFrame(), Boolean.toString(c.isMotionBool()), new Point(100, 135), Imgproc.FONT_HERSHEY_COMPLEX_SMALL, 1.0, new Scalar(255, 255, 255), 2);
       
            if(hasFallen(c)){
                Counter.Count();
            }
                // Count property TODO(Add if based on type of grapes, remove hui pls)
//                if(Counter.countGrapes == 470){
//                    System.out.println("HUI");
//                }
            HighGui.imshow(c.getName(), c.getFrame());
        }
        //ВСЕ ЩО НІЖЧЕ НІ ЧІПАТИ, ЛЕДВЕ ПРАЦЮЄ!!!
        int key = HighGui.waitKey(30);
        if (key == 27) {
            stopCameras();
            return;
        }

        javax.swing.Timer timer = new javax.swing.Timer(30, e -> processFrames());
        timer.setRepeats(false);
        timer.start();
    }

    public boolean hasFallen(Camera c) {
        return c.isMotionBool();
    }
    //Логіка детектору руху
    private void process(Camera cam) {
        //Матриці
        Mat grey = new Mat();
        Mat diff = new Mat();
        Mat thresh = new Mat();
        Mat dilated = new Mat();

        //Дзеркальне відображення
        Core.flip(cam.getFrame(), cam.getFrame(), 1);
        //Конвертим в чб формат
        Imgproc.cvtColor(cam.getFrame(), grey, Imgproc.COLOR_BGR2GRAY);

        //Блюр Гауса, почитаєте на вікі
        Imgproc.GaussianBlur(grey, grey, new Size(5, 5), 0);


        if (cam.getGreyLast().empty()) {
            grey.copyTo(cam.getGreyLast());
            return;
        }

        Core.absdiff(cam.getGreyLast(), grey, diff);

        Imgproc.threshold(diff, thresh, 15, 255, Imgproc.THRESH_BINARY);

        Imgproc.dilate(thresh, dilated, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)));

        //Контури об`єкту який фіксує камера
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilated, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        for (MatOfPoint contour : contours) {
            if (contour.size().height > 10) {
                Rect boundingRect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(cam.getFrame(), boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 2);
                cam.setMotionBool(true);
            }
        }
        Core.addWeighted(cam.getGreyLast(), 0.5, grey, 0.5, 0, cam.getGreyLast());
    }
    //Закриття камер !!!НЕ ЧІПАТИ, ПРАЦЮЄ НА СОПЛЯХ!!!
    public void removeCameras() {
        for (Camera c : list) {
            c.setMotionBool(false);
            c.getCapture().release();
        }
        HighGui.waitKey(1000);
        HighGui.destroyAllWindows();

    }
}