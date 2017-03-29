package others2017321.algorithms;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Created by L on 2017/3/21.
 */
@SuppressWarnings("Duplicates")
public class Lagrange {
    public static double lagrangeInterpolationFormula(ArrayList<Float> xPoints, ArrayList<Float> yPoints, int n, double x0) {
        double sum = 0;
        double L;
        for (int i = 0; i <n; i++) {
            float x = xPoints.get(i);
            float y = yPoints.get(i);
            L = 1;
            for (int j = 0; j < n; j++) {
                float xj = xPoints.get(j);
                if (j != i)
                    L = L * (x0 - xj) / (x - xj);
            }
            sum = sum + L * y;
        }
        return sum;
    }
    public static double cal(ArrayList<Integer> xPoints, ArrayList<Integer> yPoints, int n, double x0) {
        double sum = 0;
        double L;
        for (int i = 0; i <n; i++) {
            int x = xPoints.get(i);
            int y = yPoints.get(i);
            L = 1;
            for (int j = 0; j < n; j++) {
                int xj = xPoints.get(j);
                if (j != i)
                    L = L * (x0 - xj) / (x - xj);
            }
            sum = sum + L * y;
        }
        return sum;
    }

    public static double barycentricLagrangeInterpolationFormula(
            ArrayList<Float> xPoints, ArrayList<Float> yPoints, int n, double x0) {
        double[] w = new double[n];
        double u = 0.0;
        double v = 0.0;
        for (int i = 0; i < n; i++) {
            float x = xPoints.get(i);
            float y = yPoints.get(i);
            w[i] = 1.0;
            for (int j = 0; j < w.length; j++) {
                float xj = xPoints.get(j);
                if (i != j) w[i] *= x - xj;
            }
            w[i] = 1.0 / (w[i] * (x0 - x));
            v += w[i];
            u += w[i] * y;
        }
        return u / v;
    }

    static Line2D line2D = new Line2D.Double();
    public static void drawBarycentricLagrangeInterpolationFormula(Graphics2D d, PointGroup group) {
        if (group.nPoints < 2) return;
        double min = group.xPoints.get(0);
        double max = group.xPoints.get(group.nPoints - 1);
        for (double x = min; x < max; x++) {
            double y = barycentricLagrangeInterpolationFormula(group.xPoints, group.yPoints, group.nPoints, x);
            line2D.setLine(x, y, x, y);
            d.draw(line2D);
        }
    }
    public static void drawLagrangeInterpolationFormula(Graphics2D d, PointGroup group) {
        if (group.nPoints < 2) return;
        double min = group.xPoints.get(0);
        double max = group.xPoints.get(group.nPoints - 1);
        Double preY = null;
//        int count = 0;
        for (double x = min; x < max; x++) {
            double y = lagrangeInterpolationFormula(group.xPoints, group.yPoints, group.nPoints, x);
//            if (preY != null && Math.abs(preY - y) < 0.05) {
//                d.setColor(Color.PINK);
//                line2D.setLine(min, y, max, y);
//                d.setStroke(Test.thinStroke);
//                d.draw(line2D);
//                d.setStroke(Test.fatStroke);
//                d.setColor(Color.RED);
//                count++;
//            }
//            preY = y;
            line2D.setLine(x, y, x, y);
            d.draw(line2D);
        }
//        System.out.println(count);

    }
}
