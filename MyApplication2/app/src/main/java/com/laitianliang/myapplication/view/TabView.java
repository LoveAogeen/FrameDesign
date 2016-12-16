package com.laitianliang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laitianliang.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 頼天亮 on 5/12/2016.
 */
public class TabView extends LinearLayout implements View.OnClickListener {

    private int mTextSize=12;
    private int mTextColorSelect=0xff45c01a;
    private int mTextColorNormal=0xff777777;
    private int mPadding=10;

    private List<TabItem> mTabItems;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private int mChildSize;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnItemIconTextSelectListener mListener;
    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取存取类型的数组
        TypedArray typedArray=getResources().obtainAttributes(attrs, R.styleable.TabView);
        int N=typedArray.getIndexCount();

        for (int i=0;i<N;i++){
            switch (typedArray.getIndex(i)){
                //tabView_text_size为类型组名_成员名
                case R.styleable.TabView_text_size:
                    //COMPLEX_UNIT_SP:复杂单位为sp,DisplayMetrics对象可以取得屏幕指标
                    mTextSize = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            mTextSize, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabView_text_normal_color:
                    mTextColorNormal=typedArray.getColor(i,mTextColorNormal);
                    break;
                case R.styleable.TabView_text_select_color:
                    mTextColorSelect=typedArray.getColor(i,mTextColorSelect);
                    break;
                case R.styleable.TabView_item_padding:
                    mPadding=(int)typedArray.getDimension(i,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            mPadding,getResources().getDisplayMetrics()));
                    break;
            }
        }
        //回收TypedArray，以便后面重用
        typedArray.recycle();
        mTabItems=new ArrayList<>();
    }

    public void setViewPager(final ViewPager mViewPager){
        //mViewPager从MainActivity传进来
        if (mViewPager==null){
            return;
        }
        this.mViewPager=mViewPager;
        this.mPagerAdapter=mViewPager.getAdapter();
        if (mPagerAdapter==null){
            throw new RuntimeException("在设置TabView的ViewPager时，请先设置ViewPager的PagerAdapter");
        }
        this.mChildSize=this.mPagerAdapter.getCount();
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View leftView;
                View rightView;
                if (positionOffset > 0) {
                    leftView = mViewPager.getChildAt(position);
                    rightView = mViewPager.getChildAt(position + 1);
                    //透明度跟偏移量的关系
                    //设置传进来的ViewPager的透明度
                    leftView.setAlpha(1 - positionOffset);
                    rightView.setAlpha(positionOffset);
                    //设置下部TabItem的透明度
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                    mTabItems.get(position + 1).setTabAlpha(positionOffset);
                } else {
//                    Log.e("mViewPager",mViewPager+"");
                    mViewPager.getChildAt(position).setAlpha(1);
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                }
                //不明含义暂时注释掉
                if (mOnPageChangeListener!=null){
                    mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnPageChangeListener!=null){
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener!=null){
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        if (mPagerAdapter instanceof OnItemIconTextSelectListener){
            mListener= (OnItemIconTextSelectListener) mPagerAdapter;
        }else {
            throw new RuntimeException("让你的PageAdapter实现OnItemIconSelectListener接口");
        }
        initItem();
    }

    //这个方法被使用呢
    /*public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener){
        this.mOnPageChangeListener=mOnPageChangeListener;
    }*/
    private void initItem(){
        for (int i=0;i<mChildSize;i++){
            TabItem tabItem=new TabItem(getContext());
            LayoutParams params=new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
            tabItem.setPadding(mPadding,mPadding,mPadding,mPadding);
            tabItem.setIconText(mListener.onIconSelect(i), mListener.onTextSelect(i));
            tabItem.setTextSize(mTextSize);
            tabItem.setTextColorNormal(mTextColorNormal);
            tabItem.setTextColorSelect(mTextColorSelect);
            tabItem.setLayoutParams(params);
            tabItem.setTag(i);
            tabItem.setOnClickListener(this);
            mTabItems.add(tabItem);
            addView(tabItem);
        }
    }

    @Override
    public void onClick(View v) {
        int position= (Integer) v.getTag();
        if(mViewPager.getCurrentItem()==position){
            return;
        }
        for (TabItem tabItem: mTabItems){
            tabItem.setTabAlpha(0);
        }
        mTabItems.get(position).setTabAlpha(1);
        mViewPager.setCurrentItem(position,false);//第二个参数含义是是否光滑
    }

    public interface OnItemIconTextSelectListener{
        int[] onIconSelect(int position);
        String onTextSelect(int position);
    }
}
