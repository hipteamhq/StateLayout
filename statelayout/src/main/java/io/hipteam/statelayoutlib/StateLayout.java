package io.hipteam.statelayoutlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;

public class StateLayout extends FrameLayout {

    private int loadingViewResId;
    private View loadingView;
    private int errorViewResId;
    private int errorTextId;
    private View errorView;
    private int emptyViewResId;
    private int emptyTextId;
    private View emptyView;
    private View contentView;
    private int animationTime = 600;
    private int minimumLoadingTimeToAnimation = 1000;
    private int minimumLoadingTimeDistanceBetweenAnimations = 5000;
    private long lastAnimationTime = 0L;
    private long loadingStarted = 0L;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        try {
            loadingViewResId = a.getResourceId(R.styleable.StateLayout_loading_view_layout, R.layout.layout_state_layout_default_loading_view);
            errorViewResId = a.getResourceId(R.styleable.StateLayout_error_view_layout, R.layout.layout_state_layout_default_error_view);
            errorTextId = a.getResourceId(R.styleable.StateLayout_error_text, R.string.all_loading_error);
            emptyViewResId = a.getResourceId(R.styleable.StateLayout_empty_view_layout, R.layout.layout_state_layout_default_empty_view);
            emptyTextId = a.getResourceId(R.styleable.StateLayout_empty_text, R.string.all_no_content);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("StateLayout can host only one direct child");
        }
        contentView = getChildAt(0);
    }

    // *** CONTENT VIEW SECTION *** ///

    public void showContent() {
        if (contentView.getVisibility() != VISIBLE) {
            removeLoadingView();
            removeErrorView();
            removeEmptyView();
            //contentView.setAlpha(0f);
            contentView.setVisibility(VISIBLE);
//            contentView
//                    .animate()
//                    .alpha(1f)
//                    .setDuration(getAnimTime())
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            lastAnimationTime = new Date().getTime();
//                        }
//                    })
//                    .start();
        }
    }

    public void hideContentView() {
        contentView.setVisibility(INVISIBLE);
    }

    public View getContentView() {
        return contentView;
    }

    /// *** LOADING VIEW SECTION *** ///

    public void setLoadingViewResId(int loadingViewResId) {
        this.loadingViewResId = loadingViewResId;
    }

    public void setLoadingView(View loadingView) {
        if (this.loadingView != null) {
            removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        loadingViewResId = -1;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void showLoading() {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(getContext()).inflate(loadingViewResId, null);
        }
        hideContentView();
        removeErrorView();
        removeEmptyView();
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        loadingStarted = new Date().getTime();
    }

    public void removeLoadingView() {
        if (loadingView != null) {
            removeView(loadingView);
            if (loadingViewResId != -1) {
                loadingView = null;
            }
        }
    }


    /// *** ERROR VIEW SECTION *** ///

    public void setErrorViewResId(int errorViewResId) {
        this.errorViewResId = errorViewResId;
    }

    public void setErrorView(View errorView) {
        if (this.errorView != null) {
            removeView(this.errorView);
        }
        this.errorView = errorView;
        errorViewResId = -1;
    }

    public View getErrorView() {
        return errorView;
    }

    public void showError(int errorTextId) {
        this.errorTextId = errorTextId;
        showError();
    }

    public void showError() {
        if (errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(errorViewResId, null);
        }
        TextView errorText = (TextView) errorView.findViewById(R.id.layout_state_layout_default_error_view_text);
        if (errorText != null) {
            errorText.setText(errorTextId);
        }
        hideContentView();
        removeLoadingView();
        removeEmptyView();
        if (errorView.getParent() == null) {
            //errorView.setAlpha(0f);
            errorView.setVisibility(VISIBLE);
            addView(errorView);
            //errorView.animate().alpha(1f).setDuration(getAnimTime()).start();
        }
    }

    public void removeErrorView() {
        if (errorView != null) {
            removeView(errorView);
            if (errorViewResId != -1) {
                errorView = null;
            }
        }
    }


    /// *** EMPTY VIEW SECTION *** ///


    public void setEmptyViewResId(int emptyViewResId) {
        this.emptyViewResId = emptyViewResId;
    }

    public void setEmptyView(View emptyView) {
        if (this.emptyView != null) {
            removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        emptyViewResId = -1;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void showEmpty(int emptyTextId) {
        this.emptyTextId = emptyTextId;
        showEmpty();
    }

    public void showEmpty() {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(emptyViewResId, null);
        }
        TextView emptyText = (TextView) emptyView.findViewById(R.id.layout_state_layout_default_empty_view_text);
        if (emptyText != null) {
            emptyText.setText(emptyTextId);
        }
        hideContentView();
        removeLoadingView();
        removeErrorView();
        if (emptyView.getParent() == null) {
            //emptyView.setAlpha(0f);
            emptyView.setVisibility(VISIBLE);
            addView(emptyView);
            //emptyView.animate().alpha(1f).setDuration(getAnimTime()).start();
        }
    }

    public void removeEmptyView() {
        if (emptyView != null) {
            removeView(emptyView);
            if (emptyViewResId != -1) {
                emptyView = null;
            }
        }
    }


    /// *** ANIMATION *** ///


    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public void setMinimumLoadingTimeToAnimation(int minimumLoadingTimeToAnimation) {
        this.minimumLoadingTimeToAnimation = minimumLoadingTimeToAnimation;
    }

    private long getAnimTime() {
        // If the loading time is bigger then the minimum then show the content with animation
        long now = new Date().getTime();
        return ((now - loadingStarted) < minimumLoadingTimeToAnimation)
                || (now - lastAnimationTime < minimumLoadingTimeDistanceBetweenAnimations) ? 0 : animationTime;
    }

    private void log(String s) {
        Log.d("STL", s);
    }
}
