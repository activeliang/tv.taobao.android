package com.yunos.tvtaobao.payment.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import com.ali.auth.third.offline.NQRView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CustomNQRView extends NQRView {
    private OnShowIVPage onShowIVPage;

    public interface OnShowIVPage {
        void showIVPage(Map map, String str, String str2);
    }

    public CustomNQRView(Context context) {
        super(context);
    }

    public CustomNQRView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showIVPage(Map params, String scene, String loginToken) {
        if (this.onShowIVPage != null) {
            this.onShowIVPage.showIVPage(params, scene, loginToken);
        }
    }

    public void cancel() {
        try {
            Field field = NQRView.class.getDeclaredField("mHandler");
            field.setAccessible(true);
            ((Handler) field.get(this)).removeCallbacksAndMessages((Object) null);
            Method method = NQRView.class.getDeclaredMethod("stop", new Class[0]);
            method.setAccessible(true);
            method.invoke(this, new Object[0]);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    public OnShowIVPage getOnShowIVPage() {
        return this.onShowIVPage;
    }

    public void setOnShowIVPage(OnShowIVPage onShowIVPage2) {
        this.onShowIVPage = onShowIVPage2;
    }
}
