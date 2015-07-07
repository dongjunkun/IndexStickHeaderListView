package com.yyy.djk.stickfilterindexlistview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.clearEditText) ClearEditText mClearEditText;
    @InjectView(R.id.stickListHeadersListView) StickyListHeadersListView mStickListHeadersListView;
    @InjectView(R.id.dialog) TextView mDialog;
    @InjectView(R.id.sideBar) SideBar mSideBar;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    private CityAdapter cityAdapter;

    //汉子转换成拼音的类
    private CharacterParser characterParser;
    //数据源
    private List<City> cities = new ArrayList<>();
    //根据拼音来排列listView里的数据类
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initView();
        new MyAsyncTask(this).execute("city.json");
    }

    private void initView() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = cityAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mStickListHeadersListView.setSelection(position);
                }
            }
        });
        //mStickListHeadersListView的点击事件
        mStickListHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ((City) cityAdapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        cityAdapter = new CityAdapter(cities);
        mStickListHeadersListView.setAdapter(cityAdapter);
        /**
         * 输入框搜索过滤
         */

        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 输入框过滤更新列表
     *
     * @param s
     */
    private void filterData(String s) {
        List<City> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = cities;
        } else {
            filterDataList.clear();
            for (City sortModel : cities) {
                String name = sortModel.getName();
                if (name.indexOf(s.toString()) != -1 || characterParser.getSelling(name).startsWith(s.toString())) {
                    filterDataList.add(sortModel);
                }
            }
        }

        //根据a~z进行排序
        Collections.sort(filterDataList, pinyinComparator);
        cityAdapter.updateList(filterDataList);
    }

    class MyAsyncTask extends AsyncTask<Object, Integer, String> {
        private Context context;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... params) {
            /**
             * 获取城市数据
             */
            try {
                InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open((String) params[0]));
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line = "";
                String result = "";
                while ((line = bufReader.readLine()) != null)
                    result += line;
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("")) {
                //JSON数据转List对象
                cities = new Gson().fromJson(s, new TypeToken<ArrayList<City>>() {
                }.getType());
                for (City city : cities) {
                    String pinyin = characterParser.getSelling(city.getName());
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    city.setSortLetter(sortString);
                }
                //根据a-z 排序
                Collections.sort(cities, pinyinComparator);
                mProgressBar.setVisibility(View.GONE);
                cityAdapter.updateList(cities);
            }
        }
    }

}
