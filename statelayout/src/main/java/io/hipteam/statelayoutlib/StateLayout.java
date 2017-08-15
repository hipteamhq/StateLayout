package io.hipteam.statelayoutlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;

public class StateLayout extends FrameLayout {

    private static final int ANIMATION_TIME = 600;
    private static final int MINIMUM_LOADING_TIME_TO_ANIMATION = 1000;

    private int loadingViewResId;
    private int errorViewResId;
    private int errorTextId;
    private int emptyViewResId;
    private int emptyTextId;

    private View contentView;
    private View loadingView;
    private View errorView;
    private View emptyView;

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

    /////////////

    public void showContent() {
        if (contentView.getVisibility() != VISIBLE) {
            hideLoading();
            hideError();
            hideEmpty();
            contentView.setAlpha(0f);
            contentView.setVisibility(VISIBLE);
            contentView.animate().alpha(1f).setDuration(getAnimTime()).start();
        }
    }

    public void hideContent() {
        contentView.setVisibility(INVISIBLE);
    }

    /////////////

    public void showLoading() {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(getContext()).inflate(loadingViewResId, null);
        }
        hideContent();
        hideError();
        hideEmpty();
        if (loadingView.getParent() == null) {
            addView(loadingView);
        }
        loadingStarted = new Date().getTime();
    }

    public void hideLoading() {
        if (loadingView != null) {
            removeView(loadingView);
            loadingView = null;
        }
    }

    /////////////

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
        hideContent();
        hideLoading();
        hideEmpty();
        if (errorView.getParent() == null) {
            errorView.setAlpha(0f);
            errorView.setVisibility(VISIBLE);
            addView(errorView);
            errorView.animate().alpha(1f).setDuration(getAnimTime()).start();
        }
    }

    public void hideError() {
        if (errorView != null) {
            removeView(errorView);
            errorView = null;
        }
    }

    public View getErrorView() {
        return errorView;
    }

    /////////////

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
        hideContent();
        hideLoading();
        hideError();
        if (emptyView.getParent() == null) {
            emptyView.setAlpha(0f);
            emptyView.setVisibility(VISIBLE);
            addView(emptyView);
            emptyView.animate().alpha(1f).setDuration(getAnimTime()).start();
        }
    }

    public void hideEmpty() {
        if (emptyView != null) {
            removeView(emptyView);
            emptyView = null;
        }
    }


    private long getAnimTime() {
        // If the loading time is bigger then the minimum then show the content with animation
        return (new Date().getTime() - loadingStarted) < MINIMUM_LOADING_TIME_TO_ANIMATION ? 0 : ANIMATION_TIME;
    }

}
