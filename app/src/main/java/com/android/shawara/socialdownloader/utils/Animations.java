package com.android.shawara.socialdownloader.utils;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by shawara on 7/25/2017.
 */

public final class Animations {

    public static void rotateImageAnimation(ImageView imageView) {

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds

        //Start animation
        imageView.startAnimation(anim);
    }
}
