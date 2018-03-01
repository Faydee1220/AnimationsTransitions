package com.teamtreehouse.albumcover;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.teamtreehouse.albumcover.transition.Fold;
import com.teamtreehouse.albumcover.transition.Scale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlbumDetailActivity extends Activity {

    public static final String EXTRA_ALBUM_ART_RESID = "EXTRA_ALBUM_ART_RESID";

    @BindView(R.id.album_art) ImageView albumArtView;
    @BindView(R.id.fab) ImageButton fab; // fab 代表 Floating Action Button，右下方浮在畫面上的圓形按鈕
    @BindView(R.id.title_panel) ViewGroup titlePanel;
    @BindView(R.id.track_panel) ViewGroup trackPanel;
    @BindView(R.id.detail_container) ViewGroup detailContainer;

    private TransitionManager transitionManager;
    private Scene expandedScene;
    private Scene collapsedScene;
    private Scene currentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        ButterKnife.bind(this);
        populate();
        setupTransitions();
    }

    private Transition createTransition() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        Transition transitionFab = new Scale();
        transitionFab.setDuration(150);
        transitionFab.addTarget(fab);

        Transition transitionTitle = new Fold();
        transitionTitle.setDuration(150);
        transitionTitle.addTarget(titlePanel);

        Transition transitionTrack = new Fold();
        transitionTrack.setDuration(150);
        transitionTrack.addTarget(trackPanel);

        transitionSet.addTransition(transitionTrack);
        transitionSet.addTransition(transitionTitle);
        transitionSet.addTransition(transitionFab);

        return transitionSet;
    }

//    private void animate() {
////        fab.setScaleX(0);
////        fab.setScaleY(0);
////        fab.animate().scaleX(1).scaleY(1).start();
//
//        // Java code 方式
////        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1);
////        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 0, 1);
////        AnimatorSet scaleFab = new AnimatorSet();
////        scaleFab.playTogether(scaleX, scaleY);
//
//        // 利用 xml 檔案方式
//        Animator scaleFab = AnimatorInflater.loadAnimator(this, R.animator.scale);
//        scaleFab.setTarget(fab);
//
//        int titleStartValue = titlePanel.getTop();
//        int titleEndValue = titlePanel.getBottom();
//        ObjectAnimator animatorTitle = ObjectAnimator.ofInt(titlePanel, "bottom", titleStartValue, titleEndValue);
//        animatorTitle.setInterpolator(new AccelerateInterpolator());
//
//        int trackStartValue = trackPanel.getTop();
//        int trackEndValue = trackPanel.getBottom();
//        ObjectAnimator animatorTrack = ObjectAnimator.ofInt(trackPanel, "bottom", trackStartValue, trackEndValue);
//        animatorTrack.setInterpolator(new DecelerateInterpolator());
//
//        // 先隱藏
//        titlePanel.setBottom(titleStartValue);
//        trackPanel.setBottom(titleStartValue);
//        fab.setScaleX(0);
//        fab.setScaleY(0);
//
//        // 動畫時間，毫秒
////        animatorTitle.setDuration(1000);
////        animatorTrack.setDuration(1000);
////        animatorTitle.setStartDelay(1000);
//
//        // 動畫順序
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playSequentially(animatorTitle, animatorTrack, scaleFab);
//        animatorSet.start();
//    }

    @OnClick(R.id.album_art)
    public void onAlbumArtClick(View view) {
//        animate();
        Transition transition = createTransition();
        TransitionManager.beginDelayedTransition(detailContainer, transition);
        fab.setVisibility(View.INVISIBLE);
        titlePanel.setVisibility(View.INVISIBLE);
        trackPanel.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.track_panel)
    public void onTrackPanelClick(View view) {
        if (currentScene == expandedScene) {
            currentScene = collapsedScene;
        }
        else {
            currentScene = expandedScene;
        }
        transitionManager.transitionTo(currentScene);
    }

    private void setupTransitions() {
        // 進入時從右方滑入
//        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
//
//        // 返回時淡出
//        getWindow().setReturnTransition(new Fade());

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.excludeTarget(android.R.id.statusBarBackground, true); // 避免狀態列跳動
        getWindow().setEnterTransition(slide);
        getWindow().setSharedElementsUseOverlay(false); // 避免浮動按鈕被蓋住

        transitionManager = new TransitionManager();
        ViewGroup transitionRoot = detailContainer;


        // Expanded scene
        expandedScene = Scene.getSceneForLayout(transitionRoot,
                R.layout.activity_album_detail_expanded,
                this);
//        TransitionManager.go(expandedScene, new ChangeBounds());

        expandedScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                // 因為舊的 view 會移除，要重新綁定
                ButterKnife.bind(AlbumDetailActivity.this);
                populate();
                currentScene = expandedScene;
            }
        });

        TransitionSet expandTransitionSet = new TransitionSet();
        expandTransitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(200);
        expandTransitionSet.addTransition(changeBounds);

        Fade fadeLyrics = new Fade();
        fadeLyrics.addTarget(R.id.lyrics);
        fadeLyrics.setDuration(150);
        expandTransitionSet.addTransition(fadeLyrics);

        //        TransitionManager.go(expandedScene, expandTransitionSet);

        // Collapsed scene
        collapsedScene = Scene.getSceneForLayout(transitionRoot,
                R.layout.activity_album_detail,
                this);
