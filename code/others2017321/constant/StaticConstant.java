package others2017321.constant;

/**
 * Created by T on 2017/3/25.
 */
public class StaticConstant {
    //相邻两条线条是否被识别成为一条特征线条的其中一个条件是 长度比例应该在c1 与 1.0/c1之间
    //目前 0.5 到2.0 代表着长的线条是短的2倍以上就识别为一条特征线条，
    //也就是说这个值越大，误差越大
    public  static double c1 = 0.5;//***
    ///////////////////////////////////////////////////////////////////////
    //相邻两条线条是否被识别成为一条特征线条的另外一个条件
    // 如果两条线段偏差的角度大于这个值，将会被识别成两条特征线条
    //通常和误差角度相等吧，
    //也就是说这个值越大，误差越大
    public static double c2 = 28;//**
    ///////////////////////////////////////////////////////////////////////////
    //如果你要忽略上面的两个条件，就将该值设置为true
    //那么算法将采用只要相邻线条属于同种增长情况,就会被识别为同一条特征线条，
    public static boolean flag = false;


    public static double c = 32;//每两条特征线条如果偏差大于这个角度将被视为不相似

    //特征线条误差角度 ***    你通常只要修改这个地方就可以了

    public static float d = 2.259f;

    public static float xd = 1f;
    public static float yd = 1f;

    public static String parent = "c://data/";
    public static int Width = 1400;
    public static int Height = 800;

//    public static final boolean flag2 = false;
//    public final int c0 = 10;
}
