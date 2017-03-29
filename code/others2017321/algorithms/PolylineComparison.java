package others2017321.algorithms;

import others2017321.constant.StaticConstant;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by L on 2017/3/22.
 */
@SuppressWarnings("Duplicates")
public class PolylineComparison {
    public static void clear() {
        if (sResembleParts != null) sResembleParts.clear();
    }
    public static ArrayList<ResemblePart> sResembleParts = null;

    public static int[] search(PointGroup targetGroup, PointGroup originationGroup) {
        return search(targetGroup, originationGroup, null);
    }


    public static class ResemblePart {
        public PointGroup group;
        public int[] mostLike;
        double diff;

        public ResemblePart(PointGroup group, int[] mostLike, double diff) {
            this.group = group;
            this.mostLike = mostLike;
            this.diff = diff;
        }

        @Override
        public String toString() {
            return Arrays.toString(mostLike);
        }
    }

    public static class FeaturePoint {
        float[] featureYT;
        public ArrayDeque<Integer>[] equivalentIndex;
        int[] array;
        public FeaturePoint(float[] featureYT) {
            this.featureYT = featureYT;
            equivalentIndex = new ArrayDeque[featureYT.length];
            calEquivalentIndex();
            array = getSortArray(featureYT.clone());
        }

        public void calEquivalentIndex() {
            boolean[] c = new boolean[featureYT.length];
            for (int i = 0; i < c.length; i++) {
                if (!c[i]) {
                    c[i] = true;
                    for (int j = i + 1; j < c.length; j++) {
                        if (!c[j] && featureYT[i] == featureYT[j]) {
                            add(i, equivalentIndex, j);
                            c[j] = true;
                        }
                    }
                }
            }
            for (int i = 0; i < equivalentIndex.length; i++) {
                if (equivalentIndex[i] != null) for (Integer j : equivalentIndex[i]) {
                    equivalentIndex[j] = new ArrayDeque<>();
                    equivalentIndex[j].offer(i);
                    equivalentIndex[i].stream().filter(k -> k != j).forEach(k -> equivalentIndex[j].offer(k));
                }
            }
        }

        private void add(int which, ArrayDeque<Integer>[] equivalentIndex, int i) {
            if (equivalentIndex[which] == null) equivalentIndex[which] = new ArrayDeque<>();
            equivalentIndex[which].offer(i);
        }

        @Override
        public boolean equals(Object obj) {
            FeaturePoint otherFeaturePoint = (FeaturePoint) obj;
            int[] ar = otherFeaturePoint.array;
            for (int i = 0; i < ar.length; i++) {
                if (ar[i] != array[i]) {
                    ArrayDeque<Integer> deque = equivalentIndex[array[i]];
                    if (deque != null) {
                        boolean equal = false;
                        for (Integer j : deque)
                            if (j == ar[i]) {
                                equal = true;
                                break;
                            }
                        if (!equal) return false;
                    } else return false;
                }
            }
            return true;
        }
    }


    public static int[] search(PointGroup target, PointGroup origination, ArrayList<int[]> resembleParts) {
        int m = target.regressionFeature.equations.size();
        int n = origination.regressionFeature.equations.size();
        float[] featureYT = interposeFeaturePoint(target);//n+1个特征点

        FeaturePoint feature = new FeaturePoint(featureYT);
        int[] mostLike = null;
        double minDiff = Integer.MAX_VALUE;
        for (int from = 0; from < n - m + 1; from++) {
            double[] diff = {0.0};
            int[] tmp;
            if (checkSlope(target, origination, from)) {
                tmp = resemble(feature, target, origination, from, diff, resembleParts);
                if (tmp != null && tmp[0] != -1 && diff[0] < minDiff) {
                    mostLike = tmp;
                    minDiff = diff[0];
                }
            }
        }
        return mostLike;
    }

