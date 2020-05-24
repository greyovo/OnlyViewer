package onlyviewer.home.java.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SearchImageModel
 * @Author: tudou daren
 * @Describe: 对该imagelist下的所有图片进行查找
 */
public class SearchImageModel {
    @Getter
    //获得找到的图片数量
    private static int foundNumbers = 0;

    //实现模糊查找并返回一个图片列表供显示，建议使用此方法
    public static ArrayList<ImageModel> fuzzySearch(String name, ArrayList<ImageModel> imageModelList) {
        //对大小写不敏感，若敏感，去除CASE_INSENSITIVE
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        ArrayList<ImageModel> result = new ArrayList<ImageModel>();
        for (ImageModel im : imageModelList) {
            Matcher matcher = pattern.matcher(im.getImageName());
            if (matcher.find()) {
                foundNumbers++;
                result.add(im);
            }
        }
        //未找到
        return result;
    }

    //实现精准查找并返回一个结果，需要文件的全称包括后缀
    public static ImageModel accurateSearch(String name, ArrayList<ImageModel> imageModelList) {
        //对大小写不敏感，若敏感，去除CASE_INSENSITIVE
        Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
        for (ImageModel im : imageModelList) {
            Matcher matcher = pattern.matcher(im.getImageName());
            if (matcher.matches()) {
                foundNumbers += 1;
                return im;
            }
        }
        //未找到
        return null;
    }
}
