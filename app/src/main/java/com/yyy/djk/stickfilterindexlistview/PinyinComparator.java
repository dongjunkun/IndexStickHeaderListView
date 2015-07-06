package com.yyy.djk.stickfilterindexlistview;



import java.util.Comparator;

/**
 * Created by dongjunkun on 2015/7/4.
 *
 * 根据拼音来排列HeadListView中的数据
 */
public class PinyinComparator implements Comparator<City> {
    @Override
    public int compare(City c1, City c2) {

        if (c1.getSortLetter().equals("@") || c2.getSortLetter().equals("#")) {
            return -1;
        } else if (c1.getSortLetter().equals("#") || c2.getSortLetter().equals("@")) {
            return 1;
        } else {
            return c1.getSortLetter().compareTo(c2.getSortLetter());
        }
    }
}
