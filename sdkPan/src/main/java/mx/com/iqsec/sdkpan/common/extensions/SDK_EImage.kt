package mx.com.iqsec.sdkpan.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream

fun Bitmap.convertBitmapToBase64String(quality: Int = 30): String? {
    return try {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val byteArrayImage = baos.toByteArray()
        Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
    } catch (exception: OutOfMemoryError) {
        null
    }
}

fun Bitmap.calculateBrightnessEstimate(): Double {
    var R = 0.0
    var G = 0.0
    var B = 0.0
    val height = height
    val width = width
    val pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    var i = 0
    val pixelSpacing = 1

    while (i < pixels.size) {
        val color = pixels[i]

        R += (red(color))
        G += (green(color))
        B += (blue(color))

        i += pixelSpacing
    }
    R = (R / 255.0) * 0.2126
    G = (G / 255.0) * 0.7152
    B = (B / 255.0) * 0.0722

    return ((R + B + G)) / (pixels.size)
}

fun String?.decodeBase64ToBitmap(): Bitmap? {
    val encodedImage = this
    return if (!encodedImage.isNullOrEmpty()) {
        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        decodedByte
    } else {
        null
    }
}

fun Bitmap.obtenerMitad(parte: Int): Bitmap {
    val ancho = this.width
    val alto = this.height
    val anchoMitad = ancho / 2

    val x = when (parte) {
        0 -> 0 // izquierda
        1 -> anchoMitad // derecha
        else -> throw IllegalArgumentException("Parte debe ser 0 o 1")
    }

    return Bitmap.createBitmap(this, x, 0, anchoMitad, alto)
}

fun startLoopAnimation(
    context: Context,
    img1: ImageView,
    img2: ImageView,
) {
    val screenWidth = context.resources.displayMetrics.widthPixels
    val velocidad = .6f

    img1.translationX = 0f
    img2.translationX = screenWidth.toFloat()

    val animator = object : Runnable {
        override fun run() {
            img1.translationX -= velocidad
            img2.translationX -= velocidad

            if (img1.translationX <= -screenWidth) {
                img1.translationX = img2.translationX + screenWidth
            }
            if (img2.translationX <= -screenWidth) {
                img2.translationX = img1.translationX + screenWidth
            }
            img1.postDelayed(this, 16L)
        }
    }
    img1.post(animator)
}

fun Bitmap.obtenerTercio(parte: Int): Bitmap {
    val ancho = this.width
    val alto = this.height
    val anchoTercio = ancho / 3

    val x = when (parte) {
        0 -> 0 // izquierda
        1 -> anchoTercio // centro
        2 -> 2 * anchoTercio // derecha
        else -> throw IllegalArgumentException("Parte debe ser 0, 1 o 2")
    }

    return Bitmap.createBitmap(this, x, 0, anchoTercio, alto)
}

fun startAnimationTercio(
    context: Context,
    img1: ImageView,
    img2: ImageView,
    img3: ImageView,
) {

    val screenWidth = context.resources.displayMetrics.widthPixels
    val velocidad = .6f // Cambia este valor para ajustar la velocidad

    val image1 = img1
    val image2 = img2
    val image3 = img3

    image1.translationX = 0f
    image2.translationX = screenWidth.toFloat()
    image3.translationX = 2 * screenWidth.toFloat()

    val animator = object : Runnable {
        override fun run() {
            image1.translationX -= velocidad
            image2.translationX -= velocidad
            image3.translationX -= velocidad

            if (image1.translationX <= -screenWidth) {
                image1.translationX =
                    maxOf(image2.translationX, image3.translationX) + screenWidth
            }
            if (image2.translationX <= -screenWidth) {
                image2.translationX =
                    maxOf(image1.translationX, image3.translationX) + screenWidth
            }
            if (image3.translationX <= -screenWidth) {
                image3.translationX =
                    maxOf(image1.translationX, image2.translationX) + screenWidth
            }
            image1.postDelayed(this, 16L)
        }
    }
    image1.post(animator)
}