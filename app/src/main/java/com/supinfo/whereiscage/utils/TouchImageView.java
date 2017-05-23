package com.supinfo.whereiscage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.supinfo.whereiscage.ChronoModeOne;
import com.supinfo.whereiscage.R;

import java.util.Calendar;

/**
 * Created by fairuz on 17/03/17.
 */

public class TouchImageView extends android.support.v7.widget.AppCompatImageView {

    Matrix matrix;

    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime = 0;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = -3f;
    float maxScale = 3f;
    float[] m;

    int viewWidth, viewHeight;
    static final int CLICK = 5;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;

    Context context;
    private Bitmap myBitmap;
    private int image;
    private float mScaleFactor;
    private Rect rect;
    private TouchImageView imageView;

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    private void sharedConstructing(final Context context) {
        super.setClickable(true);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener() {

            private float deltaX,deltaY,fixTransX,fixTransY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last.set(curr);
                        start.set(last);
                        mode = DRAG;

                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                             deltaX = curr.x - last.x;
                             deltaY = curr.y - last.y;
                             fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                             fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);

                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        System.out.println("xDiff "+xDiff+" yDiff "+yDiff+" delta X: "+deltaX+" delta Y: "+deltaY);
                        if (xDiff < CLICK || yDiff < CLICK) {//performClick();
                            PointF sCoord = new PointF(event.getX(),event.getY());

                            System.out.println("Coordonnée "+sCoord+" x: "+sCoord.x +" && "+ sCoord.y);

                            Toast.makeText(context," your x: "+sCoord.x+" your y : "+sCoord.y, Toast.LENGTH_SHORT).show();
                        }



                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                       /* if(clickDuration < MAX_CLICK_DURATION) {
                            //click event has occurred
                            String text = "You click at x = " + event.getX() + " and y = " + event.getY();
                            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                        }*/

                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    public void setBitmap(Context context,int img){
        this.image = img;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
         myBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                img,
                options);
    }

    public void setImageView(TouchImageView imageView) {
        this.imageView = imageView;
    }

    public TouchImageView getImageView() {
        return imageView;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            System.out.println("original: "+origScale+" scalfactor: "+mScaleFactor);
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);
        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect = canvas.getClipBounds();
    }

}