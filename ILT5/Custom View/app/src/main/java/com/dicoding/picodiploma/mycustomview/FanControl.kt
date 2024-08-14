package com.dicoding.picodiploma.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


class FanControl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        const val SELECTION_COUNT: Int = 4 // Total number of selections.
    }

    private var mWidth = 0f // Custom view width.
    private var mHeight = 0f // Custom view height.
    private var mTextPaint: Paint? = null // For text in the view.
    private var mDialPaint: Paint? = null // For dial circle in the view.
    private var mViewNameTextPaint: Paint? = null // For text view name.
    private var mRadius = 0f // Radius of the circle.
    private var mActiveSelection = 0 // The active selection.

    private var mIndicatorLabelValue = "" // To hold Indicator text value
    private var mIndicatorCoordinateValue = mutableListOf(0F, 0F) // To hold Indicator position

    private var mTextBounds = Rect()

    init {
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            textAlign = Paint.Align.CENTER
            textSize = 40f
        }

        mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GRAY
        }

        mViewNameTextPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        mActiveSelection = 0

        setOnClickListener { // Rotate selection to the next valid choice.
            mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT
            // Set dial background color to green if selection is >= 1.
            if (mActiveSelection >= 1) {
                mDialPaint!!.color = Color.GREEN
            } else {
                mDialPaint!!.color = Color.GRAY
            }
            // Redraw the view.
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Calculate the radius from the width and height.
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = (Math.min(mWidth, mHeight) / 2 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the circle.
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint!!)

        // Draw the text labels.
        val labelRadius = mRadius + 20
        for (i in 0 until SELECTION_COUNT) {
            val xyData = calculateIndicatorPosition(i, labelRadius)
            val x = xyData[0]
            val y = xyData[1]
            mIndicatorLabelValue = "$i"
            canvas.drawText(mIndicatorLabelValue, 0, mIndicatorLabelValue.length, x, y, mTextPaint!!)
        }

        // Draw the indicator mark.
        val markerRadius = mRadius - 35
        val xyData = calculateIndicatorPosition(
            mActiveSelection,
            markerRadius
        )
        val x = xyData[0]
        val y = xyData[1]
        canvas.drawCircle(x, y, 20f, mTextPaint!!)

        // Draw the Custom View Name text.
        val viewName = "Fan Control"
        mViewNameTextPaint?.getTextBounds(viewName, 0, viewName.length, mTextBounds)

        val viewNameXCoordinate = mWidth / 2
        val viewNameYCoordinate = mHeight
        canvas.drawText(viewName, viewNameXCoordinate, viewNameYCoordinate, mViewNameTextPaint!!)
    }

    private fun calculateIndicatorPosition(pos: Int, radius: Float): List<Float>{
        val xyCoordinate = mIndicatorCoordinateValue
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + (pos * (Math.PI / 4))
        xyCoordinate[0] = (radius * cos(angle)).toFloat() + (mWidth / 2)
        xyCoordinate[1] = (radius * sin(angle)).toFloat() + (mHeight / 2)
        return xyCoordinate
    }
}