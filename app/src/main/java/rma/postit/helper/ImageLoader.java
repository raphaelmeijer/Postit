package rma.postit.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class ImageLoader extends AsyncTask<Object, Object, Bitmap> {
    private ImageView imageView;
    private String url;

    public ImageLoader(ImageView fImage, String fPictureName){
        imageView = fImage;
        url = fPictureName;
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create bmp
        // return the bitmap
        return bmp;
    }

    @Override
    protected void onPostExecute( Bitmap bitmap ){
        if( imageView != null )
            imageView.setImageBitmap(bitmap);
        //Log.d(Globals.GLOBAL_TAG, "logo loaded from cache: " + bitmap);
    }
}