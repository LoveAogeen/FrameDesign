package com.laitianliang.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 頼天亮 on 5/12/2016.
 */
public class TabItem extends View {

    private Rect mBoundText;
    private Paint mTextPaintNormal;
    private Paint mTextPaintSelect;
    private Paint mIconPaintNormal;
    private Paint mIconPaintSelect;
    private int mTextSize=12;
    private int mTextColorNormal = 0xff777777;
    private int mTextColorSelect = 0xff45c01a;
    private String mTextValue;
    private Bitmap mIconNormal;
    private Bitmap mIconSelect;
    private int mViewWidth,mViewHeight;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initText();
    }

    private void initView() {
        mBoundText=new Rect();
    }

    private void initText() {
        /*A: draw 主要指用铅笔、钢笔、粉笔、炭精等画图、画画或画线(侧重线条)
        B: paint 指用颜料绘画, 如水彩画、油画等画画(侧重着色)。*/
        mTextPaintNormal=new Paint();
//        mTextPaintNormal.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                mTextSize,getResources().getDisplayMetrics()));
//        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAntiAlias(true);//抗锯齿
//        mTextPaintNormal.setAlpha(0xff);

        mTextPaintSelect=new Paint();
//        mTextPaintSelect.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                mTextSize,getResources().getDisplayMetrics()));
//        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAntiAlias(true);
//        mTextPaintSelect.setAlpha(0);

        mIconPaintSelect=new Paint(Paint.ANTI_ALIAS_FLAG);//ANTI_ALIAS_FLAG是抗锯齿
        mIconPaintSelect.setAlpha(0);

        mIconPaintNormal=new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintNormal.setAlpha(0xff);
    }
    private void measureText(){
        //“|”表示的是或运算，即两个二进制数同位中，只要有一个为1则结果为1，若两个都为1其结果也为1
        /*实际上在数值类型转换时，只有当遇到负数时才会出现问题，
        根本原因就是Java中的负数不是采用直观的方式进行编码，
        而是采用“2的补码”方式，这样的好处是加法和减法操作可以同时使用加法电路完成，
        但是在开发时却会遇到很多奇怪的问题，例如(byte)128的结果是-128，即一个大的正数，
        截断后却变成了负数。3.2节中引用了一些转型规则，应用这些规则可以很容地解决常见的转型问题。*/
        //个人理解，getTextBounds到mBoundText中
        mTextPaintNormal.getTextBounds(mTextValue,0,mTextValue.length(),mBoundText);
    }

    //传递测量到用户定义的模式和尺寸，根据模式进行尺寸处理，决定View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthModle=MeasureSpec.getMode(widthMeasureSpec);
        int heightModle=MeasureSpec.getMode(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width=0,height=0;
        measureText();
        int contentWidth=Math.max(mBoundText.width(),mIconNormal.getWidth());
        //desired：希望
        int desiredWidth=getPaddingLeft()+getPaddingRight()+contentWidth;
        switch (widthModle){
            //MeasureSpec.AT_MOST 是子布局可以根据自己的大小选择任意大小的模式
            //不太懂
            case MeasureSpec.AT_MOST:
                width=Math.min(widthSize,desiredWidth);
                break;
            case MeasureSpec.EXACTLY:
                width=widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width=desiredWidth;
                break;
        }
        int contentHeight=mBoundText.height()+mIconNormal.getHeight();
        int desiredHeight=getPaddingTop()+getPaddingBottom()+contentHeight;
        switch (heightModle){
            case MeasureSpec.AT_MOST:
                //不太懂
                height=Math.min(heightSize,desiredHeight);
                break;
            case MeasureSpec.EXACTLY:
                height=heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height=contentHeight;
                break;
        }
        setMeasuredDimension(width,height);
        //这里其实getMeasuredWidth()获取的值就是参数width的值
        mViewWidth=getMeasuredWidth();
        mViewHeight=getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawText(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        //mIconNormal的值是setIconText方法传进来的
        int left=(mViewWidth-mIconNormal.getWidth())/2;
        int top=(mViewHeight-mIconNormal.getHeight()-mBoundText.height())/2;
        canvas.drawBitmap(mIconNormal, left, top, mIconPaintNormal);
        canvas.drawBitmap(mIconSelect, left, top, mIconPaintSelect);
    }

    private void drawText(Canvas canvas) {
        float x=(mViewWidth-mBoundText.width())/2.0f;
        float y=(mViewHeight+mIconNormal.getHeight()+mBoundText.height())/2.0f;
        canvas.drawText(mTextValue, x, y, mTextPaintNormal);
        canvas.drawText(mTextValue, x, y, mTextPaintSelect);
    }

    public void setTextValue(String textValue) {
        this.mTextValue = textValue;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextPaintNormal.setTextSize(textSize);
        mTextPaintSelect.setTextSize(textSize);
    }

    public void setTextColorSelect(int mTextColorSelect){
        this.mTextColorSelect=mTextColorSelect;
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAlpha(0xff);
    }

    public void setTextColorNormal(int mTextColorNormal) {
        this.mTextColorNormal = mTextColorNormal;
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAlpha(0xff);
    }
    //设置图标和文字
    public void setIconText(int[] iconSelId,String textVelue){
        this.mIconSelect= BitmapFactory.decodeResource(getResources(),iconSelId[0]);
        this.mIconNormal=BitmapFactory.decodeResource(getResources(),iconSelId[1]);
        this.mTextValue=textVelue;
    }

    public void setTabAlpha(float alpha){
        int paintAlpha=(int)(alpha*255);
        mIconPaintSelect.setAlpha(paintAlpha);
        mIconPaintNormal.setAlpha(255-paintAlpha);
        mTextPaintSelect.setAlpha(paintAlpha);
        mTextPaintNormal.setAlpha(255-paintAlpha);
        invalidate();//清屏重绘
    }
}



