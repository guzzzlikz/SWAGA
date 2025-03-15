import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

class Camera  {
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
public class OperateCamera implements Runnable {
    List<Camera> list = new ArrayList<>();
    Camera cam1 = new Camera(0);
    Camera cam2 = new Camera(1);
    private boolean motionBool = false;
    OperateCamera() {
        list.add(cam1);
        list.add(cam2);
        createWindow();
    }

    public boolean isMotionBool() {
        return motionBool;
    }

    @Override
    public void run() {
        while (true) {
            for (Camera c : list) {
                c.setMotionBool(false);
                c.getCapture().read(c.getFrame());
                if (c.getFrame().empty()) {
                    System.out.println("Error: Could not capture frame from one or both cameras");
                    break;
                }
                process(c);
                Imgproc.putText(c.getFrame(), Boolean.toString(c.isMotionBool()), new Point(50, 50), Imgproc.FONT_HERSHEY_COMPLEX_SMALL, 1.0, new Scalar(255, 255, 255), 2);
                hasFallen(c);
                HighGui.imshow(c.getName(), c.getFrame());
            }
            int key = HighGui.waitKey(30);

            if (key == 27) {
                break;
            }
        }
    }
    private void createWindow() {
        for (Camera c : list) {
            HighGui.namedWindow(c.getName(), HighGui.WINDOW_NORMAL);
        }
    }
    public boolean hasFallen(Camera c) {
        return c.isMotionBool();
    }
    private void process(Camera cam) {
        Mat grey = new Mat();
        Mat diff = new Mat();
        Mat thresh = new Mat();
        Mat dilated = new Mat();

        Core.flip(cam.getFrame(), cam.getFrame(), 1);
        Imgproc.cvtColor(cam.getFrame(), grey, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(grey, grey, new Size(5, 5), 0);

        if (cam.getGreyLast().empty()) {
            grey.copyTo(cam.getGreyLast());
            return;
        }

        Core.absdiff(cam.getGreyLast(), grey, diff);

        Imgproc.threshold(diff, thresh, 15, 255, Imgproc.THRESH_BINARY);

        Imgproc.dilate(thresh, dilated, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)));

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
}
