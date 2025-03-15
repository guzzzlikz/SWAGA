public class Counter {
    private static OperateCamera operateCamera;
    public static int countGrapes = 0;
    Counter(OperateCamera operateCamera){
        this.operateCamera = operateCamera;
    }

    public static void Count(){
        countGrapes++;
        System.out.println(countGrapes);
    }
}
