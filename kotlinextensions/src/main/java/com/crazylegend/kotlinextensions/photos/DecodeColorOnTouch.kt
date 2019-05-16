package com.crazylegend.kotlinextensions.photos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.View


/**
 * Created by hristijan on 3/28/19 to long live and prosper !
 */


/**
 USAGE

  img.setOnTouchListener { v, event ->
val color = decodeActionDownEvent(v,event, img.getBitmap())

color.debug(color.toHexString())

return@setOnTouchListener true
}
 */

fun decodeActionDownEvent(v: View, ev: MotionEvent, bitmap: Bitmap): Int {
    val inverse = Matrix()
    v.matrix.invert(inverse)
    val touchPoint = floatArrayOf(ev.x, ev.y)
    inverse.mapPoints(touchPoint)
    val xCoord = Integer.valueOf(touchPoint[0].toInt())
    val yCoord = Integer.valueOf(touchPoint[1].toInt())
    return try {
        bitmap.getPixel(xCoord, yCoord)
    } catch (e: IllegalArgumentException) {
        Color.WHITE
    }

}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        inSampleSize = if (width > height) {
            Math.round(height.toFloat() / reqHeight.toFloat())
        } else {
            Math.round(width.toFloat() / reqWidth.toFloat())
        }
    }
    return inSampleSize
}

fun decodeSampledBitmapFromFile(fileName: String, reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(fileName, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(fileName, options)
}