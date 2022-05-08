package com.example.cassebrique


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {
    lateinit var canvas: Canvas
    val backgroundPaint = Paint()
    val textPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    val lesParois = ArrayList<Parois>()
    val sol = Sol(0f, 0f, 0f, 0f, this)
    val dico = mutableMapOf<String, Cible>()
    lateinit var thread: Thread
    val plateforme = Plateforme(0f, 0f, 0f, 0f, this)
    val brique = Cible(0f, 0f,   0f, this)
    val balle = Balle(0f, 0f, 0f, this)
    var rightorleft: String? = null
    var key:  String? = null
    var shotsFired = 0
    var timeLeft = 0.0
    val MISS_PENALTY = 2
    val HIT_REWARD = 3
    var gameOver = false
    val activity = context as FragmentActivity
    var totalElapsedTime = 0.0
    var i =0
    var viesutilisees = 0
    lateinit var un: ImageView
    lateinit var deux: ImageView
    lateinit var trois: ImageView





    // val soundMap: SparseIntArray

    init {
        backgroundPaint.color = Color.WHITE
        textPaint.textSize= screenWidth/20
        textPaint.color = Color.BLACK
        timeLeft = 60.0



        /*soundMap = SparseIntArray(3)
        soundMap.put(0, soundPool.load(context, R.raw.target_hit, 1))
        soundMap.put(1, soundPool.load(context, R.raw.cannon_fire, 1))
        soundMap.put(2, soundPool.load(context, R.raw.blocker_hit, 1))*/
    }


    fun playObstacleSound() {
        //soundPool.play(soundMap.get(2), 1f, 1f, 1, 0, 1f)
    }

    fun playCibleSound() {
        //soundPool.play(soundMap.get(0), 1f, 1f, 1, 0, 1f)
    }

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }
    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            val elapsedTimeMS = (currentTime-previousFrameTime).toDouble()
            totalElapsedTime += elapsedTimeMS / 1000.0
            updatePositions(elapsedTimeMS)
            draw()

            previousFrameTime = currentTime
        }
    }

    override fun onSizeChanged(w:Int, h:Int, oldw:Int, oldh:Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()
        plateforme.x1 = (w * 79/200f)
        plateforme.y1 = (h * 6/7f)
        plateforme.x2 = (w * 5/8f)
        plateforme.y2 = (h * 22/25f)
        plateforme.setRect()
        sol.x1 = (0f)
        sol.y1 = (h.toFloat())
        sol.x2 = (w.toFloat())
        sol.y2 = (h.toFloat())
        sol.setRect()
        val canvasH = (h - 226).toFloat()
        val canvasW = (w - 25).toFloat()
        lesParois.add(Parois(5f, 5f, 25f, canvasH))
        lesParois.add(Parois(5f, 5f, canvasW, 25f))
        lesParois.add(Parois(canvasW, 5f, canvasW + 18, canvasH))
        lesParois.add(plateforme)
        lesParois.add(sol)
        balle.xballe = (w / 2f)
        balle.yballe = (h * 5/6f)
        balle.diametreballe = (25f)
        balle.setRect()
        /*canon.canonBaseRadius = (h / 18f)
        canon.canonLongueur = (w / 8f)
        canon.largeur = (w / 24f)
        canon.setFinCanon(h / 2f) */
        /* obstacle.obstacleDistance = (w * 5 / 8f)
         obstacle.obstacleDebut = (h / 8f)
         obstacle.obstacleFin = (h * 3 / 8f)
         obstacle.width = (w / 24f)
         obstacle.initialObstacleVitesse= (h / 2f)
         obstacle.setRect() */
        brique.width = (h/20f)
        brique.ciblel= (h / 15f)
        brique.ciblet = (w / 8f)
        textPaint.setTextSize(w / 20f)
        textPaint.isAntiAlias = true
    }





    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                canvas.height.toFloat(), backgroundPaint)
            val formatted = String.format("%.2f", timeLeft)
            canvas.drawText("Il reste $formatted secondes. ",
                30f, 50f, textPaint)
            if (balle.ballOnScreen)
                balle.draw(canvas)
            for (p in lesParois) {
                p.draw(canvas)
            }
            //obstacle.draw(canvas)
            while (i < 1){
                brique.create(brique.ciblet)
                i+= 1
            }

            for ((k, v) in dico){
                v.draw(canvas)
            }


            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        /*  obstacle.update(interval)
          cible.update(interval)*/
        balle.update()
        plateforme.update(interval)
        timeLeft -= interval
        pertevie()
        if (timeLeft <= 0.0) {
            timeLeft = 0.0
            gameOver = true
            drawing = false
            showGameOverDialog(R.string.lose)
        }
    }

    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.win)
        gameOver = true
    }
    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            @SuppressLint("StringFormatInvalid")
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                    resources.getString(
                        R.string.results_format, shotsFired, totalElapsedTime
                    )
                )
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { _, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        var x = e.rawX
        if (action == MotionEvent.ACTION_DOWN
            || action == MotionEvent.ACTION_MOVE) {
            fireCanonball(e)
        }
        else if (action == MotionEvent.ACTION_UP) {
            x = screenWidth * 1 / 2
        }
        rightorleftclick(x)
        return true
    }

    fun fireCanonball(event: MotionEvent) {
        if (! balle.ballOnScreen) {
            //val angle = alignCanon(event)
            balle.launch(0.0)
            ++shotsFired
            //soundPool.play(soundMap.get(1), 1f, 1f, 1, 0, 1f)
        }
    }

    fun pertevie() {
        balle.sortieballe(screenHeight)


        if (balle.balleperdue) {

            if (viesutilisees == 0) {
                trois.visibility = View.INVISIBLE
                gameOver = true
                drawing = false
                showGameOverDialog(R.string.vie)

            }
            if (viesutilisees == 1) {
                deux.visibility = View.INVISIBLE
                gameOver = true
                drawing = false
                showGameOverDialog(R.string.vie)
            }
            if (viesutilisees == 2) {
                un.visibility = View.INVISIBLE
                viesutilisees = -1
                gameOver = true
                drawing = false
                showGameOverDialog(R.string.lose)
            }
        }
    }




            /*if (viesutilisees < 2) {

                balle.balleperdue = false
            } else {

            }

             */


    /*fun alignCanon(event: MotionEvent): Double {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val centerMinusY = screenHeight / 2 - touchPoint.y
        var angle = 0.0
        if (centerMinusY != 0.0f)
            angle = Math.atan((touchPoint.x).toDouble()/ centerMinusY)
        if (touchPoint.y > screenHeight / 2)
            angle += Math.PI
        //canon.align(angle)
        return angle
    }*/

    fun rightorleftclick(x :Float) {
        rightorleft = null
        if (x < screenWidth * 1/2 - screenWidth*1/7) {
            rightorleft = "Left"
        }
        else if (x > screenWidth * 1/2 + screenWidth*1/7){
            rightorleft = "Right"
        }

    }

    fun newGame() {
        val h = screenHeight
        val w = screenWidth
        /*cible.resetCible()
        obstacle.resetObstacle()*/
        viesutilisees += 1
        timeLeft = 60.0
        balle.resetCanonBall()
        shotsFired = 0
        drawing = true
        balle.balleperdue = false
        balle.xballe = (w / 2f)
        balle.yballe = (h * 5/6f)
        balle.ballOnScreen = false
        balle.launch(0.0)
        plateforme.setRect()
        balle.setRect()
        plateforme.setRect()
        balle.setRect()
        if (gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }

    fun reduceTimeLeft() {
        timeLeft -= MISS_PENALTY
    }

    fun increaseTimeLeft() {
        timeLeft += HIT_REWARD
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

}