/**
 * MeasureSpec：
 *因为MeasureSpec类很小，而且设计的很巧妙，所以我贴出了全部的源码并进行了详细的标注。（ 掌握MeasureSpec的机制后会对整个Measure方法有更深刻的理解。）
 * MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求
 * MeasureSpec由size和mode组成。
 * 三种Mode：
 * 1.UNSPECIFIED
 * 父不没有对子施加任何约束，子可以是任意大小（也就是未指定）
 * (UNSPECIFIED在源码中的处理和EXACTLY一样。当View的宽高值设置为0的时候或者没有设置宽高时，模式为UNSPECIFIED
 * 2.EXACTLY
 * 父决定子的确切大小，子被限定在给定的边界里，忽略本身想要的大小。
 * (当设置width或height为match_parent时，模式为EXACTLY，因为子view会占据剩余容器的空间，所以它大小是确定的)
 * 3.AT_MOST
 * 子最大可以达到的指定大小
 * (当设置为wrap_content时，模式为AT_MOST, 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸)
 *
 * MeasureSpecs使用了二进制去减少对象的分配。
 */
/*
public class MeasureSpec {
    // 进位大小为2的30次方(int的大小为32位，所以进位30位就是要使用int的最高位和倒数第二位也就是32和31位做标志位)
    private static final int MODE_SHIFT = 30;

    // 运算遮罩，0x3为16进制，10进制为3，二进制为11。3向左进位30，就是11 00000000000(11后跟30个0)
    // (遮罩的作用是用1标注需要的值，0标注不要的值。因为1与任何数做与运算都得任何数，0与任何数做与运算都得0）
    private static final int MODE_MASK  = 0x3 << MODE_SHIFT;

    // 0向左进位30，就是00 00000000000(00后跟30个0)
    public static final int UNSPECIFIED = 0 << MODE_SHIFT;
    // 1向左进位30，就是01 00000000000(01后跟30个0)
    public static final int EXACTLY     = 1 << MODE_SHIFT;
    // 2向左进位30，就是10 00000000000(10后跟30个0)
    public static final int AT_MOST     = 2 << MODE_SHIFT;

    */
/**
     * 根据提供的size和mode得到一个详细的测量结果
     *//*

    // measureSpec = size + mode；	(注意：二进制的加法，不是10进制的加法！)
    // 这里设计的目的就是使用一个32位的二进制数，32和31位代表了mode的值，后30位代表size的值
    // 例如size=100(4)，mode=AT_MOST，则measureSpec=100+10000...00=10000..00100
    public static int makeMeasureSpec(int size, int mode) {
        return size + mode;
    }

    */
/**
     * 通过详细测量结果获得mode
     *//*

    // mode = measureSpec & MODE_MASK;
    // MODE_MASK = 11 00000000000(11后跟30个0)，原理是用MODE_MASK后30位的0替换掉measureSpec后30位中的1,再保留32和31位的mode值。
    // 例如10 00..00100 & 11 00..00(11后跟30个0) = 10 00..00(AT_MOST)，这样就得到了mode的值
    public static int getMode(int measureSpec) {
        return (measureSpec & MODE_MASK);
    }

    */
/**
     * 通过详细测量结果获得size
     *//*

    // size = measureSpec & ~MODE_MASK;
    // 原理同上，不过这次是将MODE_MASK取反，也就是变成了00 111111(00后跟30个1)，将32,31替换成0也就是去掉mode，保留后30位的size
    public static int getSize(int measureSpec) {
        return (measureSpec & ~MODE_MASK);
    }

    */
/**
     * 重写的toString方法，打印mode和size的信息，这里省略
     *//*

    public static String toString(int measureSpec) {
        return null;
    }
}*/
