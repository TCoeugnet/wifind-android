package fr.ig2i.wifind.detectors;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by Thomas on 15/12/2015.
 */
public class ImageGestureDetector extends SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Log.i("GESTURE", "Fling");

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("GESTURE", "Swipe");
        return false;
    }
}
