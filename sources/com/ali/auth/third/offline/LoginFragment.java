package com.ali.auth.third.offline;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.history.AccountHistoryManager;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.HistoryAccount;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.StringUtil;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginConstants;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.login.task.LoginByIVTokenTask;
import com.ali.auth.third.offline.login.task.LoginByPwdTask;
import com.ali.auth.third.offline.login.util.ActivityUIHelper;
import com.ali.auth.third.offline.model.FilterListListener;
import com.ali.auth.third.offline.model.LoginHistoryAdapter;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.ali.auth.third.offline.webview.BridgeWebChromeClient;
import com.ali.auth.third.offline.widget.AUAccountAutoCompleteTextView;
import com.ali.auth.third.offline.widget.NetworkCheckOnClickListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "login.LoginFragment";
    /* access modifiers changed from: private */
    public Handler handler = new Handler(Looper.getMainLooper());
    protected boolean isDropdownAccount;
    protected AutoCompleteTextView mAccountET;
    protected ActivityUIHelper mActivityHelper;
    protected ImageView mBackImageView;
    protected String mCurrentAccount;
    protected String mCurrentPassword;
    protected ImageView mDropArrow;
    protected List<HistoryAccount> mHistoryAccounts;
    protected View mKeyLeftView;
    protected View mLeftArea;
    protected ImageView mLineIV;
    protected LoginActivity mLoginActivity;
    protected LoginBridge mLoginBridge = new LoginBridge();
    protected Button mLoginBtn;
    protected LoginHistoryAdapter mLoginHistoryAdapter;
    protected View mPWDArea;
    protected EditText mPasswordET;
    protected ImageView mPwdPartIV;
    protected TextView mPwdPartTV;
    protected RelativeLayout mScanArea;
    protected View mScanLeftView;
    protected ImageView mScanPartIV;
    protected TextView mScanPartTV;
    /* access modifiers changed from: private */
    public ScrollListener mScrollListener = new ScrollListener() {
        public void scroll() {
            LoginFragment.this.handler.postDelayed(new Runnable() {
                public void run() {
                    LoginFragment.this.setScrollerHight();
                }
            }, 200);
        }
    };
    protected ScrollView mScrollView;
    protected ImageView mShowPasswordIV;
    protected TextWatcher mTextWatcherAccount = null;
    protected TextWatcher mTextWatcherPassword = null;
    protected String mUrl;
    protected AuthWebView mWebView;
    protected RelativeLayout mWebviewContainer;

    public interface ScrollListener {
        void scroll();
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.user_login_fragment;
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        initTextWatcher();
        this.mBackImageView = (ImageView) view.findViewById(R.id.com_taobao_tae_sdk_web_view_title_bar_back_button);
        if (this.mBackImageView != null) {
            this.mBackImageView.setOnClickListener(this);
        }
        this.mScrollView = (ScrollView) view.findViewById(R.id.aliuser_id_sign_frag_member_scrollview);
        View accountView = view.findViewById(R.id.aliuser_id_account_frag_member_signin_et);
        if (accountView == null || !(accountView instanceof AUAccountAutoCompleteTextView)) {
            this.mAccountET = (AutoCompleteTextView) accountView;
        } else {
            this.mAccountET = (AUAccountAutoCompleteTextView) accountView;
        }
        if (this.mAccountET != null) {
            this.mAccountET.setSingleLine();
            this.mAccountET.addTextChangedListener(this.mTextWatcherAccount);
            this.mAccountET.setDropDownHeight(-2);
            this.mAccountET.setThreshold(1);
            this.mAccountET.setTypeface(Typeface.SANS_SERIF);
        }
        this.mDropArrow = (ImageView) view.findViewById(R.id.aliuser_id_account_drop_frag_member_signin_iv);
        this.mPasswordET = (EditText) view.findViewById(R.id.aliuser_id_password_frag_member_signin_et);
        if (this.mPasswordET != null) {
            this.mPasswordET.addTextChangedListener(this.mTextWatcherPassword);
            this.mPasswordET.setTypeface(Typeface.SANS_SERIF);
            this.mPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId != 6 || TextUtils.isEmpty(LoginFragment.this.mAccountET.getText()) || TextUtils.isEmpty(LoginFragment.this.mPasswordET.getText())) {
                        return false;
                    }
                    LoginFragment.this.onLoginAction((String) null);
                    return true;
                }
            });
        }
        this.mLoginBtn = (Button) view.findViewById(R.id.aliuser_id_sign_frag_member_signin_btn);
        this.mShowPasswordIV = (ImageView) view.findViewById(R.id.aliuser_id_show_password_frag_member_signin_btn);
        if (this.mShowPasswordIV != null) {
            this.mShowPasswordIV.setOnClickListener(this);
        }
        this.mLoginActivity = (LoginActivity) getActivity();
        this.mActivityHelper = new ActivityUIHelper(this.mLoginActivity);
        this.mLeftArea = view.findViewById(R.id.aliuser_left_area);
        this.mPWDArea = view.findViewById(R.id.aliuser_pwd_area);
        this.mScanArea = (RelativeLayout) view.findViewById(R.id.aliuser_scan_area);
        this.mKeyLeftView = view.findViewById(R.id.aliuser_id_key_left);
        this.mScanLeftView = view.findViewById(R.id.aliuser_id_scan_left);
        this.mPwdPartIV = (ImageView) view.findViewById(R.id.aliuser_id_key_iv);
        this.mPwdPartTV = (TextView) view.findViewById(R.id.aliuser_id_key_tv);
        this.mScanPartIV = (ImageView) view.findViewById(R.id.aliuser_id_scan_iv);
        this.mScanPartTV = (TextView) view.findViewById(R.id.aliuser_id_scan_tv);
        this.mWebviewContainer = (RelativeLayout) view.findViewById(R.id.aliuser_webview_container);
        this.mLineIV = (ImageView) view.findViewById(R.id.aliuser_id_line_iv);
        setOnClickListener(this.mLoginBtn, this.mPwdPartIV, this.mPwdPartTV, this.mKeyLeftView);
        NetworkCheckOnClickListener listener = new NetworkCheckOnClickListener(this.mActivityHelper) {
            public void afterCheck(View v) {
                LoginFragment.this.goScan();
            }
        };
        if (this.mScanPartIV != null) {
            this.mScanPartIV.setOnClickListener(listener);
        }
        if (this.mScanPartIV != null) {
            this.mScanPartTV.setOnClickListener(listener);
        }
        if (this.mScanLeftView != null) {
            this.mScanLeftView.setOnClickListener(listener);
        }
        if (ConfigManager.getInstance().getScanParams() != null) {
            this.mWebView = createTaeWebView();
            setWebView();
            return;
        }
        if (this.mScanArea != null) {
            this.mScanArea.setVisibility(8);
        }
        if (this.mLeftArea != null) {
            this.mLeftArea.setVisibility(8);
        }
        if (this.mLineIV != null) {
            this.mLineIV.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public AuthWebView createTaeWebView() {
        try {
            return new AuthWebView(getActivity());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void setWebView() {
        if (this.mWebView != null) {
            this.mWebView.setWebChromeClient(new BridgeWebChromeClient());
            this.mWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    try {
                        view.stopLoading();
                    } catch (Exception e) {
                        Log.e(LoginFragment.TAG, e.toString());
                    }
                    try {
                        view.clearView();
                    } catch (Exception e2) {
                        Log.e(LoginFragment.TAG, e2.toString());
                    }
                    view.loadUrl("about:blank");
                    view.loadData("<!DOCTYPE html>\n<html>\n<head>\n    <meta name=\"viewport\"\n          content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no,viewport-fit=cover,shrink-to-fit=no\"/>\n</head>\n<body style=\"width:100%;height:100%;\">\n<h1 style=\"font-size:14px;\">网络异常，请联网后重试</h1>\n</body>\n</html>", "text/html", "UTF-8");
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            this.mWebView.setLayoutParams(layoutParams);
            this.mWebView.setHorizontalScrollBarEnabled(false);
            this.mWebView.setVerticalScrollBarEnabled(false);
            if (this.mWebviewContainer != null) {
                this.mWebviewContainer.addView(this.mWebView);
            }
            StringBuilder qrCodeUrlSB = new StringBuilder(String.format(ConfigManager.getInstance().getQrCodeLoginUrl(), new Object[]{KernelContext.getAppKey()}));
            Map<String, Object> params = ConfigManager.getInstance().getScanParams();
            if (params != null) {
                if (!TextUtils.isEmpty(params.get("domain") == null ? "" : (String) params.get("domain"))) {
                    qrCodeUrlSB.append("_").append(params.get("domain"));
                }
            }
            if (params != null) {
                if (!TextUtils.isEmpty(params.get(LoginConstants.CONFIG) == null ? "" : (String) params.get(LoginConstants.CONFIG))) {
                    String urlParams = urlParamsFormat((String) params.get(LoginConstants.CONFIG));
                    if (TextUtils.isEmpty(urlParams)) {
                        urlParams = "";
                    }
                    qrCodeUrlSB.append(urlParams);
                }
            }
            qrCodeUrlSB.append("&bodyId=mini");
            this.mUrl = qrCodeUrlSB.toString();
            if (!TextUtils.isEmpty(this.mUrl)) {
                this.mWebView.loadUrl(this.mUrl);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setOnClickListener(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setClickable(true);
                view.setOnClickListener(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initTextWatcher() {
        this.mTextWatcherAccount = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (LoginFragment.this.needScroll()) {
                    LoginFragment.this.mScrollListener.scroll();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginFragment.this.checkSignInable();
            }

            public void afterTextChanged(Editable s) {
            }
        };
        this.mTextWatcherPassword = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (LoginFragment.this.needScroll()) {
                    LoginFragment.this.mScrollListener.scroll();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (LoginFragment.this.mPasswordET != null) {
                    LoginFragment.this.checkSignInable();
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    /* access modifiers changed from: private */
    public void checkSignInable() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(getLayoutContent(), (ViewGroup) null);
        initViews(layoutView);
        return layoutView;
    }

    /* access modifiers changed from: protected */
    public boolean needScroll() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void setScrollerHight() {
        int[] pwdInput = new int[2];
        if (this.mPasswordET != null) {
            this.mPasswordET.getLocationOnScreen(pwdInput);
            int scrollerHight = pwdInput[1] + 40;
            if (this.mLoginBtn != null) {
                int[] location = new int[2];
                this.mLoginBtn.getLocationOnScreen(location);
                scrollerHight = location[1];
            }
            if (this.mScrollView != null) {
                this.mScrollView.scrollTo(0, scrollerHight);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readAccountFromHistory() {
        new AsyncTask<Object, Void, List<HistoryAccount>>() {
            /* access modifiers changed from: protected */
            public List<HistoryAccount> doInBackground(Object... params) {
                return AccountHistoryManager.getInstance().getHistoryAccounts();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(List<HistoryAccount> historyAccounts) {
                LoginFragment.this.mHistoryAccounts = historyAccounts;
                if (historyAccounts != null && historyAccounts.size() > 0) {
                    String userInputName = historyAccounts.get(0).nick;
                    if (!TextUtils.isEmpty(userInputName)) {
                        LoginFragment.this.mAccountET.setText(userInputName);
                        LoginFragment.this.isDropdownAccount = true;
                    }
                    LoginFragment.this.mLoginHistoryAdapter = new LoginHistoryAdapter(LoginFragment.this.getActivity(), LoginFragment.this, LoginFragment.this, LoginFragment.this.mHistoryAccounts, new FilterListListener() {
                        public void getFilterCount(int count) {
                            LoginFragment.this.setDropDownHeight(count);
                        }
                    });
                    LoginFragment.this.mAccountET.setAdapter(LoginFragment.this.mLoginHistoryAdapter);
                    LoginFragment.this.setDropDownHeight(historyAccounts.size());
                    if (LoginFragment.this.mDropArrow != null) {
                        LoginFragment.this.mDropArrow.setVisibility(0);
                        LoginFragment.this.mDropArrow.setOnClickListener(LoginFragment.this);
                    }
                } else if (LoginFragment.this.mDropArrow != null) {
                    LoginFragment.this.mDropArrow.setVisibility(8);
                    LoginFragment.this.isDropdownAccount = false;
                }
            }
        }.execute(new Object[0]);
    }

    /* access modifiers changed from: protected */
    public int getDropDownHeight() {
        return 300;
    }

    /* access modifiers changed from: protected */
    public int getDefaultListSize() {
        return 2;
    }

    /* access modifiers changed from: protected */
    public boolean needSetDropDownHeight() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void setDropDownHeight(int size) {
        if (needSetDropDownHeight()) {
            if (size >= getDefaultListSize()) {
                this.mAccountET.setDropDownHeight(getDropDownHeight());
            } else {
                this.mAccountET.setDropDownHeight(-2);
            }
        }
        this.mAccountET.requestLayout();
    }

    /* access modifiers changed from: private */
    public void onLoginAction(String aliusersdk_querystring) {
        this.mCurrentAccount = this.mAccountET.getText().toString().trim();
        this.mCurrentPassword = this.mPasswordET.getText().toString().trim();
        if (StringUtil.isEmpty(this.mCurrentAccount)) {
            showErrorMessage(getString(R.string.aliuser_sign_in_please_enter_username));
        } else if (StringUtil.isEmpty(this.mCurrentPassword)) {
            showErrorMessage(getString(R.string.aliuser_sign_in_please_enter_password));
        } else {
            if (this.mActivityHelper != null) {
                this.mActivityHelper.hideInputMethod();
            }
            new LoginByPwdTask(this.mLoginActivity).execute(new String[]{this.mCurrentAccount, this.mCurrentPassword, aliusersdk_querystring});
        }
    }

    private void showErrorMessage(String msg) {
        this.mActivityHelper.toast(msg, 0);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.aliuser_id_sign_frag_member_signin_btn) {
            onLoginAction((String) null);
        } else if (id == R.id.com_taobao_tae_sdk_web_view_title_bar_back_button) {
            if (this.mLoginActivity != null) {
                this.mLoginActivity.finishCurrentAndNotify();
            }
        } else if (id == R.id.aliuser_id_key_iv || id == R.id.aliuser_id_key_tv || id == R.id.aliuser_id_key_left) {
            this.mScanPartIV.setImageDrawable(getResources().getDrawable(R.drawable.aliusersdk_scan_gray));
            this.mPwdPartIV.setImageDrawable(getResources().getDrawable(R.drawable.aliusersdk_key));
            this.mLineIV.setImageDrawable(getResources().getDrawable(R.drawable.aliuser_line));
            this.mScanArea.setVisibility(8);
            this.mPWDArea.setVisibility(0);
            if (this.mWebView != null) {
                this.mWebView.removeBridgeObject("loginBridge");
            }
        } else if (id == R.id.aliuser_id_scan_iv || id == R.id.aliuser_id_scan_tv || id == R.id.aliuser_id_scan_left) {
            goScan();
        } else if (id == R.id.aliuser_id_account_drop_frag_member_signin_iv) {
            this.mActivityHelper.hideInputMethod();
            try {
                this.mAccountET.showDropDown();
                this.mAccountET.setThreshold(0);
                Filter filter = this.mLoginHistoryAdapter.getFilter();
                if (filter != null) {
                    filter.filter((CharSequence) null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mAccountET.requestLayout();
        } else if (id == R.id.ali_user_drop_name) {
            onDropItemClick(v);
        }
    }

    /* access modifiers changed from: private */
    public void goScan() {
        this.mScanPartIV.setImageDrawable(getResources().getDrawable(R.drawable.aliusersdk_scan));
        this.mPwdPartIV.setImageDrawable(getResources().getDrawable(R.drawable.aliusersdk_key_gray));
        this.mLineIV.setImageDrawable(getResources().getDrawable(R.drawable.aliuser_line_down));
        this.mPWDArea.setVisibility(8);
        this.mWebView.loadUrl(this.mUrl);
        SDKLogger.d(TAG, "url=" + this.mUrl);
        this.mScanArea.setVisibility(0);
        this.mWebView.addBridgeObject("loginBridge", this.mLoginBridge);
    }

    /* access modifiers changed from: protected */
    public int getDropDownBackground() {
        return 0;
    }

    private void onDropItemClick(View view) {
        this.mAccountET.setThreshold(1);
        String selectedName = ((HistoryAccount) view.getTag()).nick;
        this.isDropdownAccount = false;
        if (!TextUtils.isEmpty(selectedName)) {
            int oldValue = this.mAccountET.getThreshold();
            this.mAccountET.setThreshold(Integer.MAX_VALUE);
            this.mAccountET.setText(selectedName);
            this.mAccountET.setSelection(selectedName.length());
            this.mAccountET.setThreshold(oldValue);
        }
        this.isDropdownAccount = true;
        this.mAccountET.requestFocus();
        this.mAccountET.dismissDropDown();
    }

    private String urlParamsFormat(String configJson) {
        if (TextUtils.isEmpty(configJson)) {
            return "";
        }
        StringBuilder retStr = new StringBuilder("");
        try {
            JSONObject object = new JSONObject(configJson);
            Iterator<String> itt = object.keys();
            while (itt.hasNext()) {
                String key = itt.next().toString();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(object.getString(key))) {
                    retStr.append("&");
                    retStr.append(key);
                    retStr.append("=");
                    retStr.append(object.getString(key));
                }
            }
        } catch (JSONException e) {
        }
        return retStr.toString();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mWebView != null) {
            try {
                if (this.mWebviewContainer != null) {
                    this.mWebviewContainer.removeView(this.mWebView);
                }
                this.mWebView.removeBridgeObject("loginBridge");
                this.mWebView.removeAllViews();
                this.mWebView.setVisibility(8);
                this.mWebView.destroy();
            } catch (Throwable th) {
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.OPEN_DOUBLE_CHECK && data != null) {
            if (Constants.ACTION_QUIT.equals(data.getStringExtra("action"))) {
                this.mPasswordET.setText("");
            } else if (resultCode == ResultCode.CAPTCHA_RELOGIN.code) {
                onLoginAction(data.getStringExtra(Constants.QUERY_STRING));
            } else if (resultCode == ResultCode.TRUST_LOGIN.code) {
                String h5_token = data.getStringExtra(Constants.H5_TOKEN);
                String h5_scene = data.getStringExtra(Constants.H5_SCENE);
                if (CallbackContext.loginCallback != null) {
                    this.mCurrentAccount = this.mAccountET.getText().toString().trim();
                    this.mCurrentPassword = this.mPasswordET.getText().toString().trim();
                    new LoginByIVTokenTask(this.mLoginActivity, (LoginCallback) CallbackContext.loginCallback).execute(new String[]{h5_token, h5_scene, null});
                }
            } else if (resultCode == ResultCode.SUCCESS.code) {
                String nativeToken = data.getStringExtra(Constants.NATIVE_TOKEN);
                String nativeScene = data.getStringExtra(Constants.NATIVE_SCENE);
                String h5QueryString = data.getStringExtra(Constants.QUERY_STRING);
                if (CallbackContext.loginCallback != null) {
                    this.mCurrentAccount = this.mAccountET.getText().toString().trim();
                    this.mCurrentPassword = this.mPasswordET.getText().toString().trim();
                    new LoginByIVTokenTask(this.mLoginActivity, (LoginCallback) CallbackContext.loginCallback).execute(new String[]{nativeToken, nativeScene, h5QueryString, this.mCurrentAccount, this.mCurrentPassword});
                }
            }
        }
    }

    public boolean onLongClick(final View view) {
        if (this.mActivityHelper == null) {
            return true;
        }
        this.mActivityHelper.alert(this.mLoginActivity.getResources().getString(R.string.aliuser_auth_account_remove_title), this.mLoginActivity.getResources().getString(R.string.aliuser_auth_account_remove_info), this.mLoginActivity.getResources().getString(R.string.aliuser_auth_account_remove_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                final HistoryAccount account = (HistoryAccount) view.getTag();
                new AsyncTask<Void, Void, Void>() {
                    /* access modifiers changed from: protected */
                    public Void doInBackground(Void... voids) {
                        boolean removed = LoginFragment.this.mHistoryAccounts.remove(account);
                        AccountHistoryManager.getInstance().removeHistoryAccount(account);
                        if (!removed) {
                            return null;
                        }
                        LoginFragment.this.mHistoryAccounts = AccountHistoryManager.getInstance().getHistoryAccounts();
                        return null;
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(Void param) {
                        LoginFragment.this.notifyChange();
                    }
                }.execute(new Void[0]);
            }
        }, this.mLoginActivity.getResources().getString(R.string.aliuser_auth_account_remove_cancel), (DialogInterface.OnClickListener) null);
        return true;
    }

    /* access modifiers changed from: private */
    public void notifyChange() {
        this.mLoginHistoryAdapter.afterDeleteHistory(this.mHistoryAccounts);
        this.mLoginHistoryAdapter.notifyDataSetChanged();
        readAccountFromHistory();
    }
}
