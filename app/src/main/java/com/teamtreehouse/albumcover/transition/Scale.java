package com.teamtreehouse.albumcover.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Faydee on 2018/3/1.
 */

public class Scale extends Visibility {

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createScaleAnimator(view, 0, 1);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createScaleAnimator(view, 1, 0);
    }

    private Animator createScaleAnimator(View view, float fromScale, float toScale) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator x = ObjectAnimator.ofFloat(view, View.SCALE_X, fromScale, toScale);
        ObjectAnimator y = ObjectAnimator.ofFloat(view, View.SCALE_Y, fromScale, toScale);
        animatorSet.playTogether(x, y);
        return animatorSet;
    }

}
