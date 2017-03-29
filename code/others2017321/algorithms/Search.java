package others2017321.algorithms;

import others2017321.io.Read;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by L on 2017/3/26.
 */
public class Search {
    @Deprecated
    public static boolean search(
            float[] targetX, float[] targetY, float[] originationX, float[] originationY) {
        ArrayList<int[]> resembleParts = null;
        PointGroup targetGroup = new PointGroup(targetX, targetY);
        PointGroup originationGroup = new PointGroup(originationX, originationY);
        targetGroup.calFeature();
        originationGroup.calFeature();
        int[] mostLike = PolylineComparison.search(targetGroup, originationGroup, resembleParts);
        return !resembleParts.isEmpty();
    }

    public static boolean search(PointGroup target, PointGroup origination) {
        ArrayList<int[]> resembleParts = null;
        target.calFeature();
        origination.calFeature();
        return PolylineComparison.search(target, origination, resembleParts) != null;
    }

    public static boolean search(File child, File parent) throws FileNotFoundException {
        return search(Read.read(child), Read.read(parent));
    }
}
