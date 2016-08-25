package com.rahuljanagouda.popularmoviestwo.customViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rahuljanagouda.popularmoviestwo.R;


public class MaterialProgressDialog extends Dialog implements OnDismissListener {

    //    static ProgressHUD dialog;
    private final Context ctx;

    public MaterialProgressDialog(@NonNull Context context) {
        super(context);
        ctx = context;
    }

    private MaterialProgressDialog(@NonNull Context context, int theme) {
        super(context, theme);
        ctx = context;
    }

    @NonNull
    @SuppressLint("NewApi")
    public static MaterialProgressDialog show(@NonNull final Context context, @Nullable CharSequence message, boolean indeterminate, boolean cancelable) {
        MaterialProgressDialog dialog = new MaterialProgressDialog(context, R.style.ProgressHUD);
        try {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            dialog.setTitle("");
            dialog.setContentView(R.layout.material_progress_dialog);
            if (message == null || message.length() == 0) {
                dialog.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialog.findViewById(R.id.message);
                txt.setText(message);
            }

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub

                }
            });

        /*
         * dialog.setOnDismissListener(new OnDismissListener() {
         *
         * @Override public void onDismiss(DialogInterface dialog) {  BaseActivity a = (BaseActivity) context; a.abortAllSearchRequests(); } });
         */

            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.6f;

            dialog.getWindow().setAttributes(lp);
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }

        } catch (Exception e) {
//            CleartripUtils.handleException(e);
        }
        return dialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void setMessage(@Nullable CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public boolean isShowing() {
        return super.isShowing();
    }

    /*
     * @Override public void onCancel(DialogInterface dialog) { // TODO Auto-generated method stub Context ctx = getContext(); BaseActivity a = (BaseActivity)ctx; a.abortAllSearchRequests(); }
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
