package others2017321.main;

import others2017321.algorithms.Search;
import others2017321.constant.StaticConstant;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * Created by pc on 2017/3/24.
 */
public class Used {

    //误差修改看StaticConstant类
    //在parent文件中搜索child文件
    public static void main(String[] args) throws FileNotFoundException {
        File child = new File(StaticConstant.parent + "0.txt");
        File parent = new File(StaticConstant.parent + "7.txt");
        boolean result = Search.search(child, parent);
        System.out.println(result);
    }

}
