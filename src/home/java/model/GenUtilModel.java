package home.java.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    static String getFormatSize(long fileLength){
        String Standardsize = null;
        if (fileLength < KB){
            Standardsize = String.format("%d Byte", fileLength);
        }else if(fileLength < MB){
            Standardsize = String.format("%.0f KB", fileLength/KB);
        }else if (fileLength < GB){
            Standardsize = String.format("%.2f MB", fileLength/MB);
        }else {
            Standardsize = String.format("%.2f GB", fileLength/GB);
        }
        return Standardsize;
    }

    static String getFormatTime(long time){
        Date data = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(data);
    }

    static byte[] getByteByFile(File file){
        try(FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)){
            byte[] bytes = new byte[1024];
            int i;
            while ((i = fis.read(bytes)) != -1) {
                bos.write(bytes, 0, i);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean getFileByByte(byte[] bytes, File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)){
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
