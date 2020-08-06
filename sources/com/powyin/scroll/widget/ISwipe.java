package com.powyin.scroll.widget;

import com.powyin.scroll.widget.SwipeController;

public interface ISwipe {

    public enum FreshStatus {
        ERROR,
        ERROR_NET,
        SUCCESS
    }

    public enum LoadedStatus {
        ERROR,
        NO_MORE
    }

    public interface OnRefreshListener {
        void onLoading();

        void onRefresh();
    }

    void completeLoadMore();

    void enableEmptyView(boolean z);

    void enableLoadMoreOverScroll(boolean z);

    void refresh();

    void setEmptyController(EmptyController emptyController);

    void setFreshResult(FreshStatus freshStatus);

    void setLoadMoreResult(LoadedStatus loadedStatus);

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    void setSwipeController(SwipeController swipeController);

    void setSwipeModel(SwipeController.SwipeModel swipeModel);
}
