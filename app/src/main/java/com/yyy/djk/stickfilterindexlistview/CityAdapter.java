package com.yyy.djk.stickfilterindexlistview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by dongjunkun on 2015/7/4.
 */
public class CityAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private List<City> cities;

    public CityAdapter(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeadViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (HeadViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_layout, null);
            viewHolder = new HeadViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        //必须加""连接，否则会出现空指针异常，原因是setText方法传入整型参数会被解析成资源类型
        viewHolder.mHeadText.setText(""+cities.get(position).getSortLetter().charAt(0));
        return convertView;
    }

    /**
     * 更新列表
     * @param cities
     */
    public void updateList(List<City> cities){
        this.cities = cities;
        notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int i) {
        return cities.get(i).getSortLetter().charAt(0);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.mCity.setText(cities.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.city) TextView mCity;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class HeadViewHolder {
        @InjectView(R.id.headText) TextView mHeadText;

        HeadViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    //根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = cities.get(i).getSortLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
