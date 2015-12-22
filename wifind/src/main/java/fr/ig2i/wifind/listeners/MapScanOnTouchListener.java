package fr.ig2i.wifind.listeners;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import fr.ig2i.wifind.views.DrawableImageView;

/**
 * Created by Thomas on 16/12/2015.
 */
public class MapScanOnTouchListener extends ScrollableOnTouchListener{

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        DrawableImageView img = (DrawableImageView) v;

        if(this.isLocked()) {
            img.showPin();
            img.movePin(event.getX(), event.getY());
        }

        return true;
    }
}
