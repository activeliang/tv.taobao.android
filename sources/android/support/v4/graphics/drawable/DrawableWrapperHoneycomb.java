package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableWrapperGingerbread;

@TargetApi(11)
@RequiresApi(11)
class DrawableWrapperHoneycomb extends DrawableWrapperGingerbread {
    DrawableWrapperHoneycomb(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperHoneycomb(DrawableWrapperGingerbread.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateHoneycomb(this.mState, (Resources) null);
    }

    private static class DrawableWrapperStateHoneycomb extends DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateHoneycomb(@Nullable DrawableWrapperGingerbread.DrawableWrapperState orig, @Nullable Resources res) {
            super(orig, res);
        }

        public Drawable newDrawable(@Nullable Resources res) {
            return new DrawableWrapperHoneycomb(this, res);
        }
    }
}
