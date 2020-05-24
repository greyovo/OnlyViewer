package onlyviewer.home.java.model;

import java.util.Comparator;

/**
 * @author Kevin
 * @since 2020/4/30
 **/

public class SortBySize implements Comparator<ImageModel> {
    @Override
    public int compare(ImageModel o1, ImageModel o2) {
        return Long.compare(o1.getFileLength(), o2.getFileLength());
    }
}
