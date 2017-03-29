package others2017321.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by L on 2017/3/27.
 */
public class ReadCSV {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("c://data/1.csv");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
    }
}
