package com.yunos.tvtaobao.tvsdk.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Comparator;

public class FocusFinder {
    private static ThreadLocal<FocusFinder> tlFocusFinder = new ThreadLocal<FocusFinder>() {
        /* access modifiers changed from: protected */
        public FocusFinder initialValue() {
            return new FocusFinder();
        }
    };
    Rect mBestCandidateRect = new Rect();
    ArrayList<View> mFocusables = new ArrayList<>(24);
    Rect mFocusedRect = new Rect();
    Rect mOtherRect = new Rect();
    ViewGroup mRoot;
    SequentialFocusComparator mSequentialFocusComparator = new SequentialFocusComparator();

    public static FocusFinder getInstance() {
        return tlFocusFinder.get();
    }

    public void initFocusables(ViewGroup root) {
        for (int index = 0; index < root.getChildCount(); index++) {
            View childAt = root.getChildAt(index);
            this.mFocusables.add(root.getChildAt(index));
        }
    }

    public void addFocusable(View child) {
        if (child.isFocusable()) {
            this.mFocusables.add(child);
        }
    }

    public void removeFocusable(View child) {
        this.mFocusables.remove(child);
    }

    public void clearFocusables() {
        this.mFocusables.clear();
    }

    public ViewGroup getRoot() {
        return this.mSequentialFocusComparator.getRoot();
    }

    public final View findNextFocus(ViewGroup root, View focused, int direction) {
        if (focused == null) {
            switch (direction) {
                case 1:
                case 17:
                case 33:
                    int rootBottom = root.getScrollY() + root.getHeight();
                    int rootRight = root.getScrollX() + root.getWidth();
                    this.mFocusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
                    break;
                case 2:
                case 66:
                case 130:
                    int rootTop = root.getScrollY();
                    int rootLeft = root.getScrollX();
                    this.mFocusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
                    break;
            }
        } else {
            View userSetNextFocus = findUserSetNextFocus(root, direction, focused);
            if (userSetNextFocus != null && userSetNextFocus.isFocusable() && (!userSetNextFocus.isInTouchMode() || userSetNextFocus.isFocusableInTouchMode())) {
                return userSetNextFocus;
            }
            focused.getFocusedRect(this.mFocusedRect);
            root.offsetDescendantRectToMyCoords(focused, this.mFocusedRect);
        }
        return findNextFocus(root, focused, this.mFocusedRect, direction);
    }

    /* access modifiers changed from: package-private */
    public Rect getRect(View focused) {
        Rect rect = new Rect();
        int[] location = new int[2];
        focused.getLocationOnScreen(location);
        rect.set(location[0], location[1], location[0] + focused.getWidth(), location[1] + focused.getHeight());
        return rect;
    }

    /* access modifiers changed from: package-private */
    public View findUserSetNextFocus(View root, int direction, View v) {
        switch (direction) {
            case 2:
            case 17:
                if (v.getNextFocusLeftId() != -1) {
                    return root.findViewById(v.getNextFocusLeftId());
                }
                return null;
            case 33:
                if (v.getNextFocusUpId() != -1) {
                    return root.findViewById(v.getNextFocusUpId());
                }
                return null;
            case 66:
                if (v.getNextFocusRightId() != -1) {
                    return root.findViewById(v.getNextFocusRightId());
                }
                return null;
            case 130:
                if (v.getNextFocusDownId() != -1) {
                    return root.findViewById(v.getNextFocusDownId());
                }
                return null;
            default:
                return null;
        }
    }

    public View findNextFocusFromRect(ViewGroup root, Rect focusedRect, int direction) {
        return findNextFocus(root, (View) null, focusedRect, direction);
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction) {
        ArrayList<View> focusables = this.mFocusables;
        if (focusables.isEmpty()) {
            return null;
        }
        if (this.mSequentialFocusComparator.getRoot() == null) {
            this.mSequentialFocusComparator.setRoot((ViewGroup) root.getRootView());
        }
        this.mBestCandidateRect.set(focusedRect);
        switch (direction) {
            case 1:
            case 66:
                direction = 66;
                this.mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
                break;
            case 2:
            case 17:
                direction = 17;
                this.mBestCandidateRect.offset(focusedRect.width() + 1, 0);
                break;
            case 33:
                this.mBestCandidateRect.offset(0, focusedRect.height() + 1);
                break;
            case 130:
                this.mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
                break;
        }
        View closest = null;
        int numFocusables = focusables.size();
        for (int i = 0; i < numFocusables; i++) {
            View focusable = focusables.get(i);
            if (focusable.isFocusable() && focusable != focused && focusable != root && focusable.getVisibility() == 0) {
                focusable.getFocusedRect(this.mOtherRect);
                root.offsetDescendantRectToMyCoords(focusable, this.mOtherRect);
                if (isBetterCandidate(direction, focusedRect, this.mOtherRect, this.mBestCandidateRect)) {
                    this.mBestCandidateRect.set(this.mOtherRect);
                    closest = focusable;
                }
            }
        }
        return closest;
    }

