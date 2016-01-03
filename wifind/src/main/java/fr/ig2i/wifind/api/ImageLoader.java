package fr.ig2i.wifind.api;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.ig2i.wifind.listeners.LoadImageListener;
import fr.ig2i.wifind.objects.Image;

/**
 * Created by Thomas on 02/01/2016.
 */
public class ImageLoader {

    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private Image img;

        public LoadImageTask(Image image, LoadImageListener listener) {
            this.img = image;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return ImageLoader.this.loadBitmap(this.img);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            listener.onBitmapLoaded(this.img, bitmap);
        }
    }


    private Context ctx;
    private LoadImageListener listener;

    public static Bitmap.CompressFormat COMPRESS_FORMAT     = Bitmap.CompressFormat.PNG;
    public static int                   COMPRESS_QUALITY    = 100;

    public static String IMG_INTERNAL_NAME      = "cachedimg-%.3s-%s.%s";
    public static String IMG_INTERNAL_EXTENSION = COMPRESS_FORMAT.equals(Bitmap.CompressFormat.JPEG) ? "jpeg" : (COMPRESS_FORMAT.equals(Bitmap.CompressFormat.PNG) ? "png" : "webp");
    public static String IMG_INTERNAL_DIR       = "images";

    public static String HASH_ALGORITHM = "md5";

    public void asyncLoad(Image img) {
        new LoadImageTask(img, listener).execute((Void) null);
    }

    private Bitmap loadBitmap(Image image) {

        Bitmap bmp = null;

        if(this.isInInternalStorage(image)) {
            bmp = this.loadFromInternalStorage(image);
        } else {
            bmp = this.loadFromURL(image);

            if(this.isInInternalStorage(image)) {
                this.deleteFromInternalStorage(image);
            }

            image.setHash(this.computeHash(bmp));
            this.saveToInternalStorage(image, bmp);
        }

        return bmp;
    }

    public ImageLoader(Context context, LoadImageListener listener) {
        this.ctx = context;
        this.listener = listener;
    }

    /**
     *
     * @param bmp
     * @return
     */
    private String computeHash(Bitmap bmp) {

        StringBuilder sb = new StringBuilder();
        MessageDigest md = null;
        byte[] bytes;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        bmp.compress(COMPRESS_FORMAT, COMPRESS_QUALITY, stream);

        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException exc) {
        }

        bytes = md.digest(stream.toByteArray());
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        try {
            stream.close();
        } catch (IOException exc){

        }

        return sb.toString().toLowerCase();
    }

    /**
     *
     * @param name
     * @param hash
     * @return
     */
    private String computeInternalStorageName(String name, String hash) {
        return String.format(IMG_INTERNAL_NAME, name.substring(name.lastIndexOf("/") + 1), hash.toLowerCase(), IMG_INTERNAL_EXTENSION);
    }

    /**
     *
     * @param image
     * @return
     */
    private boolean isInInternalStorage(Image image) {
        return this.getInternalStorageFile(image.getChemin(), image.getHash()).exists();
    }

    private Bitmap loadFromURL(Image image) {
        //Chargement + sauvegarde
        Bitmap bmp = null;
        URL url = null;

        try {
            url = new URL("http:" + image.getChemin());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.d("ImageLoader", "Loaded from URL");
        } catch (Exception exc) {
            Log.e("ImageLoader", exc.getMessage());
        }

        return bmp;
    }

    private Bitmap loadFromInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), image.getHash());
        Bitmap bmp = null;

        if(imageFile.exists()) {
            try {
                bmp = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                Log.d("ImageLoader", "Loaded from internal storage");
            } catch (FileNotFoundException exc) {
                Log.e("ImageLoader", "Unable to find file");
            }
        }
        return bmp;
    }

    private File getInternalStorageFile(String fileLocation, String hash) {
        ContextWrapper cw = new ContextWrapper(this.ctx);
        File imageDir = cw.getDir(IMG_INTERNAL_DIR, Context.MODE_PRIVATE),
             imageFile;

        if(fileLocation != null) {
            imageFile = new File(imageDir, this.computeInternalStorageName(fileLocation, hash));
            return imageFile;
        } else {
            throw new InvalidParameterException("Bad file location");
        }
    }

    private void saveToInternalStorage(Image image, Bitmap bmp) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), this.computeHash(bmp));
        FileOutputStream stream = null;

        if(!imageFile.exists()) {
            Log.d("ImageLoader", "Saving to internal storage");
            try {
                stream = new FileOutputStream(imageFile);
                bmp.compress(COMPRESS_FORMAT, COMPRESS_QUALITY, stream);
            } catch (FileNotFoundException exc) {
                Log.e("ImageLoader", "File not found");
            } finally {
                try {
                    stream.close();
                    Log.d("ImageLoader", "Saved to internal storage");
                } catch (IOException exc) {
                    Log.e("ImageLoader", "Unable to close stream");
                }
            }
        }
    }

    private void deleteFromInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), image.getHash());

        if(imageFile.exists()) {
            imageFile.delete();
            Log.d("ImageLoader", "Deleted from internal storage");
        }
    }
}
