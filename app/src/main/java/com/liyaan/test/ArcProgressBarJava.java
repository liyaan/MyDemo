package com.liyaan.test;

import android.animation.ValueAnimator;
        import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
        import android.graphics.RectF;
        import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class ArcProgressBarJava extends View {

    private static final String TAG = "ArcProgressBar";
    /**
     * 圆弧的宽度
     */
    private int mStrokeWidth = dp2px(8);
    /**
     * 字体间距
     */
    private int mMargin = dp2px(8);
    /**
     * 圆弧开始的角度
     */
    private float mStartAngle = 135;
    /**
     * 起点角度和终点角度对应的夹角大小
     */
    private float mAngleSize = 270;
    /**
     * 圆弧背景颜色
     */
    private int mArcBgColor = Color.YELLOW;


    /**
     * 最大的进度，用于计算进度与夹角的比例
     */
    public float getMaxProgress() {
        return mMaxProgress;
    }

    private float mMaxProgress = 500;
    /**
     * 当前进度对应的起点角度到当前进度角度夹角的大小
     */
    private float mCurrentAngleSize = 0;
    /**
     * 当前进度
     */
    private float mCurrentProgress = 0;
    /**
     * 动画的执行时长
     */
    private long mDuration = 3000;
    /**
     * 进度圆弧的颜色
     */
    private int mProgressColor = Color.RED;
    /**
     * 第一行文本
     */
    private String mFirstText = "42";
    /**
     * 第一行文本的颜色
     */
    private int mFirstTextColor = Color.RED;
    /**
     * 第一行文本的字体大小
     */
    private float mFirstTextSize = 56f;
    /**
     * 第二行文本
     */
    private String mSecondText = "优";
    /**
     * 第二行文本的颜色
     */
    private int mSecondTextColor = Color.RED;
    /**
     * 第二行文本的字体大小
     */
    private float mSecondTextSize = 56f;

    private RectF mRectFTextArc;
    private Path mPath;
    private Rect mRectText;


    private String[] mTexts;

    public void setTexts(String[] mTexts) {
        this.size = mTexts.length - 1;
        this.mTexts = mTexts;
    }

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private int mBackgroundColor;
    private int[] mBgColors;
    private int mRadius;
    private int size;
    private float mLength2;

    public ArcProgressBarJava(Context context) {
        this(context, null);
    }

    public ArcProgressBarJava(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ArcProgressBarJava(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectFTextArc = new RectF();
        mPath = new Path();
        mRectText = new Rect();

        mTexts = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        size = mTexts.length - 1;
        mBgColors = new int[]{ContextCompat.getColor(getContext(), R.color.color_red),
                ContextCompat.getColor(getContext(), R.color.color_orange),
                ContextCompat.getColor(getContext(), R.color.color_yellow),
                ContextCompat.getColor(getContext(), R.color.color_green),
                ContextCompat.getColor(getContext(), R.color.color_blue)};
        mBackgroundColor = mBgColors[0];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);


        int width = resolveSize(dp2px(220), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width - dp2px(30));

        mCenterX = mCenterY = getMeasuredWidth() / 2f;

        mLength2 = mPadding + mStrokeWidth + mMargin;
        mPaint.setTextSize(sp2px(15));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFTextArc.set(
                mLength2 + mRectText.height(),
                mLength2 + mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height()
        );
    }

    /**
     * 设置初始化的参数
     *
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        mMaxProgress = array.getFloat(R.styleable.ArcProgressBar_arc_max_progress, 500f);
        mArcBgColor = array.getColor(R.styleable.ArcProgressBar_arc_bg_color, Color.YELLOW);
        mStrokeWidth = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_stroke_width, 12f));
        mMargin = dp2px(array.getDimension(R.styleable.ArcProgressBar_text_margin_width, 8f));
        mCurrentProgress = array.getFloat(R.styleable.ArcProgressBar_arc_progress, 300f);
        mProgressColor = array.getColor(R.styleable.ArcProgressBar_arc_progress_color, Color.RED);
        mFirstText = array.getString(R.styleable.ArcProgressBar_arc_first_text);
        mFirstTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_first_text_size, 20f));
        mFirstTextColor = array.getColor(R.styleable.ArcProgressBar_arc_first_text_color, Color.RED);
        mSecondText = array.getString(R.styleable.ArcProgressBar_arc_second_text);
        mSecondTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_second_text_size, 20f));
        mSecondTextColor = array.getColor(R.styleable.ArcProgressBar_arc_second_text_color, Color.RED);
        mAngleSize = array.getFloat(R.styleable.ArcProgressBar_arc_angle_size, 270f);
        mStartAngle = array.getFloat(R.styleable.ArcProgressBar_arc_start_angle, 135f);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        RectF rectF = new RectF();
        rectF.left = mStrokeWidth;
        rectF.top = mStrokeWidth;
        rectF.right = centerX * 2 - mStrokeWidth;
        rectF.bottom = centerX * 2 - mStrokeWidth;
        //画最外层的圆弧
        drawArcBg(canvas, rectF);
        //画进度
        drawArcProgress(canvas, rectF);
        //绘制第一级文本
//        drawFirstText(canvas, centerX);
//        //绘制第二级文本
//        drawSecondText(canvas, centerX);

    }


    /**
     * 画最开始的圆弧
     *
     * @param canvas
     * @param rectF
     */
    private void drawArcBg(Canvas canvas, RectF rectF) {
        Paint mPaint = new Paint();
        //画笔的填充样式，Paint.Style.FILL 填充内部;Paint.Style.FILL_AND_STROKE 填充内部和描边;Paint.Style.STROKE 描边
        mPaint.setStyle(Paint.Style.STROKE);
        //圆弧的宽度
        mPaint.setStrokeWidth(mStrokeWidth);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //画笔的颜色
        mPaint.setColor(mArcBgColor);
        //画笔的样式 Paint.Cap.Round 圆形,Cap.SQUARE 方形
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //开始画圆弧
        canvas.drawArc(rectF, mStartAngle, mAngleSize, false, mPaint);


        /**
         * 画长刻度读数
         * 添加一个圆弧path，文字沿着path绘制
         */
        mPaint.setTextSize(sp2px(10));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(160);
        mPaint.setColor(mBackgroundColor);
        for (int i = 0; i < mTexts.length; i++) {
            mPaint.getTextBounds(mTexts[i], 0, mTexts[i].length(), mRectText);
            // 粗略把文字的宽度视为圆心角2*θ对应的弧长，利用弧长公式得到θ，下面用于修正角度
            //PI = 3.14159265358979323846 mRadius半径 mLength2字距离圆弧的距离 mRectText文本的块
            float θ = (float) (180 * mRectText.width() / 2 /
                    (Math.PI * (mRadius - mLength2 - mRectText.height())));

            mPath.reset();
            mPath.addArc(
                    mRectFTextArc,
                    mStartAngle + i * (mAngleSize / size) - θ, // 正起始角度减去θ使文字居中对准长刻度
                    mAngleSize
            );
            canvas.drawTextOnPath(mTexts[i], mPath, 0, 0, mPaint);
        }
    }

    /**
     * 画进度的圆弧
     *
     * @param canvas
     * @param rectF
     */
    private void drawArcProgress(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, mStartAngle, mCurrentAngleSize, false, paint);
    }


    /**
     * 绘制第一级文字
     *
     * @param canvas  画笔
     * @param centerX 位置
     */
    private void drawFirstText(Canvas canvas, float centerX) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mFirstTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mFirstTextSize);
        Rect firstTextBounds = new Rect();
        paint.getTextBounds(mFirstText, 0, mFirstText.length(), firstTextBounds);
        canvas.drawText(mFirstText, centerX, firstTextBounds.height() / 2 + getHeight() * 2 / 5, paint);
    }

    /**
     * 绘制第二级文本
     *
     * @param canvas  画笔
     * @param centerX 文本
     */
    private void drawSecondText(Canvas canvas, float centerX) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mSecondTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mSecondTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(mSecondText, 0, mSecondText.length(), bounds);
        canvas.drawText(mSecondText, centerX, getHeight() / 2 + bounds.height() / 2 +
                getFontHeight(mSecondText, mSecondTextSize), paint);
    }

    /**
     * 设置最大的进度
     *
     * @param progress
     */
    public void setMaxProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0 ");
        }
        mMaxProgress = progress;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0");
        }
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
        float size = mCurrentProgress / mMaxProgress;
        mCurrentAngleSize = (int) (mAngleSize * size);
        setAnimator(0, mCurrentAngleSize);
    }

    /**
     * 设置进度圆弧的颜色
     *
     * @param color
     */
    public void setProgressColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color can no be 0");
        }
        mProgressColor = color;
    }

    /**
     * 设置圆弧的颜色
     *
     * @param color
     */
    public void setArcBgColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color can no be 0");
        }
        mArcBgColor = color;
    }

    /**
     * 设置圆弧的宽度
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        if (strokeWidth < 0) {
            throw new IllegalArgumentException("strokeWidth value can not be less than 0");
        }
        mStrokeWidth = dp2px(strokeWidth);
    }

    /**
     * 设置动画的执行时长
     *
     * @param duration
     */
    public void setAnimatorDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration value can not be less than 0");
        }
        mDuration = duration;
    }

    /**
     * 设置第一行文本
     *
     * @param text
     */
    public void setFirstText(String text) {
        mFirstText = text;
    }

    /**
     * 设置第一行文本的颜色
     *
     * @param color
     */
    public void setFirstTextColor(int color) {
        if (color <= 0) {
            throw new IllegalArgumentException("Color value can not be less than 0");
        }
        mFirstTextColor = color;
    }

    /**
     * 设置第一行文本的大小
     *
     * @param textSize
     */
    public void setFirstTextSize(float textSize) {
        if (textSize <= 0) {
            throw new IllegalArgumentException("textSize can not be less than 0");
        }
        mFirstTextSize = textSize;
    }

    /**
     * 设置第二行文本
     *
     * @param text
     */
    public void setSecondText(String text) {
        mSecondText = text;
    }

    /**
     * 设置第二行文本的颜色
     *
     * @param color
     */
    public void setSecondTextColor(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color value can not be less than 0");
        }
        mSecondTextColor = color;
    }

    /**
     * 设置第二行文本的大小
     *
     * @param textSize
     */
    public void setSecondTextSize(float textSize) {
        if (textSize <= 0) {
            throw new IllegalArgumentException("textSize can not be less than 0");
        }
        mSecondTextSize = textSize;
    }

    /**
     * 设置圆弧开始的角度
     *
     * @param startAngle
     */
    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    /**
     * 设置圆弧的起始角度到终点角度的大小
     *
     * @param angleSize
     */
    public void setAngleSize(int angleSize) {
        mAngleSize = angleSize;
    }

    /**
     * dp转成px
     *
     * @param dp
     * @return
     */
    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 设置动画
     *
     * @param start  开始位置
     * @param target 结束位置
     */
    private void setAnimator(float start, float target) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, target);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setTarget(mCurrentAngleSize);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            mCurrentAngleSize = (float) valueAnimator1.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();
    }

    /**
     * 测量字体的高度
     *
     * @param textStr
     * @param fontSize
     * @return
     */
    private float getFontHeight(String textStr, float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds = new Rect();
        paint.getTextBounds(textStr, 0, textStr.length(), bounds);
        return bounds.height();
    }

    public void setMargin(int margin) {
        if (mMargin < 0) {
            throw new IllegalArgumentException("margin value can not be less than 0");
        }
        this.mMargin = mMargin;
    }
}

