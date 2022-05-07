package com.example.cassebrique

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt


class Balle(var xballe: Float, var yballe: Float, var diametreballe: Float, val view: DrawingView) {



    val w = RectF(xballe, yballe, xballe + diametreballe, yballe + diametreballe)
    var ballOnScreen = false
    var balleperdue = false
    val paint = Paint()
    val ballVitesse = 5f
    var dx = 0f
    var dy = 0f

    fun setRect(){
        w.set(xballe, yballe, xballe + diametreballe, yballe + diametreballe)
    }


    fun draw (canvas: Canvas) {
        paint.color = Color.GREEN
        canvas.drawOval(w, paint)
    }

    fun launch(angle: Double) {
        dx = (ballVitesse * Math.sin(angle)).toFloat()
        dy = (-ballVitesse * Math.cos(angle)).toFloat()
        ballOnScreen = true
    }

    fun sortieballe(h:Float) {
        if (yballe > h) {
            balleperdue = true
        }
    }

    fun update() {
        "Mouvement balle"
        if (ballOnScreen) {
            w.offset(4.0F * dx, 4.0F * dy)
        }

        "Si contact avec parois"
        for (p in view.lesParois) {
            p.gereBalle(this)
        }
        view.key = null
        for ((k, v) in view.dico){
            v.gereBalle(k, this)

        }
        view.dico.remove(view.key)

    }

    fun changeDirection(x: Boolean, p: Parois?) {
        "Si contact avec parois rebond normal"
        "Si contact avec plateforme rebond en fct d'où ça a touché (else)"
        if (p != view.plateforme){
            if (x) {
                this.dy = -dy
            }
            else {
                this.dx = -dx
            }

        }
        else{
            this.dx = dx + 3f* (view.plateforme.facteurrebond -1f)
            this.dy = -sqrt(ballVitesse*ballVitesse -this.dx*this.dx)
        }

        w.offset(3.0F*dx, 3.0F*dy)

    }

    fun resetCanonBall() {
        ballOnScreen = false
    }



}