    private static boolean checkSlope(PointGroup target, PointGroup origination, int from) {
        ArrayList<double[]> et = target.regressionFeature.equations;
        ArrayList<double[]> eo = origination.regressionFeature.equations;
        int n = target.regressionFeature.equations.size();
        for (int i = 0; i < n; i++) {
            double kt = et.get(i)[0];
            double ko = eo.get(i + from)[0];

            //不同的增长情况
            if (kt != 0) {
//                System.out.println(kt * ko);
                if (kt * ko <= 0) return false;
            } else if (ko != 0) return false;

        }
        return true;
    }

    private static float[] interposeFeaturePoint(PointGroup target) {
        ArrayList<Integer> pt = target.regressionFeature.parts;
        int n = pt.size();//target有n条回归方程
        float[] featureYT = new float[n + 1];//n+1个特征点
        featureYT[0] = target.yPoints.get(0);
        for (int i = 0; i < n; i++) featureYT[i + 1] = target.yPoints.get(pt.get(i) - 1);
        return featureYT;
    }

    private static int[] resemble(FeaturePoint targetFeature, PointGroup target, PointGroup origination, int fromWhich, double[] diff, ArrayList<int[]> resembleParts) {

            int n = target.regressionFeature.equations.size();
            if (n == 0)
                return null;

            int a = origination.regressionFeature.parts.get(fromWhich);
            int b = -1;
            int k = -1;
            if (n > 1) {
                b = origination.regressionFeature.parts.get(fromWhich + n - 1);
                k = origination.regressionFeature.parts.get(fromWhich + n - 2);
            }
            int mostLikeFrom = -1;
            int mostLikeTo = -1;
            double minDiff = Integer.MAX_VALUE;
            for (int i = fromWhich > 0 ? origination.regressionFeature.parts.get(fromWhich - 1) - 1 : 0
                 ; i < a - 1; i++) {
                if (n > 1) {
                    for (int j = k; j < b; j++) {//一个点不能形成回归方程
                        double[] d = {0.0};
                        if (resemble(targetFeature, target, origination, fromWhich, i, j, d, resembleParts) && d[0] < minDiff) {
                            minDiff = d[0];
                            mostLikeFrom = i;
                            mostLikeTo = j;
                        }
                    }
                } else {
                    double[] d = {0.0};
                    if (resemble(targetFeature, target, origination, fromWhich, i, a - 1, d, resembleParts) && d[0] < minDiff) {
                        minDiff = d[0];
                        mostLikeFrom = i;
                        mostLikeTo = a;
                    }
                }
            }
            diff[0] = minDiff;

            return new int[]{mostLikeFrom, mostLikeTo};


    }

    //将i和j做为特征点
    private static boolean resemble(FeaturePoint targetFeature, PointGroup target, PointGroup origination
            , final int fromWhich, int i, int j, double[] D, ArrayList<int[]> resembleParts) {

        ArrayList<Integer> pt = target.regressionFeature.parts;
        ArrayList<Integer> po = origination.regressionFeature.parts;
        ArrayList<double[]> et = target.regressionFeature.equations;
        ArrayList<double[]> eo = origination.regressionFeature.equations;
        int n = pt.size();//target有n条回归方程

        float[] featureYO = new float[n + 1];
        featureYO[0] = origination.yPoints.get(i);
        for (int x = 0; x < n - 1; x++) featureYO[x + 1] = origination.yPoints.get(po.get(x + fromWhich) - 1);
        featureYO[n] = origination.yPoints.get(j);

        FeaturePoint originationFeature = new FeaturePoint(featureYO);
        if (!targetFeature.equals(originationFeature)) return false;

        double diffSum = 0;
        for (int x = 0; x < n; x++) {
            double kt = et.get(x)[0];
            double ko;
            if (x == 0) {
                ko = calLinearRegressionK(origination, i, po.get(fromWhich));
            } else if (x == n - 1) {
                ko = calLinearRegressionK(origination, po.get(fromWhich + n - 2) - 1, j + 1);
            } else {
                ko = eo.get(fromWhich + x)[0];
            }
            double thetaT = Math.atan(kt);
            double thetaO = Math.atan(ko);
            double diff = Math.abs(Math.toDegrees(thetaT) - Math.toDegrees(thetaO));
            if (diff > StaticConstant.c) //斜率相差过大认为不相似
                return false;
            diffSum += diff;
        }
        D[0] = diffSum;
        if (resembleParts != null) resembleParts.add(new int[]{i, j + 1});
        if (sResembleParts != null) sResembleParts.add(new ResemblePart(origination, new int[]{i, j + 1}, diffSum));

        return true;
//        return diffSum <= c0 * n;
    }


