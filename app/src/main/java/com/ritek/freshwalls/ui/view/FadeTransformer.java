package com.ritek.freshwalls.ui.view;

import android.animation.ObjectAnimator;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

public class FadeTransformer implements ViewPager.PageTransformer {





        public void transformPage(View view, float position) {
            if(position <= -1.0F || position >= 1.0F) {

            } else if( position == 0.0F ) {


            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setTranslationX(view.getWidth() * -position);

                ObjectAnimator anim = ObjectAnimator.ofFloat(view,"alpha",1.0F - Math.abs(position));
                anim.setDuration(500); // duration 3 seconds
                anim.start();
            }

        }


}
