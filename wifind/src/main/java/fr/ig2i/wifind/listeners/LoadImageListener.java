package fr.ig2i.wifind.listeners;

import android.graphics.Bitmap;

import fr.ig2i.wifind.objects.Image;

/**
 * Created by Thomas on 02/01/2016.
 */
public interface LoadImageListener {
    public void onBitmapLoaded(Image image, Bitmap bmp);
}