    private static double calLinearRegressionK(PointGroup group, int from, int to) {

        return LinearRegressionFeature.linearRegression(group.xPoints, group.yPoints, from, to)[0];
    }


    private static int[] getSortArray(float[] featurePoint) {
        int[] array = new int[featurePoint.length];
        Arrays.setAll(array, i -> i);
        mergeSort(featurePoint, 0, featurePoint.length, array);
        return array;
    }

    public static void mergeSort(float[] ar, int fromIndex, int toIndex, int[] br) {
        int max_per_n = 01;
        while (max_per_n < toIndex - fromIndex) {
            int i = fromIndex;
            while (i < toIndex) {
                merge(ar, i, i += max_per_n, (i += max_per_n) > toIndex ? toIndex : i, br);
                if (i + max_per_n >= toIndex) {
                    break;
                }
            }
            max_per_n <<= 1;
        }
    }

    public static void merge(float[] ar, int fromIndex0, int fromIndex2, int toIndex, int[] br) {
        float[] temp = new float[toIndex - fromIndex0];
        int[] tbr = new int[toIndex - fromIndex0];
        int k = 0;
        int j = fromIndex2;
        while (fromIndex0 < fromIndex2 && j < toIndex) {
            if (ar[fromIndex0] > ar[j]) {
                temp[k] = ar[j];
                tbr[k] = br[j];
                j++;
            } else {
                temp[k] = ar[fromIndex0];
                tbr[k] = br[fromIndex0];
                fromIndex0++;
            }
            k++;
        }
        if (fromIndex0 < fromIndex2) {
            System.arraycopy(ar, fromIndex0, ar, toIndex - fromIndex2 + fromIndex0, fromIndex2 - fromIndex0);
            System.arraycopy(br, fromIndex0, br, toIndex - fromIndex2 + fromIndex0, fromIndex2 - fromIndex0);
        }
        System.arraycopy(temp, 0, ar, toIndex - temp.length, k);
        System.arraycopy(tbr, 0, br, toIndex - tbr.length, k);
    }

    public static void main(String[] args) {

//        Random random = new Random();
//        Arrays.setAll(ar, operand -> random.nextInt(50));
//        Arrays.setAll(br, operand -> ar[operand] * 2);
//        ar[9] += 10;

//        ar = new int[]{225, 112, 112, 156};
//        br = new int[]{240, 129, 129, 274};
//        int[] cr = getSortArray(ar);
//        int[] dr = getSortArray(br);
//        System.out.println(Arrays.toString(cr));
//        System.out.println(Arrays.toString(dr));
//        System.out.println(Arrays.toString(ar));
//        System.out.println(Arrays.toString(br));
//        System.out.println(Arrays.equals(cr, dr));
        float[] ar = {1, 2, 1.5f, 4, 1.5f, 2, 1,};
        FeaturePoint featurePoint = new FeaturePoint(ar);
        featurePoint.calEquivalentIndex();
        System.out.println(Arrays.toString(featurePoint.equivalentIndex));
        System.out.println(Arrays.toString(featurePoint.array));
        System.out.println(Arrays.toString(featurePoint.featureYT));
    }


}
