package others2017321.io;

/**
 * Created by L on 2017/3/27.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        CSVFileUtil csvFileUtil = new CSVFileUtil("c://data/1.csv");
        System.out.println(csvFileUtil.readLine());
    }
}
