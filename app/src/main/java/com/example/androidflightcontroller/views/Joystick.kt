package com.example.androidflightcontroller.views
    import android.annotation.SuppressLint
    import android.content.Context
    import android.graphics.Canvas
    import android.graphics.Color
    import android.graphics.Paint
    import android.graphics.PointF
    import android.util.AttributeSet
    import android.view.MotionEvent
    import android.view.View
    import kotlin.math.abs
    import kotlin.math.min

    fun makePaint(color: Int): Paint {
        val result = Paint()
        result.color = color
        result.style = Paint.Style.FILL_AND_STROKE
        result.isAntiAlias = true
        return result
    }

    class Joystick @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : View(context, attrs, defStyle) {
        // parameters for drawing a joystick
        private val outerCircle = makePaint(Color.BLACK) // joystick's pad
        private val innerCircle = makePaint(Color.GRAY) // the joystick itself
        private var outerRadius: Float = 0f
        private var innerRadius: Float = 0f
        private var outerCenter: PointF = PointF()
        private var innerCenter: PointF = PointF()
        var aileron = 0f
        var elevator = 0f
        lateinit var onChange: (Float, Float) -> Unit

        // called when the size of this view has changed to init
        override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
            outerRadius = min(width, height) / 3.3f
            innerRadius = min(width, height) / 10f
            outerCenter = PointF(width / 2.0f, height / 2.0f)
            innerCenter = PointF(width / 2.0f, height / 2.0f)
        }

        // called to draw the joystick and it's pad
        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(outerCenter.x, outerCenter.y, outerRadius, outerCircle)
            canvas.drawCircle(innerCenter.x, innerCenter.y, innerRadius, innerCircle)
        }

        // when the joystick is moved
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_MOVE -> makeMove(event.x, event.y)
                MotionEvent.ACTION_UP -> makeUp()
            }
            return true
        }

        private fun makeMove(x: Float, y: Float) {
            val xDistance = abs(x - outerCenter.x)
            val yDistance = abs(y - outerCenter.y)
            // make sure the movement is not out of bounds
            if ((xDistance <= outerRadius) && (yDistance <= outerRadius)) {
                innerCenter.set(x, y);

                aileron = (x - outerCenter.x) / (outerRadius)
                elevator = (y - outerCenter.y) / (outerRadius)

                try {
                    onChange(aileron,elevator)
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
            invalidate()
        }

        private fun makeUp() {
            innerCenter.set(outerCenter.x, outerCenter.y);
            invalidate()
        }
    }