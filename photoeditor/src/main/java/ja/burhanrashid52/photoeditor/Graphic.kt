package ja.burhanrashid52.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import ja.burhanrashid52.photoeditor.MultiTouchListener.OnGestureControl
import ja.burhanrashid52.photoeditor.utils.RotateTouchListener
import ja.burhanrashid52.photoeditor.utils.ZoomTouchListener

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal abstract class Graphic(
    val context: Context,
    val layoutId: Int,
    val viewType: ViewType,
    val graphicManager: GraphicManager?
) {

    val rootView: View

    open fun updateView(view: View) {
        //Optional for subclass to override
    }

    init {
        if (layoutId == 0) {
            throw UnsupportedOperationException("Layout id cannot be zero. Please define a layout")
        }
        rootView = LayoutInflater.from(context).inflate(layoutId, null)
        setupView(rootView)
        setupRemoveView(rootView)
    }


    private fun setupRemoveView(rootView: View) {
        //We are setting tag as ViewType to identify what type of the view it is
        //when we remove the view from stack i.e onRemoveViewListener(ViewType viewType, int numberOfAddedViews);
        rootView.tag = viewType
        val imgClose = rootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)

        imgClose?.setOnClickListener { graphicManager?.removeView(this@Graphic) }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected fun toggleSelection() {
        val frmBorder = rootView.findViewById<View>(R.id.frmBorder)
        val imgClose = rootView.findViewById<View>(R.id.imgPhotoEditorClose)
        val imgZoom = rootView.findViewById<ImageView>(R.id.imgPhotoEditorZoom)
        val imgRotate = rootView.findViewById<ImageView>(R.id.imgPhotoEditorRotate)
        if (frmBorder != null) {
            frmBorder.setBackgroundResource(R.drawable.rounded_border_tv)
            frmBorder.tag = true
        }
        if (imgClose != null) imgClose.visibility = View.VISIBLE
        if (imgZoom != null) imgZoom.visibility = View.VISIBLE

//        imgZoom?.setOnClickListener {
//            Toast.makeText(rootView.context, "Zoom Clicked", Toast.LENGTH_SHORT).show()
//        }

        var startAngle = 0f
        val rotateTouchListener = RotateTouchListener(rootView,
            object : RotateTouchListener.RotateListener {
                override fun onRotate(startAng: Float, updatedAngle: Float) {
                    startAngle = startAng
                }
            })
        imgRotate.setOnTouchListener(rotateTouchListener)

        var rotationAngle = 0f
        rotateTouchListener.rotateAngle.observe((context as LifecycleOwner)) {
//             Rotate the view based on the drag direction
            if (it > startAngle) {
                // Clockwise rotation
                rotationAngle += 5
            } else {
                // Anti-clockwise rotation
                rotationAngle -= 5
            }
            rootView.rotation = rotationAngle
        }

        imgZoom.setOnTouchListener(ZoomTouchListener(rootView))
        graphicManager?.onPhotoEditorListener?.onViewInstance(rootView)
    }

    protected fun buildGestureController(
        photoEditorView: PhotoEditorView,
        viewState: PhotoEditorViewState
    ): OnGestureControl {
        val boxHelper = BoxHelper(photoEditorView, viewState)
        return object : OnGestureControl {
            override fun onClick() {
                boxHelper.clearHelperBox()
                toggleSelection()
                // Change the in-focus view
                viewState.currentSelectedView = rootView
            }

            override fun onLongClick() {
                updateView(rootView)
            }
        }
    }

    open fun setupView(rootView: View) {}
}