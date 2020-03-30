package model;

/**
 * @ProjName: OnlyViewer
 * @ClassName: GenUtilModel
 * @Author: Kevin
 * @Time:2020/3/30 15:37
 * @Describe: 通用的方法类 仅限包内调用 方法缺省修饰
 **/

public class GenUtilModel {
    private static final double KB = 1024.0;
    private static final double MB = 1024.0*1024.0;
    private static final double GB = 1024.0*1024.0*1024.0;

    static String getStandardSize(long fileLength){
        String Standardsize = null;
        if (fileLength < KB){
            Standardsize = String.format("%d BYTE", fileLength);
        }else if(fileLength < MB){
            Standardsize = String.format("%.0f KB", fileLength/KB);
        }else if (fileLength < GB){
            Standardsize = String.format("%.2f MB", fileLength/MB);
        }else {
            Standardsize = String.format("%.2f GB", fileLength/GB);
        }
        return Standardsize;
    }


}
