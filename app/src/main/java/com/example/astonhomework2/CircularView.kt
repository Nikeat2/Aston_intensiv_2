package com.example.astonhomework2

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import kotlin.random.Random

class CircularView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    private val orange = ContextCompat.getColor(context, R.color.orange)
    private val lightBlue = ContextCompat.getColor(context, R.color.light_blue)
    private val purple = ContextCompat.getColor(context, R.color.purple)
    private var angle: Float = 0f
    private val indicatorPaint = Paint().apply {
        color = 0xFF000000.toInt()
        strokeWidth = 25f
        style = Paint.Style.STROKE
    }
    private var selectedPieIndex: Int = -1
    private var colorStopListener: OnColorStopListener? = null
    private var radius: Float = 0f
    val colors = arrayOf(
        Color.RED,
        orange,
        Color.YELLOW,
        Color.GREEN,
        lightBlue,
        Color.BLUE,
        purple
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val desiredHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        radius = (width / 2).toFloat()
        val cx = width / 2f
        val cy = height / 2f
        val sweepAngle = 360f / colors.size
        var startAngle = angle

        for (i in colors) {
            paint.color = i
            canvas.drawArc(
                cx - radius, cy - radius, cx + radius, cy + radius,
                startAngle, sweepAngle, true, paint
            )
            startAngle += sweepAngle
        }
        selectedPieIndex = ((angle % 360) / sweepAngle).toInt() % colors.size

        val indicatorLength = 30f
        val indicatorStartY = cy - radius
        val indicatorEndY = cy - indicatorLength

        canvas.drawLine(cx, indicatorStartY, cx, indicatorEndY, indicatorPaint)
    }

    fun rotate(duration: Long) {
        val randomSector = Random.nextInt(colors.size)
        val finalAngle = (randomSector * (360 / colors.size) + 720) % 360

        ValueAnimator.ofFloat(angle, finalAngle + angle).apply {
            interpolator = LinearInterpolator()
            this.duration = duration
            addUpdateListener { animation ->
                angle = animation.animatedValue as Float % 360
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    val sectorColor = getColorForAngle(angle)
                    val colorName = colorToString(sectorColor)
                    colorStopListener?.onColorStop(colorName)
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }

    private fun getColorForAngle(angle: Float): Int {
        val sweepAngle = 360f / colors.size
        val sectorIndex = ((angle % 360) / sweepAngle).toInt() % colors.size
        return colors[sectorIndex]
    }

    private fun colorToString(color: Int): String {
        return when (color) {
            Color.RED -> "lightblue"
            Color.GREEN -> "orange"
            Color.BLUE -> "purple"
            Color.YELLOW -> "yellow"
            orange -> "green"
            lightBlue -> "red"
            purple -> "blue"
            else -> "Неизвестный цвет"
        }
    }

    fun setOnColorStopListener(listener: OnColorStopListener) {
        this.colorStopListener = listener
    }

}