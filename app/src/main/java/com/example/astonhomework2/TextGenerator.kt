package com.example.astonhomework2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TextGenerator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    var text: String = ""

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = 500
        val height = 600
        setMeasuredDimension(width, height)
    }

    private val paint = Paint().apply {
        color = Color.YELLOW
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width / 2f
        val y = height / 2f
        canvas.drawText(text, x, y, paint)
    }

    fun setNewText(newText: String) {
        text = newText
        invalidate()
    }

    fun updateText() {
        text = ""
        invalidate()
    }
}