package ru.ksu.motygullin.gallery;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by UseR7 on 06.10.2016.
 */

public class GalleryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
