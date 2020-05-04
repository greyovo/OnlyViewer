package home.java.model;

import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjName: OnlyViewer
 * @ClassName: FindImageModel
 * @Author: tudoudaren233
 * @Describe: 对该imagelist下的所有图片进行查找
 */
public class FindImageModel {
    @Getter
    //获得找到的图片数量
    private static int foundNumbers=0;
//       /**
//     * 通过图片名字查找图片（精准匹配）
//     */
//    private ImageModel findPic(ArrayList<ImageModel> imageModelList, String picName) {
//        for (ImageModel im : imageModelList) {
//            if (im.getImageName().equals(picName)) {
//                foundNumbers+=1;
//                return im;
//            }
//        }
//        return null;
//    }



    //实现精准查找并返回一个结果，需要文件的全称包括后缀
    public static ImageModel  findImage_exact(String name,ArrayList<ImageModel> imageModelList){
        //对大小写不敏感，若敏感，去除CASE_INSENSITIVE
        Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        for(ImageModel im : imageModelList){
            Matcher matcher = pattern.matcher(im.getImageName());
            if(matcher.matches()){
                foundNumbers+=1;
                return im;
            }
        }
        //未找到
        return null;
    }

    //实现模糊查找并返回一个图片列表供显示，建议使用此方法
    public static ArrayList<ImageModel>  findImage_nonexact(String name,ArrayList<ImageModel> imageModelList){
        //对大小写不敏感，若敏感，去除CASE_INSENSITIVE
        Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        ArrayList<ImageModel> result = new ArrayList<ImageModel>();
        for(ImageModel im : imageModelList){
            Matcher matcher = pattern.matcher(im.getImageName());
            if(matcher.find()){
                foundNumbers++;
                result.add(im);
            }
        }
        //未找到
        return result;
    }

    @Test
    public void Test() throws IOException {

            String path = "F:\\手机中的照片和视频\\2017-2019老照片";
            ArrayList<ImageModel> ilist = ImageListModel.initImgList(path);
            long timef = System.currentTimeMillis();
            String name = "img";
            findImage_nonexact(name, ilist);
            long timel = System.currentTimeMillis();
            System.out.printf("查找耗时 %d ms\n", timel - timef);
            System.out.println("共有"+ilist.size()+"张照片");
            System.out.println("共找到"+foundNumbers+"张照片");


    }

}
