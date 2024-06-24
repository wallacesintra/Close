package com.example.close.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class ConvertImgToBitmapDescriptor {

    private fun getCircleBitmapFromResource(context: Context, resourceId: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, (bitmap.width), bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)

        canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, bitmap.width / 2f, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output

    }

    fun getBitmapDescriptorFromBitmap(context: Context, resource: Int): BitmapDescriptor {
        val circularBitmap = getCircleBitmapFromResource(context, resource)

        return BitmapDescriptorFactory.fromBitmap(circularBitmap)

    }

}