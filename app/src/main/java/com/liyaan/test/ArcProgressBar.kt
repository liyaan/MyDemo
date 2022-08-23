package com.liyaan.test

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View


class ArcProgressBar: View {

    private val TAG = "ArcProgressBar"

    /**
     * 圆弧的宽度
     */
    private var mStrokeWidth: Float = dp2px(8f)

    /**
     * 圆弧开始的角度
     */
    private var mStartAngle = 135f

    /**
     * 起点角度和终点角度对应的夹角大小
     */
    private var mAngleSize = 270f

    /**
     * 圆弧背景颜色
     */
    private var mArcBgColor: Int = Color.YELLOW

    /**
     * 最大的进度，用于计算进度与夹角的比例
     */
    private var mMaxProgress = 500f

    /**
     * 当前进度对应的起点角度到当前进度角度夹角的大小
     */
    private var mCurrentAngleSize = 0f

    /**
     * 当前进度
     */
    private var mCurrentProgress = 0f

    /**
     * 动画的执行时长
     */
    private var mDuration: Long = 3000

    /**
     * 进度圆弧的颜色
     */
    private var mProgressColor: Int = Color.RED

    /**
     * 第一行文本
     */
    private var mFirstText = "42"

    /**
     * 第一行文本的颜色
     */
    private var mFirstTextColor: Int = Color.RED

    /**
     * 第一行文本的字体大小
     */
    private var mFirstTextSize = 56f

    /**
     * 第二行文本
     */
    private var mSecondText = "优"

    /**
     * 第二行文本的颜色
     */
    private var mSecondTextColor: Int = Color.RED

    /**
     * 第二行文本的字体大小
     */
    private var mSecondTextSize = 56f

    private val mPaint by lazy {
         Paint()
    }
    private val mPaintProgress by lazy {
        Paint()
    }
    constructor(context:Context):this(context,null)
    constructor(context:Context, attrs: AttributeSet?):this(context, attrs,0)
    constructor(context:Context, attrs: AttributeSet?,defStyleAttr:Int):super(context, attrs, defStyleAttr){
        initPaint()
        initAttr(context, attrs)
        Log.i(TAG,"constructor初始化")
    }

