package ru.labon.automiet.helpers;

import android.graphics.drawable.Animatable;
import android.util.Log;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

public class ImageListener extends BaseControllerListener<ImageInfo> {
    @Override
    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        Log.i("DraweeUpdate", "Image is fully loaded!");
    }

    @Override
    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
        Log.i("DraweeUpdate", "Image is partly loaded! (maybe it's a progressive JPEG?)");
        if (imageInfo != null) {
            int quality = imageInfo.getQualityInfo().getQuality();
            Log.i("DraweeUpdate", "Image quality (number scans) is: " + quality);
        }
    }

    @Override
    public void onFailure(String id, Throwable throwable) {
        Log.i("DraweeUpdate", "Image failed to load: " + throwable.getMessage());
    }
}
