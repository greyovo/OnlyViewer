package home.java.model;

import java.util.Comparator;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SortByDate
 * @Author: Kevin
 * @Time:2020/4/30 11:52
 * @Describe: TODO
 **/

public class SortByDate implements Comparator<ImageModel> {
    @Override
    public int compare(ImageModel o1, ImageModel o2) {
        return Long.compare(o1.getImageLastModified(), o2.getImageLastModified());
    }
}