//        TransitionManager.go(expandedScene, new ChangeBounds());

        collapsedScene.setEnterAction(new Runnable() {
            @Override
            public void run() {
                // 因為舊的 view 會移除，要重新綁定
                ButterKnife.bind(AlbumDetailActivity.this);
                populate();
                currentScene = collapsedScene;
            }
        });

        TransitionSet collapseTransitionSet = new TransitionSet();
        collapseTransitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        Fade fadeOutLyrics = new Fade();
        fadeOutLyrics.addTarget(R.id.lyrics);
        fadeOutLyrics.setDuration(150);
        collapseTransitionSet.addTransition(fadeOutLyrics);

        ChangeBounds resetBounds = new ChangeBounds();
        resetBounds.setDuration(200);
        collapseTransitionSet.addTransition(resetBounds);

        transitionManager.setTransition(expandedScene, collapsedScene, collapseTransitionSet);
        transitionManager.setTransition(collapsedScene, expandedScene, expandTransitionSet);

        // 起始畫面（尚未有任何動畫前）要記得呼叫 enter()，
        collapsedScene.enter();

        // 遞延動畫
//        postponeEnterTransition();
    }

    private void populate() {
        // 模擬從網路下載大圖需要時間的延遲狀況
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int albumArtResId = getIntent().getIntExtra(EXTRA_ALBUM_ART_RESID, R.drawable.mean_something_kinder_than_wolves);
//                albumArtView.setImageResource(albumArtResId);
//
//                Bitmap albumBitmap = getReducedBitmap(albumArtResId);
//                colorizeFromImage(albumBitmap);
//
//                // 開始被遞延的動畫
//                startPostponedEnterTransition();
//            }
//        }, 1000);

        int albumArtResId = getIntent().getIntExtra(EXTRA_ALBUM_ART_RESID, R.drawable.mean_something_kinder_than_wolves);
        albumArtView.setImageResource(albumArtResId);

        Bitmap albumBitmap = getReducedBitmap(albumArtResId);
        colorizeFromImage(albumBitmap);
    }

    private Bitmap getReducedBitmap(int albumArtResId) {
        // reduce image size in memory to avoid memory errors
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;
        return BitmapFactory.decodeResource(getResources(), albumArtResId, options);
    }

    private void colorizeFromImage(Bitmap image) {
        Palette palette = Palette.from(image).generate();

        // set panel colors
        int defaultPanelColor = 0xFF808080;
        int defaultFabColor = 0xFFEEEEEE;
        titlePanel.setBackgroundColor(palette.getDarkVibrantColor(defaultPanelColor));
        trackPanel.setBackgroundColor(palette.getLightMutedColor(defaultPanelColor));

        // set fab colors
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed}
        };

        int[] colors = new int[]{
                palette.getVibrantColor(defaultFabColor),
                palette.getLightVibrantColor(defaultFabColor)
        };
        fab.setBackgroundTintList(new ColorStateList(states, colors));
    }
}
