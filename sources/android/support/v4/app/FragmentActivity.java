package android.support.v4.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompatApi23;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.bftv.fui.constantplugin.Constant;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import mtopsdk.common.util.SymbolExpUtil;
import org.apache.commons.codec.language.Soundex;

public class FragmentActivity extends BaseFragmentActivityJB implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator {
    static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
    static final String FRAGMENTS_TAG = "android:support:fragments";
    static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
    static final int MSG_REALLY_STOPPED = 1;
    static final int MSG_RESUME_PENDING = 2;
    static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
    static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
    private static final String TAG = "FragmentActivity";
    boolean mCreated;
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (FragmentActivity.this.mStopped) {
                        FragmentActivity.this.doReallyStop(false);
                        return;
                    }
                    return;
                case 2:
                    FragmentActivity.this.onResumeFragments();
                    FragmentActivity.this.mFragments.execPendingActions();
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    int mNextCandidateRequestIndex;
    boolean mOptionsMenuInvalidated;
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    boolean mReallyStopped = true;
    boolean mRequestedPermissionsFromFragment;
    boolean mResumed;
    boolean mRetaining;
    boolean mStopped = true;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    @RequiresApi(16)
    public /* bridge */ /* synthetic */ void startActivityForResult(Intent intent, int i, @Nullable Bundle bundle) {
        super.startActivityForResult(intent, i, bundle);
    }