    private fun initAttr(context: Context,attrs:AttributeSet?){
        val array = context.obtainStyledAttributes(attrs,R.styleable.ArcProgressBar)
        mMaxProgress = array.getFloat(R.styleable.ArcProgressBar_arc_max_progress,500f)
        mArcBgColor = array.getColor(R.styleable.ArcProgressBar_arc_bg_color,Color.YELLOW)
        mStrokeWidth = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_stroke_width, 12f))
        mCurrentProgress = array.getFloat(R.styleable.ArcProgressBar_arc_progress, 300f)
        mProgressColor = array.getColor(R.styleable.ArcProgressBar_arc_progress_color, Color.RED)
        mFirstText = array.getString(R.styleable.ArcProgressBar_arc_first_text).toString()
        mFirstTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_first_text_size, 20f))
        mFirstTextColor = array.getColor(R.styleable.ArcProgressBar_arc_first_text_color, Color.RED)
        mSecondText = array.getString(R.styleable.ArcProgressBar_arc_second_text).toString()
        mSecondTextSize = dp2px(array.getDimension(R.styleable.ArcProgressBar_arc_second_text_size, 20f))
        mSecondTextColor = array.getColor(R.styleable.ArcProgressBar_arc_second_text_color, Color.RED)
        mAngleSize = array.getFloat(R.styleable.ArcProgressBar_arc_angle_size, 270f)
        mStartAngle = array.getFloat(R.styleable.ArcProgressBar_arc_start_angle, 135f)
        array.recycle()
    }
    private fun initPaint(){
        //画笔的填充样式，Paint.Style.FILL 填充内部;Paint.Style.FILL_AND_STROKE 填充内部和描边;Paint.Style.STROKE 描边
        mPaint.style = Paint.Style.STROKE
        //抗锯齿
        mPaint.isAntiAlias = true

        //画笔的样式 Paint.Cap.Round 圆形,Cap.SQUARE 方形
        mPaint.strokeCap = Paint.Cap.ROUND

        mPaintProgress.style = Paint.Style.STROKE
        mPaintProgress.isAntiAlias = true
        mPaintProgress.strokeCap = Paint.Cap.ROUND

        Log.i(TAG,"paint初始化")
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX = width/2
        val rectF = RectF()
        rectF.left = mStrokeWidth
        rectF.top = mStrokeWidth
        rectF.right = centerX * 2 - mStrokeWidth
        rectF.bottom = centerX * 2 - mStrokeWidth

        //画最外层的圆弧
        drawArcBg(canvas, rectF);
        //画进度
        drawArcProgress(canvas, rectF);
        //绘制第一级文本
        drawFirstText(canvas, centerX);
        //绘制第二级文本
        drawSecondText(canvas, centerX);
    }

    private fun drawSecondText(canvas: Canvas?, centerX: Int) {

    }

    private fun drawFirstText(canvas: Canvas?, centerX: Int) {

    }

    private fun drawArcProgress(canvas: Canvas?, rectF: RectF) {
        mPaintProgress.strokeWidth = mStrokeWidth
        mPaintProgress.color = mProgressColor
        canvas?.drawArc(rectF, mStartAngle, mCurrentAngleSize, false, mPaintProgress)
    }

    private fun drawArcBg(canvas: Canvas?, rectF: RectF) {
        //圆弧的宽度
        mPaint.strokeWidth = mStrokeWidth
        //画笔的颜色
        mPaint.color = mArcBgColor
        //开始画圆弧
        canvas?.drawArc(rectF, mStartAngle, mAngleSize, false, mPaint)
    }

    /**
     * dp转成px
     *
     * @param dp
     * @return
     */
    private fun dp2px(dp: Float): Float {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f * if (dp >= 0) 1 else -1)
    }

    /**
     * 设置最大的进度
     *
     * @param progress
     */
    fun setMaxProgress(progress: Int) {
        require(progress >= 0) { "Progress value can not be less than 0 " }
        mMaxProgress = progress.toFloat()
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    fun setProgress(progress: Float) {
        var progress = progress
        require(progress >= 0) { "Progress value can not be less than 0" }
        if (progress > mMaxProgress) {
            progress = mMaxProgress
        }
        mCurrentProgress = progress
        val size = mCurrentProgress / mMaxProgress
        mCurrentAngleSize = (mAngleSize * size).toInt().toFloat()
        setAnimator(0f, mCurrentAngleSize)
    }

    /**
     * 设置进度圆弧的颜色
     *
     * @param color
     */
    fun setProgressColor(color: Int) {
        require(color != 0) { "Color can no be 0" }
        mProgressColor = color
    }

    /**
     * 设置圆弧的颜色
     *
     * @param color
     */
    fun setArcBgColor(color: Int) {
        require(color != 0) { "Color can no be 0" }
        mArcBgColor = color
    }

    /**
     * 设置圆弧的宽度
     *
     * @param strokeWidth
     */
    fun setStrokeWidth(strokeWidth: Int) {
        require(strokeWidth >= 0) { "strokeWidth value can not be less than 0" }
        mStrokeWidth = dp2px(strokeWidth.toFloat())
    }

    /**
     * 设置动画的执行时长
     *
     * @param duration
     */
    fun setAnimatorDuration(duration: Long) {
        require(duration >= 0) { "Duration value can not be less than 0" }
        mDuration = duration
    }

    /**
     * 设置第一行文本
     *
     * @param text
     */
    fun setFirstText(text: String) {
        mFirstText = text
    }

    /**
     * 设置第一行文本的颜色
     *
     * @param color
     */
    fun setFirstTextColor(color: Int) {
        require(color > 0) { "Color value can not be less than 0" }
        mFirstTextColor = color
    }

    /**
     * 设置第一行文本的大小
     *
     * @param textSize
     */
    fun setFirstTextSize(textSize: Float) {
        require(textSize > 0) { "textSize can not be less than 0" }
        mFirstTextSize = textSize
    }

    /**
     * 设置第二行文本
     *
     * @param text
     */
    fun setSecondText(text: String) {
        mSecondText = text
    }

    /**
     * 设置第二行文本的颜色
     *
     * @param color
     */
    fun setSecondTextColor(color: Int) {
        require(color != 0) { "Color value can not be less than 0" }
        mSecondTextColor = color
    }

    /**
     * 设置第二行文本的大小
     *
     * @param textSize
     */
    fun setSecondTextSize(textSize: Float) {
        require(textSize > 0) { "textSize can not be less than 0" }
        mSecondTextSize = textSize
    }

    /**
     * 设置圆弧开始的角度
     *
     * @param startAngle
     */
    fun setStartAngle(startAngle: Int) {
        mStartAngle = startAngle.toFloat()
    }

    /**
     * 设置圆弧的起始角度到终点角度的大小
     *
     * @param angleSize
     */
    fun setAngleSize(angleSize: Int) {
        mAngleSize = angleSize.toFloat()
    }

    /**
     * 设置动画
     *
     * @param start  开始位置
     * @param target 结束位置
     */
    private fun setAnimator(start: Float, target: Float) {
        val valueAnimator = ValueAnimator.ofFloat(start, target)
        valueAnimator.duration = mDuration
        valueAnimator.setTarget(mCurrentAngleSize)
        valueAnimator.addUpdateListener { valueAnimator ->
            mCurrentAngleSize = valueAnimator.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()
    }

    /**
     * 测量字体的高度
     *
     * @param textStr
     * @param fontSize
     * @return
     */
    private fun getFontHeight(textStr: String, fontSize: Float): Int {
        val paint = Paint()
        paint.textSize = fontSize
        val bounds = Rect()
        paint.getTextBounds(textStr, 0, textStr.length, bounds)
        return bounds.height()
    }
}