    /* access modifiers changed from: package-private */
    public boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (!isCandidate(source, rect1, direction)) {
            return false;
        }
        if (!isCandidate(source, rect2, direction) || beamBeats(direction, source, rect1, rect2)) {
            return true;
        }
        if (beamBeats(direction, source, rect2, rect1)) {
            return false;
        }
        if (getWeightedDistanceFor(majorAxisDistance(direction, source, rect1), minorAxisDistance(direction, source, rect1)) >= getWeightedDistanceFor(majorAxisDistance(direction, source, rect2), minorAxisDistance(direction, source, rect2))) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        if (beamsOverlap(direction, source, rect2) || !rect1InSrcBeam) {
            return false;
        }
        if (!isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66 || majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public float getWeightedDistanceFor(int majorAxisDistance, int minorAxisDistance) {
        float floatAxisDistance = ((float) majorAxisDistance) / 100.0f;
        float floatMinorAxisDistance = ((float) minorAxisDistance) / 100.0f;
        return (13.0f * floatAxisDistance * floatAxisDistance) + (floatMinorAxisDistance * floatMinorAxisDistance);
    }

    /* access modifiers changed from: package-private */
    public boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        switch (direction) {
            case 1:
            case 66:
                if ((srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right) {
                    if (srcRect.top <= destRect.top && srcRect.bottom >= destRect.top) {
                        return true;
                    }
                    if (srcRect.top <= destRect.bottom && srcRect.bottom >= destRect.bottom) {
                        return true;
                    }
                    if (srcRect.top >= destRect.top && srcRect.bottom <= destRect.bottom) {
                        return true;
                    }
                }
                return false;
            case 2:
            case 17:
                if ((srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left) {
                    if (srcRect.top <= destRect.top && srcRect.bottom >= destRect.top) {
                        return true;
                    }
                    if (srcRect.top <= destRect.bottom && srcRect.bottom >= destRect.bottom) {
                        return true;
                    }
                    if (srcRect.top >= destRect.top && srcRect.bottom <= destRect.bottom) {
                        return true;
                    }
                }
                return false;
            case 33:
                if ((srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top) {
                    if (srcRect.left <= destRect.left && srcRect.right >= destRect.left) {
                        return true;
                    }
                    if (srcRect.left <= destRect.right && srcRect.right >= destRect.right) {
                        return true;
                    }
                    if (srcRect.left >= destRect.left && srcRect.right <= destRect.right) {
                        return true;
                    }
                }
                return false;
            case 130:
                if ((srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom) {
                    if (srcRect.left <= destRect.left && srcRect.right >= destRect.left) {
                        return true;
                    }
                    if (srcRect.left <= destRect.right && srcRect.right >= destRect.right) {
                        return true;
                    }
                    if (srcRect.left >= destRect.left && srcRect.right <= destRect.right) {
                        return true;
                    }
                }
                return false;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        switch (direction) {
            case 1:
            case 2:
            case 17:
            case 66:
                if (rect2.bottom < rect1.top || rect2.top > rect1.bottom) {
                    return false;
                }
                return true;
            case 33:
            case 130:
                return rect2.right >= rect1.left && rect2.left <= rect1.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        switch (direction) {
            case 1:
            case 66:
                if (src.right > dest.left) {
                    return false;
                }
                return true;
            case 2:
            case 17:
                if (src.left >= dest.right) {
                    return true;
                }
                return false;
            case 33:
                if (src.top < dest.bottom) {
                    return false;
                }
                return true;
            case 130:
                return src.bottom <= dest.top;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 1:
            case 66:
                return dest.left - source.right;
            case 2:
            case 17:
                return source.left - dest.right;
            case 33:
                return source.top - dest.bottom;
            case 130:
                return dest.top - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 1:
            case 66:
                return dest.right - source.right;
            case 2:
            case 17:
                return source.left - dest.left;
            case 33:
                return source.top - dest.top;
            case 130:
                return dest.bottom - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int minorAxisDistance(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 1:
            case 2:
            case 17:
            case 66:
                return Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
            case 33:
            case 130:
                return Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
        switch (direction) {
            case 17:
                if (destRect.left > x || destRect.top > y || y > destRect.bottom) {
                    return false;
                }
                return true;
            case 33:
                if (destRect.top > y || destRect.left > x || x > destRect.right) {
                    return false;
                }
                return true;
            case 66:
                if (destRect.left < x || destRect.top > y || y > destRect.bottom) {
                    return false;
                }
                return true;
            case 130:
                return destRect.top >= y && destRect.left <= x && x <= destRect.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static final class SequentialFocusComparator implements Comparator<View> {
        private final Rect mFirstRect;
        private ViewGroup mRoot;
        private final Rect mSecondRect;

        private SequentialFocusComparator() {
            this.mFirstRect = new Rect();
            this.mSecondRect = new Rect();
        }

        public void recycle() {
            this.mRoot = null;
        }

        public void setRoot(ViewGroup root) {
            this.mRoot = root;
        }

        public ViewGroup getRoot() {
            return this.mRoot;
        }

        public int compare(View first, View second) {
            if (first == second) {
                return 0;
            }
            getRect(first, this.mFirstRect);
            getRect(second, this.mSecondRect);
            if (this.mFirstRect.top < this.mSecondRect.top) {
                return -1;
            }
            if (this.mFirstRect.top > this.mSecondRect.top) {
                return 1;
            }
            if (this.mFirstRect.left < this.mSecondRect.left) {
                return -1;
            }
            if (this.mFirstRect.left > this.mSecondRect.left) {
                return 1;
            }
            if (this.mFirstRect.bottom < this.mSecondRect.bottom) {
                return -1;
            }
            if (this.mFirstRect.bottom > this.mSecondRect.bottom) {
                return 1;
            }
            if (this.mFirstRect.right < this.mSecondRect.right) {
                return -1;
            }
            if (this.mFirstRect.right > this.mSecondRect.right) {
                return 1;
            }
            return 0;
        }

        private void getRect(View view, Rect rect) {
            view.getDrawingRect(rect);
            this.mRoot.offsetDescendantRectToMyCoords(view, rect);
        }
    }
}
