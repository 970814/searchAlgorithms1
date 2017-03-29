package others2017321.algorithms;

import java.util.ArrayList;

/**
 * Created by L on 2017/3/21.
 */
public class LinearRegression {
    public static void linearRegression(ArrayList<Float> xPoints, ArrayList<Float> yPoints, int n, double[] equation) {
        if (n < 2) return;
        double diffXY = 0.0;
        double diffX = 0.0;
        double sumX = 0;
        double sumY = 0;
        for (int i = 0; i < n; i++) {
            float x = xPoints.get(i);
            float y = yPoints.get(i);
            sumX += x;
            sumY += y;
            diffXY += x * y;
            diffX += x * x;
        }
        diffXY -= sumX * sumY / n;
        diffX -= sumX * sumX / n;
        double b = diffXY / diffX;
        double a = sumY / n - b * sumX / n;
//        sumX /= n;
//        sumY /= n;
        equation[0] = b;
        equation[1] = a;
        equation[2] = xPoints.get(0);
        equation[3] = equation[2] * b + a;
        equation[4] = xPoints.get(n - 1);
        equation[5] = equation[4] * b + a;
    }
    public static void linearRegression(PointGroup group) {
        linearRegression(group.xPoints, group.yPoints, group.nPoints, group.equation);
    }
}
