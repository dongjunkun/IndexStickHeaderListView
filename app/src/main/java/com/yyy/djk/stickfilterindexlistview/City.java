package com.yyy.djk.stickfilterindexlistview;

/**
 * Created by dongjunkun on 2015/7/4.
 */
public class City {

    private int proId;
    private int cityId;
    private String name;
    private int citySort;

    private String sortLetter;

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCitySort(int citySort) {
        this.citySort = citySort;
    }

    public int getProId() {
        return proId;
    }

    public int getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public int getCitySort() {
        return citySort;
    }
}
