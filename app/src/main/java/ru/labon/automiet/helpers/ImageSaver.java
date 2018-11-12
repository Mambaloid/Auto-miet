package ru.labon.automiet.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Admin on 10.10.2017.
 */

public class ImageSaver {
/*


*/


    private static final String DIRECTORY_NAME ="img";


    public static String saveImage(Bitmap bitmapImage, Context context, String name){


        Bitmap theBitmap = null;
//        try {
//            theBitmap = Glide.
//                    with(context).
//                    load("Url of your image").
//                    asBitmap().
//                    into(-1, -1).
//                    get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

         //Folder name in device android/data/
        File directory = cw.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);

        // Create imageDir
        File mypath=new File(directory,name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("absolutepath ", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    /** Method to retrieve image from your device **/

    public Bitmap loadImage(String path, String name)
    {
        Bitmap b;
        try {
            File f=new File(path, DIRECTORY_NAME);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /** Retrieve your image from device and set to imageview **/
//Provide your image path and name of the image your previously used.


}
