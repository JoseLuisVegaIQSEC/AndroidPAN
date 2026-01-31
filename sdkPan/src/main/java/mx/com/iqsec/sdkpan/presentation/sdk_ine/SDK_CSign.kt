package mx.com.iqsec.sdkpan.presentation.sdk_ine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasSinguature @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var disableScroll: ((Boolean) -> Unit)? = null
) : View(context, attrs, defStyleAttr) {
    // Callback que el fragmento puede asignar
    var onOccupationChanged: ((Float) -> Unit)? = null

    var pLine = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 9f
    }
    var pBg: Paint = Paint().apply {
        setColor(Color.WHITE)
    }

    lateinit var c: Canvas
    var touchPath: Path = Path()
    lateinit var b: Bitmap
    private var isCanvasEmpty = true

    private fun notifyOccupationChanged() {
        val p = getSignatureOccupationPercentage()
        onOccupationChanged?.invoke(p)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        c = Canvas(b)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPath.moveTo(touchX, touchY)
                isCanvasEmpty = false
                disableScroll?.invoke(true)
                notifyOccupationChanged()
            }
            MotionEvent.ACTION_MOVE -> {
                touchPath.lineTo(touchX, touchY)
                notifyOccupationChanged()
            }
            MotionEvent.ACTION_UP -> {
                touchPath.lineTo(touchX, touchY)
                c.drawPath(touchPath, pLine)
                touchPath = Path()
                disableScroll?.invoke(false)
                notifyOccupationChanged()
            }

            else -> return false
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(b, 0f, 0f, pBg)
        canvas.drawPath(touchPath, pLine)
    }

    fun isCanvasEmpty(): Boolean {
        return isCanvasEmpty
    }

    fun clearCanvas() {
        touchPath.reset()
        b.eraseColor(Color.TRANSPARENT)
        isCanvasEmpty = true
        notifyOccupationChanged()
        invalidate()
    }

    fun getSignatureOccupationPercentage(): Float {
        if (isCanvasEmpty) return 0f

        // Total de píxeles en el canvas
        val totalPixels = b.width * b.height

        // Contamos píxeles ocupados por el trazo
        var occupiedPixels = 0
        val pixels = IntArray(totalPixels)
        b.getPixels(pixels, 0, b.width, 0, 0, b.width, b.height)

        for (pixel in pixels) {
            // Comparamos con el color de fondo (blanco o transparente)
            if (pixel != Color.TRANSPARENT && pixel != Color.WHITE) {
                occupiedPixels++
            }
        }

        // Calculamos el porcentaje
        return (occupiedPixels.toFloat() / totalPixels) * 100f
    }
}