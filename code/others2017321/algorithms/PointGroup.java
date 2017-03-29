package others2017321.algorithms;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by L on 2017/3/21.
 */
public class PointGroup {
    public ArrayList<Float> xPoints = new ArrayList<>();//1,2,3
    public ArrayList<Float> yPoints = new ArrayList<>();//2,3,4
    public int nPoints;

    public PointGroup() {
    }
    public PointGroup(float[] target, float[] origination) {
        for (int i = 0; i < target.length; i++) {
            add(new Point2D.Float(target[i], origination[i]));
        }
    }

    @Override
    public String toString() {
        return yPoints.toString();
    }

    public Point2D.Float get(int i) {
        if (i >= nPoints) throw new NoSuchElementException();
        return new Point2D.Float(xPoints.get(i), yPoints.get(i));
    }

    public void remove(int i) {
        xPoints.remove(i);
        yPoints.remove(i);
        nPoints--;
    }

    public int search(Point2D.Float p) {

        for (int i = 0; i < nPoints; i++)
            if (xPoints.get(i) == p.x && yPoints.get(i) == p.y) return i;

        return -1;
    }

    public void set(int where, Point2D.Float p) {
        xPoints.set(where,  p.x);
        yPoints.set(where,  p.y);
    }
    public void add(Point point) {
        int i = 0;
        while (i < nPoints && point.x > xPoints.get(i)) i++;
        xPoints.add(i, (float)point.x);
        yPoints.add(i, (float)point.y);
        nPoints++;
    }
    public void add(Point2D.Float point) {
        int i = 0;
        while (i < nPoints && point.x > xPoints.get(i)) i++;
        xPoints.add(i, point.x);
        yPoints.add(i, point.y);
        nPoints++;
    }

    public boolean isCaled = false;
    public boolean isCaledFeature = false;
    public void cal() {
        LinearRegression.linearRegression(this);
        isCaled = true;
    }

    public double[] equation = new double[6];
    public LinearRegressionFeature regressionFeature = new LinearRegressionFeature();
    public void calFeature() {
        regressionFeature.subsectionLinearRegression(this);
        isCaledFeature = true;
    }

    public int searchCeilX(float x) {
        int i = 0;
        for (; i < nPoints; i++) if (xPoints.get(i) >= x) break;
        i--;
        return i;
    }
}