package com.dicoding.mywatermelon

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

// todo-01: create a WatermelonView
class WatermelonView : View {

    // todo-02: add constructor like create a custom view
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // todo-03: add onDraw method to draw a Canvas
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // todo-04: add static value to create a watermelon
        val mX = (width / 2).toFloat()
        val mY = (height / 2).toFloat() - 200

        val startAngle = 0f
        val sweepAngle = 180f
        val sliceRind = 50f
        val radiusWidth = 400f
        val radiusHeight = 350f

        // todo-05: draw a rind using arc (oval)
        val rindPaint = Paint()
        rindPaint.color =
            ResourcesCompat.getColor(resources, android.R.color.holo_green_light, null)

        val rectRind = RectF(
            mX - radiusWidth,
            mY - radiusHeight,
            mX + radiusWidth,
            mY + radiusHeight
        )
        canvas.drawArc(rectRind, startAngle, sweepAngle, true, rindPaint)

        // todo-06: draw a flesh using arc (oval)
        val fleshPaint = Paint()
        fleshPaint.color = ResourcesCompat.getColor(resources, android.R.color.holo_red_light, null)

        val rectFlesh = RectF(
            mX - radiusWidth + sliceRind,
            mY - radiusHeight + sliceRind,
            mX + radiusWidth - sliceRind,
            mY + radiusHeight - sliceRind
        )
        canvas.drawArc(rectFlesh, startAngle, sweepAngle, true, fleshPaint)

        // todo-07: draw a seed using circle (lingkaran)
        val seedPaint = Paint()
        seedPaint.color = ResourcesCompat.getColor(resources, android.R.color.black, null)

        canvas.drawCircle(mX + 30f, mY + 40f, 10f, seedPaint)
        canvas.drawCircle(mX - 60f, mY + 90f, 10f, seedPaint)
        canvas.drawCircle(mX - 190f, mY + 130f, 10f, seedPaint)
        canvas.drawCircle(mX - 280f, mY + 50f, 10f, seedPaint)
        canvas.drawCircle(mX - 100f, mY + 220f, 10f, seedPaint)
        canvas.drawCircle(mX - 150f, mY + 40f, 10f, seedPaint)
        canvas.drawCircle(mX + 0f, mY + 170f, 10f, seedPaint)
        canvas.drawCircle(mX + 90f, mY + 140f, 10f, seedPaint)
        canvas.drawCircle(mX + 40f, mY + 270f, 10f, seedPaint)
        canvas.drawCircle(mX + 160f, mY + 30f, 10f, seedPaint)
        canvas.drawCircle(mX + 200f, mY + 120f, 10f, seedPaint)
        canvas.drawCircle(mX + 280f, mY + 70f, 10f, seedPaint)
        canvas.drawCircle(mX + 150f, mY + 200f, 10f, seedPaint)
    }
}
