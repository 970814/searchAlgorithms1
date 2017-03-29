package others2017321.algorithms;

import others2017321.constant.StaticConstant;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by L on 2017/3/22.
 */
public class LinearRegressionFeature {
    public ArrayList<double[]> equations = new ArrayList<>();
    public ArrayList<Integer> parts = new ArrayList<>();
    public static double[] linearRegression(ArrayList<Float> xPoints, ArrayList<Float> yPoints, final int from, final int to) {
        int n = to - from;
        if (n < 2) return null;
        double diffXY = 0.0;
        double diffX = 0.0;
        double sumX = 0;
        double sumY = 0;
        for (int tmp = from; tmp < to; tmp++) {
            float x = xPoints.get(tmp);
            float y = yPoints.get(tmp);
            sumX += x;
            sumY += y;
            diffXY += x * y;
            diffX += x * x;
        }
        diffXY -= sumX * sumY / n;
        diffX -= sumX * sumX / n;
        double b = diffXY / diffX;
        double a = sumY / n - b * sumX / n;
        double[] equation = new double[6];
        equation[0] = b;
        equation[1] = a;
        equation[2] = xPoints.get(from);
        equation[3] = equation[2] * b + a;
        equation[4] = xPoints.get(to - 1);
        equation[5] = equation[4] * b + a;
        return equation;
    }

    public void subsectionLinearRegression(PointGroup group) {
        equations.clear();
        parts.clear();
        if (group.nPoints < 2) return;
        float preX = group.xPoints.get(0);
        float preY = group.yPoints.get(0);
        Double preLength = 0.0;
        Double preK = null;
        for (int i = 1; i < group.nPoints; i++) {
            float x = group.xPoints.get(i);
            float y = group.yPoints.get(i);
            double k = (y - preY) / (double) (x - preX);
            double length = Point.distance(preX, preY, x, y);
            if (StaticConstant.flag) {
                if (preK != null)
                    if (preK != 0) {
                        if (preK * k <= 0) parts.add(i);
                    } else if (k != 0) parts.add(i);
                preK = k;
                preX = x;
                preY = y;
            } else {
                boolean flag = true;//是否被归纳在一个集合中
                if (preK != null
                        && (length / preLength > StaticConstant.c1 && length / preLength < 1.0 / StaticConstant.c1
                        && Math.abs(Math.toDegrees(Math.atan(preK)) - Math.toDegrees(Math.atan(k)))
                        > StaticConstant.c2 || preK * k < 0)) {

                    preLength = 0.0;
                    parts.add(i);
                    flag = false;
                }
                if (preK != null && flag && k == 0 && preK != 0) {
                } else {
                    preK = k;
                }
                preLength += length;
                preX = x;
                preY = y;
            }


        }
        parts.add(group.nPoints);
        cal(group);
    }

    private void cal(PointGroup group) {
        int from = 0;
        for (int i = 0; i < parts.size(); i++) {
            int to = parts.get(i);
            cal(group, from, to);
            from = to - 1;
        }
    }

    private void cal(PointGroup group, int from, int to) {
        double[] equation = linearRegression(group.xPoints, group.yPoints, from, to);
        equations.add(equation);
    }
}
