package br.com.dragdrop

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val TAG = "DRAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myimage1.setOnTouchListener(MyTouchListener())
        myimage2.setOnTouchListener(MyTouchListener())
        myimage3.setOnTouchListener(MyTouchListener())
        myimage4.setOnTouchListener(MyTouchListener())

        topleft.setOnDragListener(MyDragListener())
        topright.setOnDragListener(MyDragListener())
        bottomleft.setOnDragListener(MyDragListener())
        bottomright.setOnDragListener(MyDragListener())
    }

    inner class MyTouchListener : View.OnTouchListener {
        override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
            return if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)
                view?.startDragAndDrop(data, shadowBuilder, view, 0)
                view?.visibility = View.INVISIBLE
                true
            } else {
                false
            }
        }
    }


    inner class MyDragListener : View.OnDragListener {
        private val enterShape = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape_droptarget
        )

        private val normalShape = ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.shape
        )

        private lateinit var layoutParams: LinearLayout.LayoutParams

        override fun onDrag(v: View, event: DragEvent?): Boolean {
            when (event?.action) {
                DragEvent.ACTION_DRAG_STARTED -> Log.d(TAG, "Action is DragEvent.ACTION_DRAG_STARTED")

                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENTERED")
                    v.background = enterShape
                }

                DragEvent.ACTION_DRAG_ENDED -> v.background = normalShape

                DragEvent.ACTION_DRAG_LOCATION -> Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION")

                DragEvent.ACTION_DRAG_EXITED -> {
                    v.background = normalShape
                    Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED")
                    val xCord = event.x.toInt()
                    val yCord = event.y.toInt()
                    layoutParams.leftMargin = xCord
                    layoutParams.topMargin = yCord
                    v.layoutParams = layoutParams
                }

                DragEvent.ACTION_DROP -> {
                    Log.d(TAG, "Action is DragEvent.ACTION_DRAG_DROP")

                    val view = event.localState as View
                    val viewGroupOwner = view.parent as ViewGroup
                    viewGroupOwner.removeView(view)

                    val container = v as LinearLayout
                    container.addView(view)
                    view.visibility = View.VISIBLE
                }


            }
            return true
        }
    }
}
