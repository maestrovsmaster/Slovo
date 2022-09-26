package com.maestrovs.slovo.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.ProgressBar


class UIProgressHorizontal @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ProgressBar(context, attrs) {

    private val DEFAULT_TEXT_SIZE = 16
    private val DEFAULT_TEXT_COLOR = -0x3ff2f
    private val DEFAULT_COLOR_UNREACHED_COLOR = -0x2c2926
    private val DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2
    private val DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2
    private val DEFAULT_SIZE_TEXT_OFFSET = 10

    /**
     * painter of all drawing things
     */
    protected var mPaint: Paint = Paint()

    /**
     * color of progress number
     */
    protected var mTextColor = DEFAULT_TEXT_COLOR

    /**
     * size of text (sp)
     */
    protected var mTextSize = sp2px(DEFAULT_TEXT_SIZE)

    /**
     * offset of draw progress
     */
    protected var mTextOffset = dp2px(DEFAULT_SIZE_TEXT_OFFSET)

    /**
     * height of reached progress bar
     */
    protected var mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR)

    /**
     * color of reached bar
     */
    protected var mReachedBarColor = DEFAULT_TEXT_COLOR

    /**
     * color of unreached bar
     */
    protected var mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR

    /**
     * height of unreached progress bar
     */
    protected var mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR)

    /**
     * view width except padding
     */
    protected var mRealWidth = 0

    protected var mIfDrawText = true

    protected val VISIBLE = 0

    /*fun HorizontalProgressBarWithNumber(context: Context?, attrs: AttributeSet?) {
        this(context, attrs, 0)
    }*/

    /*fun HorizontalProgressBarWithNumber(
        context: Context?, attrs: AttributeSet,
        defStyle: Int
    ) {
        super(context, attrs, defStyle)
       // obtainStyledAttributes(attrs)
        mPaint.setTextSize(mTextSize.toFloat())
        mPaint.setColor(mTextColor)
    }*/

    @Synchronized
    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
        mRealWidth = measuredWidth - paddingRight - paddingLeft
    }

    private fun measureHeight(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            val textHeight: Float = mPaint.descent() - mPaint.ascent()
            result = (paddingTop + paddingBottom + Math.max(
                Math.max(
                    mReachedProgressBarHeight,
                    mUnReachedProgressBarHeight
                ).toFloat(), Math.abs(textHeight)
            )).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    /**
     * get the styled attributes
     *
     * @param attrs
     */
   /* private fun obtainStyledAttributes(attrs: AttributeSet) {
        // init values from custom attributes
        val attributes: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.HorizontalProgressBarWithNumber
        )
        mTextColor = attributes
            .getColor(
                R.styleable.HorizontalProgressBarWithNumber_progress_text_color,
                DEFAULT_TEXT_COLOR
            )
        mTextSize = attributes.getDimension(
            R.styleable.HorizontalProgressBarWithNumber_progress_text_size,
            mTextSize.toFloat()
        ).toInt()
        mReachedBarColor = attributes
            .getColor(
                R.styleable.HorizontalProgressBarWithNumber_progress_reached_color,
                mTextColor
            )
        mUnReachedBarColor = attributes
            .getColor(
                R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color,
                DEFAULT_COLOR_UNREACHED_COLOR
            )
        mReachedProgressBarHeight = attributes
            .getDimension(
                R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height,
                mReachedProgressBarHeight.toFloat()
            ).toInt()
        mUnReachedProgressBarHeight = attributes
            .getDimension(
                R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height,
                mUnReachedProgressBarHeight.toFloat()
            ).toInt()
        mTextOffset = attributes
            .getDimension(
                R.styleable.HorizontalProgressBarWithNumber_progress_text_offset,
                mTextOffset.toFloat()
            ).toInt()
        val textVisible = attributes
            .getInt(
                R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility,
                VISIBLE
            )
        if (textVisible != VISIBLE) {
            mIfDrawText = false
        }
        attributes.recycle()
    }*/

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat())
        Log.d("mProgress","progress=$progress")

        var noNeedBg = false
        val radio = progress * 1.0f / max
        var progressPosX: Float = (mRealWidth * radio).toFloat()
        val text = "$progress%"
        // mPaint.getTextBounds(text, 0, text.length(), mTextBound);
        val textWidth: Float = mPaint.measureText(text)
        val textHeight: Float = (mPaint.descent() + mPaint.ascent()) / 2
        if (progressPosX + textWidth > mRealWidth) {
            progressPosX = mRealWidth - textWidth
            noNeedBg = true
        }

        // draw reached bar
        val endX = progressPosX - mTextOffset / 2
        if (endX > 0) {
            mPaint.setColor(mReachedBarColor)
            mPaint.setStrokeWidth(mReachedProgressBarHeight.toFloat())
            canvas.drawLine(0.toFloat(), 0.toFloat(), endX, 0.toFloat(), mPaint)
        }
        // draw progress bar
        // measure text bound
        if (mIfDrawText) {
            mPaint.setColor(mTextColor)
            canvas.drawText(text, progressPosX, -textHeight, mPaint)
        }

        // draw unreached bar
        if (!noNeedBg) {
            val start = progressPosX + mTextOffset / 2 + textWidth
            mPaint.setColor(mUnReachedBarColor)
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight.toFloat())
            canvas.drawLine(start, 0.toFloat(), mRealWidth.toFloat(), 0.toFloat(), mPaint)
        }
        canvas.restore()
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected fun dp2px(dpVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(), resources.displayMetrics
        ).toInt()
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected fun sp2px(spVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal.toFloat(), resources.displayMetrics
        ).toInt()
    }
}