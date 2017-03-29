package others2017321.io;

import others2017321.algorithms.PointGroup;
import others2017321.constant.StaticConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by T on 2017/3/25.
 */
public class Read {
    public static PointGroup read(String filename) throws FileNotFoundException {
        File file = new File(filename);
        return read(file);
    }
    public static PointGroup read(String filename, ArrayList<PointGroup> groups) throws FileNotFoundException {
        File file = new File(filename);
        PointGroup group1 = read(file, 0, Integer.MAX_VALUE, StaticConstant.d, 1.0f);
        PointGroup group2 = new PointGroup();
        float from = 0;
        for (int i = group1.nPoints - 1; i >= 0; i--){
            group2.yPoints.add(group1.yPoints.get(i));
            group2.xPoints.add(from);
            from += StaticConstant.d;
        }

        group2.nPoints = group1.nPoints;
        groups.add(group1);
        groups.add(group2);
        return group1;
    }

    public static PointGroup read(File file) throws FileNotFoundException {
       /* Scanner scanner = new Scanner(file);
        PointGroup group = new PointGroup();
        ArrayList<Float> yPoint = group.yPoints;
        ArrayList<Float> xPoint = group.xPoints;
        int from = 0;
        while (scanner.hasNext()) {
            xPoint.add((float) from);
            yPoint.add((Float.valueOf(scanner.nextLine())));
            group.nPoints++;
            from += StaticConstant.d;
        }
        scanner.close();*/
        return read(file, 0, Integer.MAX_VALUE, StaticConstant.d, 1.0f);
    }

    public static PointGroup read(String filename, int from, int to, float gap, float proportion) throws FileNotFoundException {

        return read(new File(filename), from, to, gap, proportion);
    }

    public static PointGroup read(File file, int fromIndex, int toIndex, float gap, float proportion) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        PointGroup group = new PointGroup();
        ArrayList<Float> yPoint = group.yPoints;
        ArrayList<Float> xPoint = group.xPoints;
        int lineCount = 0;
        while (scanner.hasNext() && lineCount < fromIndex) {
            scanner.nextLine();
            lineCount++;
        }
        int from = 0;
        while (scanner.hasNext() && lineCount < toIndex) {
            xPoint.add((float) from);
            yPoint.add(Float.valueOf(scanner.nextLine()) * proportion);
            group.nPoints++;
            from += gap;
            lineCount++;
        }
        scanner.close();
        return group;
    }
}
