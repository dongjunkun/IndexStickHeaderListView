package com.yyy.djk.stickfilterindexlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {

    //触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private TextView mTextDialog;

    //设置选中
    private int choose = -1;

    //设置画笔
    private Paint paint = new Paint();

    //构造函数
    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置dialog显示字母
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**
     * 重写onDraw方法
     * <p/>
     * 获取焦点 改变背景颜色
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / letters.length;// 获取每一个字母的高度

        for (int i = 0; i < letters.length; i++) {
            paint.setColor(Color.rgb(33, 65, 98));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(spToPx(12));
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#9C27B0"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letters[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        //点击获取Y坐标
        final float y = event.getY();
        final int oldChoose = choose;

        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        //点击y坐标所占总高度的比例*letters数组的长度就等于点击b中的个数.
        int c = (int) (y / getHeight() * letters.length);

        switch (action) {

            case MotionEvent.ACTION_UP:

                setBackgroundDrawable(new ColorDrawable(0x00000000));

                choose = -1;
                //
                invalidate();

                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }

                break;

            default:


                setBackgroundResource(R.drawable.sidebar_bg);

                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {

                        if (listener != null) {
                            listener.onTouchingLetterChanged(letters[c]);
                        }

                        //dialog显示选中的字母
                        if (mTextDialog != null) {
                            mTextDialog.setText(letters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);

                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 对话公开的方法
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }


    /**
     * 定义接口  触摸自定义View字母时 改变监听
     */

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    public int spToPx(int px) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getResources().getDisplayMetrics()));
    }
}
