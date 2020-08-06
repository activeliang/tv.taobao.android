package com.yunos.tvtaobao.payment.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import com.yunos.tvtaobao.payment.view.AlipayQRDialog;
import com.yunos.tvtaobao.payment.view.CustomConfirmDialog;
import com.zhiping.tvtao.payment.alipay.task.AgreementPayTask;

public class AlipayAuthUtil {

    public interface ExitPayListener {
        void onResumePay();

        void onSureExit();
    }

    public static void doHandleAuth(final Context context, final AgreementPayTask agreementPayTask, String alipayUid, String taobaoOrderNo, double totalPrice, final ExitPayListener exitPayListener) {
        AlipayQRDialog dialog = new AlipayQRDialog.Builder(context).setTaobaoOrderId(taobaoOrderNo).setOrderPrice(totalPrice).setAlipayUserId(alipayUid).create();
        dialog.setDelegate(new AlipayQRDialog.QRDialogDelegate() {
            public void QRDialogSuccess(AlipayQRDialog dialog, boolean success) {
                dialog.dismiss();
                agreementPayTask.resumePay();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() != 4) {
                    return false;
                }
                if (keyEvent.getAction() == 0) {
                    new CustomConfirmDialog.Builder(context).setMessage("支付未完成,是否确认退出?").setPositiveButton("确定退出", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            agreementPayTask.stop();
                            dialog.dismiss();
                            if (exitPayListener != null) {
                                exitPayListener.onSureExit();
                            }
                        }
                    }).setNegativeButton("继续支付", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (exitPayListener != null) {
                                exitPayListener.onResumePay();
                            }
                        }
                    }).create().show();
                }
                return true;
            }
        });
        dialog.show();
    }
}