    public /* bridge */ /* synthetic */ void startIntentSenderForResult(IntentSender intentSender, int i, @Nullable Intent intent, int i2, int i3, int i4) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4);
    }

    @RequiresApi(16)
    public /* bridge */ /* synthetic */ void startIntentSenderForResult(IntentSender intentSender, int i, @Nullable Intent intent, int i2, int i3, int i4, Bundle bundle) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4, bundle);
    }

    static final class NonConfigurationInstances {
        Object custom;
        FragmentManagerNonConfig fragments;
        SimpleArrayMap<String, LoaderManager> loaders;

        NonConfigurationInstances() {
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mFragments.noteStateNotSaved();
        int requestIndex = requestCode >> 16;
        if (requestIndex != 0) {
            int requestIndex2 = requestIndex - 1;
            String who = this.mPendingFragmentActivityResults.get(requestIndex2);
            this.mPendingFragmentActivityResults.remove(requestIndex2);
            if (who == null) {
                Log.w(TAG, "Activity result delivered for unknown Fragment.");
                return;
            }
            Fragment targetFragment = this.mFragments.findFragmentByWho(who);
            if (targetFragment == null) {
                Log.w(TAG, "Activity result no fragment exists for who: " + who);
            } else {
                targetFragment.onActivityResult(65535 & requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onBackPressed() {
        if (!this.mFragments.getSupportFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    @Deprecated
    public final void setSupportMediaController(MediaControllerCompat mediaController) {
        MediaControllerCompat.setMediaController(this, mediaController);
    }

    @Deprecated
    public final MediaControllerCompat getSupportMediaController() {
        return MediaControllerCompat.getMediaController(this);
    }

    public void supportFinishAfterTransition() {
        ActivityCompat.finishAfterTransition(this);
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        ActivityCompat.setEnterSharedElementCallback(this, callback);
    }

    public void setExitSharedElementCallback(SharedElementCallback listener) {
        ActivityCompat.setExitSharedElementCallback(this, listener);
    }

    public void supportPostponeEnterTransition() {
        ActivityCompat.postponeEnterTransition(this);
    }

    public void supportStartPostponedEnterTransition() {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    @CallSuper
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        this.mFragments.dispatchMultiWindowModeChanged(isInMultiWindowMode);
    }

    @CallSuper
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        this.mFragments.dispatchPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mFragments.dispatchConfigurationChanged(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        FragmentManagerNonConfig fragmentManagerNonConfig = null;
        this.mFragments.attachHost((Fragment) null);
        super.onCreate(savedInstanceState);
        NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nc != null) {
            this.mFragments.restoreLoaderNonConfig(nc.loaders);
        }
        if (savedInstanceState != null) {
            Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
            FragmentController fragmentController = this.mFragments;
            if (nc != null) {
                fragmentManagerNonConfig = nc.fragments;
            }
            fragmentController.restoreAllState(p, fragmentManagerNonConfig);
            if (savedInstanceState.containsKey(NEXT_CANDIDATE_REQUEST_INDEX_TAG)) {
                this.mNextCandidateRequestIndex = savedInstanceState.getInt(NEXT_CANDIDATE_REQUEST_INDEX_TAG);
                int[] requestCodes = savedInstanceState.getIntArray(ALLOCATED_REQUEST_INDICIES_TAG);
                String[] fragmentWhos = savedInstanceState.getStringArray(REQUEST_FRAGMENT_WHO_TAG);
                if (requestCodes == null || fragmentWhos == null || requestCodes.length != fragmentWhos.length) {
                    Log.w(TAG, "Invalid requestCode mapping in savedInstanceState.");
                } else {
                    this.mPendingFragmentActivityResults = new SparseArrayCompat<>(requestCodes.length);
                    for (int i = 0; i < requestCodes.length; i++) {
                        this.mPendingFragmentActivityResults.put(requestCodes[i], fragmentWhos[i]);
                    }
                }
            }
        }
        if (this.mPendingFragmentActivityResults == null) {
            this.mPendingFragmentActivityResults = new SparseArrayCompat<>();
            this.mNextCandidateRequestIndex = 0;
        }
        this.mFragments.dispatchCreate();
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId != 0) {
            return super.onCreatePanelMenu(featureId, menu);
        }
        boolean show = super.onCreatePanelMenu(featureId, menu) | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
        if (Build.VERSION.SDK_INT >= 11) {
            return show;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final View dispatchFragmentsOnCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return this.mFragments.onCreateView(parent, name, context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        doReallyStop(false);
        this.mFragments.dispatchDestroy();
        this.mFragments.doLoaderDestroy();
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (super.onMenuItemSelected(featureId, item)) {
            return true;
        }
        switch (featureId) {
            case 0:
                return this.mFragments.dispatchOptionsItemSelected(item);
            case 6:
                return this.mFragments.dispatchContextItemSelected(item);
            default:
                return false;
        }
    }

    public void onPanelClosed(int featureId, Menu menu) {
        switch (featureId) {
            case 0:
                this.mFragments.dispatchOptionsMenuClosed(menu);
                break;
        }
        super.onPanelClosed(featureId, menu);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mResumed = false;
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
            onResumeFragments();
        }
        this.mFragments.dispatchPause();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mFragments.noteStateNotSaved();
    }

    public void onStateNotSaved() {
        this.mFragments.noteStateNotSaved();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mHandler.sendEmptyMessage(2);
        this.mResumed = true;
        this.mFragments.execPendingActions();
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        super.onPostResume();
        this.mHandler.removeMessages(2);
        onResumeFragments();
        this.mFragments.execPendingActions();
    }

    /* access modifiers changed from: protected */
    public void onResumeFragments() {
        this.mFragments.dispatchResume();
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0 || menu == null) {
            return super.onPreparePanel(featureId, view, menu);
        }
        if (this.mOptionsMenuInvalidated) {
            this.mOptionsMenuInvalidated = false;
            menu.clear();
            onCreatePanelMenu(featureId, menu);
        }
        return onPrepareOptionsPanel(view, menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
    }

    /* access modifiers changed from: protected */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    public final Object onRetainNonConfigurationInstance() {
        if (this.mStopped) {
            doReallyStop(true);
        }
        Object custom = onRetainCustomNonConfigurationInstance();
        FragmentManagerNonConfig fragments = this.mFragments.retainNestedNonConfig();
        SimpleArrayMap<String, LoaderManager> loaders = this.mFragments.retainLoaderNonConfig();
        if (fragments == null && loaders == null && custom == null) {
            return null;
        }
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.custom = custom;
        nci.fragments = fragments;
        nci.loaders = loaders;
        return nci;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable p = this.mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
        if (this.mPendingFragmentActivityResults.size() > 0) {
            outState.putInt(NEXT_CANDIDATE_REQUEST_INDEX_TAG, this.mNextCandidateRequestIndex);
            int[] requestCodes = new int[this.mPendingFragmentActivityResults.size()];
            String[] fragmentWhos = new String[this.mPendingFragmentActivityResults.size()];
            for (int i = 0; i < this.mPendingFragmentActivityResults.size(); i++) {
                requestCodes[i] = this.mPendingFragmentActivityResults.keyAt(i);
                fragmentWhos[i] = this.mPendingFragmentActivityResults.valueAt(i);
            }
            outState.putIntArray(ALLOCATED_REQUEST_INDICIES_TAG, requestCodes);
            outState.putStringArray(REQUEST_FRAGMENT_WHO_TAG, fragmentWhos);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mStopped = false;
        this.mReallyStopped = false;
        this.mHandler.removeMessages(1);
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.noteStateNotSaved();
        this.mFragments.execPendingActions();
        this.mFragments.doLoaderStart();
        this.mFragments.dispatchStart();
        this.mFragments.reportLoaderStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mStopped = true;
        this.mHandler.sendEmptyMessage(1);
        this.mFragments.dispatchStop();
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    public Object getLastCustomNonConfigurationInstance() {
        NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
        if (nc != null) {
            return nc.custom;
        }
        return null;
    }

    public void supportInvalidateOptionsMenu() {
        if (Build.VERSION.SDK_INT >= 11) {
            ActivityCompatHoneycomb.invalidateOptionsMenu(this);
        } else {
            this.mOptionsMenuInvalidated = true;
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        if (Build.VERSION.SDK_INT >= 11) {
        }
        writer.print(prefix);
        writer.print("Local FragmentActivity ");
        writer.print(Integer.toHexString(System.identityHashCode(this)));
        writer.println(" State:");
        String innerPrefix = prefix + "  ";
        writer.print(innerPrefix);
        writer.print("mCreated=");
        writer.print(this.mCreated);
        writer.print("mResumed=");
        writer.print(this.mResumed);
        writer.print(" mStopped=");
        writer.print(this.mStopped);
        writer.print(" mReallyStopped=");
        writer.println(this.mReallyStopped);
        this.mFragments.dumpLoaders(innerPrefix, fd, writer, args);
        this.mFragments.getSupportFragmentManager().dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
    }

    private static String viewToString(View view) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        String pkgname;
        char c8 = 'F';
        char c9 = '.';
        StringBuilder out = new StringBuilder(128);
        out.append(view.getClass().getName());
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(view)));
        out.append(' ');
        switch (view.getVisibility()) {
            case 0:
                out.append('V');
                break;
            case 4:
                out.append('I');
                break;
            case 8:
                out.append('G');
                break;
            default:
                out.append('.');
                break;
        }
        if (view.isFocusable()) {
            c = 'F';
        } else {
            c = '.';
        }
        out.append(c);
        if (view.isEnabled()) {
            c2 = 'E';
        } else {
            c2 = '.';
        }
        out.append(c2);
        out.append(view.willNotDraw() ? '.' : 'D');
        if (view.isHorizontalScrollBarEnabled()) {
            c3 = 'H';
        } else {
            c3 = '.';
        }
        out.append(c3);
        if (view.isVerticalScrollBarEnabled()) {
            c4 = 'V';
        } else {
            c4 = '.';
        }
        out.append(c4);
        if (view.isClickable()) {
            c5 = 'C';
        } else {
            c5 = '.';
        }
        out.append(c5);
        if (view.isLongClickable()) {
            c6 = 'L';
        } else {
            c6 = '.';
        }
        out.append(c6);
        out.append(' ');
        if (!view.isFocused()) {
            c8 = '.';
        }
        out.append(c8);
        if (view.isSelected()) {
            c7 = 'S';
        } else {
            c7 = '.';
        }
        out.append(c7);
        if (view.isPressed()) {
            c9 = 'P';
        }
        out.append(c9);
        out.append(' ');
        out.append(view.getLeft());
        out.append(',');
        out.append(view.getTop());
        out.append(Soundex.SILENT_MARKER);
        out.append(view.getRight());
        out.append(',');
        out.append(view.getBottom());
        int id = view.getId();
        if (id != -1) {
            out.append(" #");
            out.append(Integer.toHexString(id));
            Resources r = view.getResources();
            if (!(id == 0 || r == null)) {
                switch (-16777216 & id) {
                    case 16777216:
                        pkgname = DispatchConstants.ANDROID;
                        break;
                    case 2130706432:
                        pkgname = "app";
                        break;
                    default:
                        try {
                            pkgname = r.getResourcePackageName(id);
                            break;
                        } catch (Resources.NotFoundException e) {
                            break;
                        }
                }
                String typename = r.getResourceTypeName(id);
                String entryname = r.getResourceEntryName(id);
                out.append(" ");
                out.append(pkgname);
                out.append(SymbolExpUtil.SYMBOL_COLON);
                out.append(typename);
                out.append(WVNativeCallbackUtil.SEPERATER);
                out.append(entryname);
            }
        }
        out.append("}");
        return out.toString();
    }

    private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        ViewGroup grp;
        int N;
        writer.print(prefix);
        if (view == null) {
            writer.println(Constant.NULL);
            return;
        }
        writer.println(viewToString(view));
        if ((view instanceof ViewGroup) && (N = grp.getChildCount()) > 0) {
            String prefix2 = prefix + "  ";
            for (int i = 0; i < N; i++) {
                dumpViewHierarchy(prefix2, writer, (grp = (ViewGroup) view).getChildAt(i));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void doReallyStop(boolean retaining) {
        if (!this.mReallyStopped) {
            this.mReallyStopped = true;
            this.mRetaining = retaining;
            this.mHandler.removeMessages(1);
            onReallyStop();
        } else if (retaining) {
            this.mFragments.doLoaderStart();
            this.mFragments.doLoaderStop(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void onReallyStop() {
        this.mFragments.doLoaderStop(this.mRetaining);
        this.mFragments.dispatchReallyStop();
    }

    public void onAttachFragment(Fragment fragment) {
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments.getSupportFragmentManager();
    }

    public LoaderManager getSupportLoaderManager() {
        return this.mFragments.getSupportLoaderManager();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (!this.mStartedActivityFromFragment && requestCode != -1) {
            checkForValidRequestCode(requestCode);
        }
        super.startActivityForResult(intent, requestCode);
    }

    public final void validateRequestPermissionsRequestCode(int requestCode) {
        if (!this.mRequestedPermissionsFromFragment && requestCode != -1) {
            checkForValidRequestCode(requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int index = (requestCode >> 16) & 65535;
        if (index != 0) {
            int index2 = index - 1;
            String who = this.mPendingFragmentActivityResults.get(index2);
            this.mPendingFragmentActivityResults.remove(index2);
            if (who == null) {
                Log.w(TAG, "Activity result delivered for unknown Fragment.");
                return;
            }
            Fragment frag = this.mFragments.findFragmentByWho(who);
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for who: " + who);
            } else {
                frag.onRequestPermissionsResult(requestCode & 65535, permissions, grantResults);
            }
        }
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, (Bundle) null);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        this.mStartedActivityFromFragment = true;
        if (requestCode == -1) {
            try {
                ActivityCompat.startActivityForResult(this, intent, -1, options);
            } finally {
                this.mStartedActivityFromFragment = false;
            }
        } else {
            checkForValidRequestCode(requestCode);
            ActivityCompat.startActivityForResult(this, intent, ((allocateRequestIndex(fragment) + 1) << 16) + (65535 & requestCode), options);
            this.mStartedActivityFromFragment = false;
        }
    }

    public void startIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        this.mStartedIntentSenderFromFragment = true;
        if (requestCode == -1) {
            try {
                ActivityCompat.startIntentSenderForResult(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
            } finally {
                this.mStartedIntentSenderFromFragment = false;
            }
        } else {
            checkForValidRequestCode(requestCode);
            ActivityCompat.startIntentSenderForResult(this, intent, ((allocateRequestIndex(fragment) + 1) << 16) + (65535 & requestCode), fillInIntent, flagsMask, flagsValues, extraFlags, options);
            this.mStartedIntentSenderFromFragment = false;
        }
    }

    private int allocateRequestIndex(Fragment fragment) {
        if (this.mPendingFragmentActivityResults.size() >= MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS) {
            throw new IllegalStateException("Too many pending Fragment activity results.");
        }
        while (this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0) {
            this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS;
        }
        int requestIndex = this.mNextCandidateRequestIndex;
        this.mPendingFragmentActivityResults.put(requestIndex, fragment.mWho);
        this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS;
        return requestIndex;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void requestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
        if (requestCode == -1) {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
            return;
        }
        checkForValidRequestCode(requestCode);
        try {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, permissions, ((allocateRequestIndex(fragment) + 1) << 16) + (65535 & requestCode));
            this.mRequestedPermissionsFromFragment = false;
        } catch (Throwable th) {
            this.mRequestedPermissionsFromFragment = false;
            throw th;
        }
    }

    class HostCallbacks extends FragmentHostCallback<FragmentActivity> {
        public HostCallbacks() {
            super(FragmentActivity.this);
        }

        @SuppressLint({"NewApi"})
        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            FragmentActivity.this.dump(prefix, fd, writer, args);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return !FragmentActivity.this.isFinishing();
        }

        public LayoutInflater onGetLayoutInflater() {
            return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
        }

        public FragmentActivity onGetHost() {
            return FragmentActivity.this;
        }

        public void onSupportInvalidateOptionsMenu() {
            FragmentActivity.this.supportInvalidateOptionsMenu();
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
            FragmentActivity.this.startActivityFromFragment(fragment, intent, requestCode);
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
            FragmentActivity.this.startActivityFromFragment(fragment, intent, requestCode, options);
        }

        public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
            FragmentActivity.this.startIntentSenderFromFragment(fragment, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        }

        public void onRequestPermissionsFromFragment(@NonNull Fragment fragment, @NonNull String[] permissions, int requestCode) {
            FragmentActivity.this.requestPermissionsFromFragment(fragment, permissions, requestCode);
        }

        public boolean onShouldShowRequestPermissionRationale(@NonNull String permission) {
            return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, permission);
        }

        public boolean onHasWindowAnimations() {
            return FragmentActivity.this.getWindow() != null;
        }

        public int onGetWindowAnimations() {
            Window w = FragmentActivity.this.getWindow();
            if (w == null) {
                return 0;
            }
            return w.getAttributes().windowAnimations;
        }

        public void onAttachFragment(Fragment fragment) {
            FragmentActivity.this.onAttachFragment(fragment);
        }

        @Nullable
        public View onFindViewById(int id) {
            return FragmentActivity.this.findViewById(id);
        }

        public boolean onHasView() {
            Window w = FragmentActivity.this.getWindow();
            return (w == null || w.peekDecorView() == null) ? false : true;
        }
    }
}
