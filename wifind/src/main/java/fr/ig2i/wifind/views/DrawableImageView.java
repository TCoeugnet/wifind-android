package fr.ig2i.wifind.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import fr.ig2i.wifind.activities.R;

/**
 * Created by Thomas on 16/12/2015.
 */
public class DrawableImageView extends ImageView {

    private boolean isPinShown = true;
    private boolean isPinLocked = false;
    private PointF pinPosition = new PointF(0, 0);
    private Bitmap pinImg = BitmapFactory.decodeResource(getResources(), R.drawable.fighting_64);

    public void showPin() {
        this.isPinShown = true;
        this.invalidate();
    }

    public void hidePin() {
        this.isPinShown = false;
        this.invalidate();
    }

    public void lockPin() {
        this.isPinLocked = true;
    }

    public void unlockPin() {
        this.isPinLocked = false;
    }

    public void movePin(float x, float y) {
        this.pinPosition.set(x + this.getScrollX() - (this.getWidth()) / 2, y + this.getScrollY() - (this.getHeight()) / 2);
        this.invalidate();
    }

    /**
     * Returns current pin position (in pixels)
     * @return PointF pin position
     */
    public PointF getPinPosition() {
        PointF pos = new PointF();
        pos.set(pinPosition);

        return pos;
    }

    public DrawableImageView(Context context) {
        super(context);
    }

    public DrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private PointF calculatePhysicalPosition() {
        PointF phys = new PointF();
        phys.x = pinPosition.x + (this.getWidth() - pinImg.getWidth()) / 2;
        phys.y = pinPosition.y + (this.getHeight() - pinImg.getHeight()) / 2;

        return phys;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if(isPinShown) {
            PointF physicalPosition = this.calculatePhysicalPosition();
            canvas.drawBitmap(pinImg, physicalPosition.x, physicalPosition.y, null);
        }

    }
}
