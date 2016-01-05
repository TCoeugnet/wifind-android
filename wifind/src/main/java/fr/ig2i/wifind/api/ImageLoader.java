package fr.ig2i.wifind.api;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    //TODO : Gestion des images lourdes

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
    public static int                   COMPRESS_QUALITY    = 50;

    public static String IMG_INTERNAL_NAME      = "cachedimg-%.3s-%s.%s";
    public static String IMG_INTERNAL_EXTENSION = COMPRESS_FORMAT.equals(Bitmap.CompressFormat.JPEG) ? "jpeg" : (COMPRESS_FORMAT.equals(Bitmap.CompressFormat.PNG) ? "png" : "webp");
    public static String IMG_INTERNAL_DIR       = "images";

    public static String HASH_ALGORITHM = "md5";

    public void asyncLoad(Image img) {
        new LoadImageTask(img, listener).execute((Void) null);
    }

    private Bitmap loadBitmap(Image image) {

        Bitmap bmp = null;
        byte[] data = null;

        if(this.isInInternalStorage(image)) {
            Log.d("ImageLoader", "Internal");
            bmp = this.loadFromInternalStorage(image);
        } else {
            bmp = this.loadFromURL(image);
            Log.d("ImageLoader", "URL");

            if(bmp != null) {
                if (this.isInInternalStorage(image)) {
                    this.deleteFromInternalStorage(image); //TODO On ne viendra jamais ici
                }
                image.setHash(this.computeHash(image.getData()));

                Log.d("ImageLoader", "Hash : " + image.getHash());
                this.saveToInternalStorage(image, bmp);
            }
        }

        return bmp;
    }

    public ImageLoader(Context context, LoadImageListener listener) {
        this.ctx = context;
        this.listener = listener;
    }

    /**
     *
     * @param data
     * @return
     */
    private String computeHash(byte[] data) {

        StringBuilder sb = new StringBuilder();
        MessageDigest md = null;
        byte[] bytes;
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //mp.compress(COMPRESS_FORMAT, COMPRESS_QUALITY, stream);

        try {
            md = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException exc) {
        }


        bytes = md.digest(data);
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }

        /*try {
            //stream.close();
        } catch (IOException exc){

        }*/

        String result = sb.toString().toLowerCase();
        return result;
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
        byte[] data = new byte[4096];
        int length = 0;

        try {
            url = new URL(image.getChemin());
            InputStream stream = url.openConnection().getInputStream();

            ByteArrayBuffer buffer = new ByteArrayBuffer(4096);
            while((length = stream.read(data, 0, data.length)) != -1) {
                buffer.append(data, 0, length);
            }
            bmp = BitmapFactory.decodeByteArray(buffer.buffer(), 0, buffer.length());

            image.setData(buffer.toByteArray());
            buffer.clear();

        } catch (Exception exc) {
            Log.e("ImageLoader", "", exc);
        }

        return bmp;
    }

    private Bitmap loadFromInternalStorage(Image image) {
        File imageFile = this.getInternalStorageFile(image.getChemin(), image.getHash());
        Bitmap bmp = null;

        if(imageFile.exists()) {
            try {
                bmp = BitmapFactory.decodeStream(new FileInputStream(imageFile));
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
        File imageFile = this.getInternalStorageFile(image.getChemin(), this.computeHash(image.getData()));
        FileOutputStream stream = null;

        if(!imageFile.exists()) {
            try {
                stream = new FileOutputStream(imageFile);

                stream.write(image.getData());
            } catch (FileNotFoundException exc) {
                Log.e("ImageLoader", "File not found");
            } catch (IOException exc) {
                Log.e("ImageLoader", "Unable to write data");
            } finally {
                try {
                    stream.close();
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
