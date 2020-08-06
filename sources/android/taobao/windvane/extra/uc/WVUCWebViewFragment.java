package android.taobao.windvane.extra.uc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.taobao.windvane.util.TaoLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WVUCWebViewFragment extends Fragment {
    private static String TAG = WVUCWebViewFragment.class.getSimpleName();
    public static String URL = "url";
    private Activity activity;
    private WVUCWebChromeClient mChromeClient = null;
    private WVUCWebView mWebView = null;
    private WVUCWebViewClient mWebclient = null;
    private String url = null;

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        this.activity = activity2;
    }

    @Deprecated
    public WVUCWebViewFragment() {
    }

    public WVUCWebViewFragment(Activity activity2) {
        this.activity = activity2;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.url = bundle.getString(URL);
        }
    }

    public void onDestroy() {
        if (this.mWebView != null) {
            this.mWebView.setVisibility(8);
            this.mWebView.removeAllViews();
            if (this.mWebView.getParent() != null) {
                ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
            }
            this.mWebView.destroy();
            this.mWebView = null;
        }
        this.activity = null;
        try {
            super.onDestroy();
        } catch (Exception e) {
            TaoLog.e(TAG, e.getMessage());
        }
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause();
        }
        super.onPause();
    }

    public void onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume();
        }
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getWebView();
        if (this.url == null || this.mWebView == null) {
            TaoLog.d(TAG, "image urls is null");
        } else {
            this.mWebView.loadUrl(this.url);
        }
        return this.mWebView;
    }

    public void setWebViewClient(WVUCWebViewClient webclient) {
        if (webclient != null) {
            this.mWebclient = webclient;
            if (this.mWebView != null) {
                this.mWebView.setWebViewClient(this.mWebclient);
            }
        }
    }

    public void setWebchormeClient(WVUCWebChromeClient client) {
        if (client != null) {
            this.mChromeClient = client;
            if (this.mWebView != null) {
                this.mWebView.setWebChromeClient(this.mChromeClient);
            }
        }
    }

    public WVUCWebView getWebView() {
        if (this.mWebView == null) {
            Activity lActivity = this.activity == null ? getActivity() : this.activity;
            if (lActivity == null) {
                return null;
            }
            this.mWebView = new WVUCWebView(lActivity);
            setWebViewClient(this.mWebclient);
            setWebchormeClient(this.mChromeClient);
            this.mWebView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        }
        return this.mWebView;
    }

    public boolean onBackPressed() {
        if (getWebView() == null || !getWebView().canGoBack()) {
            return false;
        }
        getWebView().goBack();
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mWebView != null) {
            this.mWebView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
