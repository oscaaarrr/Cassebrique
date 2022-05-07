package com.example.cassebrique

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log

class Sol ( x1: Float,  y1: Float, x2 : Float,  y2: Float, val view: DrawingView)
    :Parois ( x1, y1, x2, y2) {
    override val r = RectF(x1, y1, x2, y2)
    override val paint = Paint()
    override var color = Color.GREEN

    fun setRect() {
        r.set(x1, y1, x2, y2)
    }

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawRect(r, paint)

    }

    override fun gereBalle(balle: Balle) {
        "Permet que lorsque la balle touche la plateforme à un certain endroit de faire varier son rebond en fonction de " +
                "où ça a touché "
        "Et change de couleur"

        if (RectF.intersects(r,balle.w)) {
            balle.yballe = 100000000F

        }
    }

    /*fun ballesort(balle: Balle) {
        "Permet que lorsque la balle touche la plateforme à un certain endroit de faire varier son rebond en fonction de " +
                "où ça a touché "
        "Et change de couleur"

        if (RectF.intersects(r, balle.w)) {
            balle.yballe = 100000000F

        }

     */


    }

