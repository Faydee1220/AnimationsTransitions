package com.teamtreehouse.albumcover.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Faydee on 2018/3/1.
 */

public class Fold extends Visibility {
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createFoldAnimator(view, false);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createFoldAnimator(view, true);
    }

    private Animator createFoldAnimator(View view, boolean folding) {
        int start = view.getTop();
        int end = view.getTop() + view.getMeasuredHeight() - 1;
        if (folding) {
            int temp = start;
            start = end;
            end = temp;
        }
        view.setBottom(start);
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "bottom", start, end);
        return animator;
    }



}














