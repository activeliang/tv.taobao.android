package android.taobao.atlas.remote.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.remote.IRemote;
import android.taobao.atlas.remote.IRemoteContext;
import android.taobao.atlas.remote.IRemoteTransactor;
import android.taobao.atlas.remote.RemoteActivityManager;
import android.taobao.atlas.remote.Util;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class RemoteFragment extends Fragment implements IRemoteContext, IRemoteTransactor {
    private IRemote hostTransactor;
    private Field mCalled;
    private Activity remoteActivity;
    private String targetBundleName;
    private Fragment targetFragment;

    public static RemoteFragment createRemoteFragment(Activity activity, String key, String bundleName) throws Exception {
        RemoteFragment remoteFragment = new RemoteFragment();
        remoteFragment.targetBundleName = bundleName;
        remoteFragment.remoteActivity = RemoteActivityManager.obtain(activity).getRemoteHost(remoteFragment);
        remoteFragment.targetFragment = (Fragment) remoteFragment.remoteActivity.getClassLoader().loadClass(AtlasBundleInfoManager.instance().getBundleInfo(bundleName).remoteFragments.get(key)).newInstance();
        Util.findFieldFromInterface(remoteFragment.targetFragment, "remoteContext").set(remoteFragment.targetFragment, remoteFragment);
        Util.findFieldFromInterface(remoteFragment.targetFragment, "realHost").set(remoteFragment.targetFragment, remoteFragment.remoteActivity);
        if (remoteFragment.targetFragment instanceof IRemote) {
            return remoteFragment;
        }
        throw new RuntimeException("Fragment for remote use must implements IRemote");
    }

    public String getTargetBundle() {
        return this.targetBundleName;
    }

    public IRemote getRemoteTarget() {
        return (IRemote) this.targetFragment;
    }

    public IRemote getHostTransactor() {
        return this.hostTransactor;
    }

    public void registerHostTransactor(IRemote transactor) {
        this.hostTransactor = transactor;
    }

    public Bundle call(String commandName, Bundle args, IRemoteTransactor.IResponse callback) {
        return ((IRemote) this.targetFragment).call(commandName, args, callback);
    }

    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        return ((IRemote) this.targetFragment).getRemoteInterface(interfaceClass, args);
    }

    private FragmentHostCallback getFragmentHostCallback(FragmentHostCallback callback) {
        try {
            Class HostCallbacksClz = Class.forName("android.support.v4.app.FragmentActivity$HostCallbacks");
            Constructor constructor = HostCallbacksClz.getDeclaredConstructor(new Class[]{FragmentActivity.class});
            constructor.setAccessible(true);
            Object hostCallbacks = constructor.newInstance(new Object[]{this.remoteActivity});
            for (Field field : HostCallbacksClz.getSuperclass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("mActivity")) {
                    field.set(hostCallbacks, this.remoteActivity);
                } else if (field.getName().equals("mContext")) {
                    field.set(hostCallbacks, this.remoteActivity.getBaseContext());
                } else {
                    field.set(hostCallbacks, field.get(callback));
                }
            }
            return (FragmentHostCallback) hostCallbacks;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return this.targetFragment.toString();
    }

    public void setArguments(Bundle args) {
        this.targetFragment.setArguments(args);
    }

    public void setInitialSavedState(Fragment.SavedState state) {
        this.targetFragment.setInitialSavedState(state);
    }

    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
        this.targetFragment.setTargetFragment(fragment, requestCode);
    }

    public Context getContext() {
        return this.remoteActivity.getBaseContext();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mCalled = getClass().getSuperclass().getDeclaredField("mCalled");
            this.mCalled.setAccessible(true);
            Field mHost = AndroidHack.findField(this.targetFragment, "mHost");
            Field mOriginalHost = getClass().getSuperclass().getDeclaredField("mHost");
            mOriginalHost.setAccessible(true);
            mHost.set(this.targetFragment, getFragmentHostCallback((FragmentHostCallback) mOriginalHost.get(this)));
            mHost.set(this, getFragmentHostCallback((FragmentHostCallback) mOriginalHost.get(this)));
            AndroidHack.findField(this.targetFragment, "mFragmentManager").set(this.targetFragment, getFragmentManager());
            Field mCalled2 = AndroidHack.findField(this.targetFragment, "mCalled");
            mCalled2.set(this.targetFragment, (Boolean) null);
            this.targetFragment.onAttach(this.remoteActivity);
            AndroidHack.findField(this.targetFragment, "mChildFragmentManager").set(this.targetFragment, getChildFragmentManager());
            if (!((Boolean) mCalled2.get(this.targetFragment)).booleanValue()) {
                throw new RuntimeException("Fragment " + this.targetFragment + " did not call through to super.onAttach()");
            }
            Field mIndexF = getClass().getSuperclass().getDeclaredField("mIndex");
            mIndexF.setAccessible(true);
            Field mWhoF = getClass().getSuperclass().getDeclaredField("mWho");
            mWhoF.setAccessible(true);
            int index = ((Integer) mIndexF.get(this)).intValue();
            Field targetIndexF = AndroidHack.findField(this.targetFragment, "mIndex");
            Field targetWhoF = AndroidHack.findField(this.targetFragment, "mWho");
            targetIndexF.set(this.targetFragment, Integer.valueOf(index));
            targetWhoF.set(this.targetFragment, (String) mWhoF.get(this));
            AndroidHack.findField(this.targetFragment, "mTag").set(this.targetFragment, getTag());
            AndroidHack.findMethod(this.targetFragment, "performCreate", Bundle.class).invoke(this.targetFragment, new Object[]{null});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = this.targetFragment.onCreateView(LayoutInflater.from(this.remoteActivity), container, savedInstanceState);
            if (view != null) {
                Field mInnerView = AndroidHack.findField(this.targetFragment, "mInnerView");
                Field mView = AndroidHack.findField(this.targetFragment, "mView");
                mInnerView.set(this.targetFragment, view);
                mView.set(this.targetFragment, view);
                AndroidHack.findField(this, "mHidden").set(this, Boolean.valueOf(this.targetFragment.isHidden()));
            }
            return view;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.targetFragment.onViewCreated(view, savedInstanceState);
    }

    public void onStart() {
        try {
            this.mCalled.set(this, true);
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 4);
            this.targetFragment.onStart();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onResume() {
        super.onResume();
        try {
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 5);
            this.targetFragment.onResume();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        this.targetFragment.onSaveInstanceState(outState);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.targetFragment.onConfigurationChanged(newConfig);
    }

    public void onPause() {
        super.onPause();
        try {
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 4);
            this.targetFragment.onPause();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onStop() {
        super.onStop();
        try {
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 3);
            this.targetFragment.onStop();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.targetFragment.onLowMemory();
    }

    public void onDestroyView() {
        super.onDestroyView();
        try {
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 1);
            this.targetFragment.onDestroyView();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void onDestroy() {
        try {
            this.mCalled.set(this, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            AndroidHack.findField(this.targetFragment, "mState").set(this.targetFragment, 0);
            this.targetFragment.onDestroy();
        } catch (Throwable e2) {
            throw new RuntimeException(e2);
        }
    }

    public void onDetach() {
        super.onDetach();
        this.targetFragment.onDetach();
    }

    public void onHiddenChanged(boolean hidden) {
        this.targetFragment.onHiddenChanged(hidden);
    }

    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        this.targetFragment.setRetainInstance(retain);
    }

    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
        this.targetFragment.setHasOptionsMenu(hasMenu);
    }

    public void setMenuVisibility(boolean menuVisible) {
        this.targetFragment.setMenuVisibility(menuVisible);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.targetFragment.setUserVisibleHint(isVisibleToUser);
    }

    public boolean getUserVisibleHint() {
        return this.targetFragment.getUserVisibleHint();
    }

    public LoaderManager getLoaderManager() {
        return this.targetFragment.getLoaderManager();
    }

    public void startActivity(Intent intent) {
        this.targetFragment.startActivity(intent);
    }

    public void startActivity(Intent intent, @Nullable Bundle options) {
        this.targetFragment.startActivity(intent, options);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.targetFragment.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        this.targetFragment.startActivityForResult(intent, requestCode, options);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        this.targetFragment.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.targetFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.targetFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return this.targetFragment.shouldShowRequestPermissionRationale(permission);
    }

    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflater.from(this.remoteActivity);
    }

    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        throw new RuntimeException("remote fragment can not be inflated from xml");
    }

    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        this.targetFragment.onAttachFragment(childFragment);
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return this.targetFragment.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    public View getView() {
        return super.getView();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.targetFragment.onActivityCreated((Bundle) null);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        try {
            this.mCalled.set(this, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.targetFragment.onViewStateRestored(savedInstanceState);
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        this.targetFragment.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        this.targetFragment.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.targetFragment.onCreateOptionsMenu(menu, inflater);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        this.targetFragment.onPrepareOptionsMenu(menu);
    }

    public void onDestroyOptionsMenu() {
        this.targetFragment.onDestroyOptionsMenu();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return this.targetFragment.onOptionsItemSelected(item);
    }

    public void onOptionsMenuClosed(Menu menu) {
        this.targetFragment.onOptionsMenuClosed(menu);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        this.targetFragment.onCreateContextMenu(menu, v, menuInfo);
    }

    public void registerForContextMenu(View view) {
        this.targetFragment.registerForContextMenu(view);
    }

    public void unregisterForContextMenu(View view) {
        this.targetFragment.unregisterForContextMenu(view);
    }

    public boolean onContextItemSelected(MenuItem item) {
        return this.targetFragment.onContextItemSelected(item);
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        super.setEnterSharedElementCallback(callback);
        this.targetFragment.setEnterSharedElementCallback(callback);
    }

    public void setExitSharedElementCallback(SharedElementCallback callback) {
        super.setExitSharedElementCallback(callback);
        this.targetFragment.setExitSharedElementCallback(callback);
    }

    public void setEnterTransition(Object transition) {
        this.targetFragment.setEnterTransition(transition);
    }

    public Object getEnterTransition() {
        return this.targetFragment.getEnterTransition();
    }

    public void setReturnTransition(Object transition) {
        this.targetFragment.setReturnTransition(transition);
    }

    public Object getReturnTransition() {
        return this.targetFragment.getReturnTransition();
    }

    public void setExitTransition(Object transition) {
        this.targetFragment.setExitTransition(transition);
    }

    public Object getExitTransition() {
        return this.targetFragment.getExitTransition();
    }

    public void setReenterTransition(Object transition) {
        this.targetFragment.setReenterTransition(transition);
    }

    public Object getReenterTransition() {
        return this.targetFragment.getReenterTransition();
    }

    public void setSharedElementEnterTransition(Object transition) {
        this.targetFragment.setSharedElementEnterTransition(transition);
    }

    public Object getSharedElementEnterTransition() {
        return this.targetFragment.getSharedElementEnterTransition();
    }

    public void setSharedElementReturnTransition(Object transition) {
        this.targetFragment.setSharedElementReturnTransition(transition);
    }

    public Object getSharedElementReturnTransition() {
        return this.targetFragment.getSharedElementReturnTransition();
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
        this.targetFragment.setAllowEnterTransitionOverlap(allow);
    }

    public boolean getAllowEnterTransitionOverlap() {
        return this.targetFragment.getAllowEnterTransitionOverlap();
    }

    public void setAllowReturnTransitionOverlap(boolean allow) {
        this.targetFragment.setAllowReturnTransitionOverlap(allow);
    }

    public boolean getAllowReturnTransitionOverlap() {
        return this.targetFragment.getAllowReturnTransitionOverlap();
    }

    public void postponeEnterTransition() {
        this.targetFragment.postponeEnterTransition();
    }

    public void startPostponedEnterTransition() {
        this.targetFragment.startPostponedEnterTransition();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }
}
