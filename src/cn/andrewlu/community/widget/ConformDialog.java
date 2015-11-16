package cn.andrewlu.community.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.andrewlu.community.R;


/**
 * Created by andrewlu on 2015/8/21.
 * 需要点击确定才能消失的控件.
 */
public class ConformDialog extends Dialog {

    public ConformDialog(Context context) {
        super(context);
    }

    public ConformDialog(Context context, int theme) {
        super(context, theme);
    }


    public static ConformDialog createDialog(Context context, boolean cancelable) {

        final ConformDialog
                customProgressDialog = new ConformDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.dialog_conform_layout);
        customProgressDialog.setCancelable(cancelable);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //customProgressDialog.dismiss();
                if (customProgressDialog.onOkListener != null) {
                    customProgressDialog.onOkListener.onClick(customProgressDialog, v);
                }
            }
        });
        customProgressDialog.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //customProgressDialog.dismiss();
                if (customProgressDialog.onCancelListener != null) {
                    customProgressDialog.onCancelListener.onClick(customProgressDialog, view);
                }
            }
        });
        return customProgressDialog;
    }

    /**
     * [Summary]
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public ConformDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return this;
    }

    public ConformDialog setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        return this;
    }

    public ConformDialog setCancelListener(OnClickListener l) {
        findViewById(R.id.cancelBtn).setVisibility(View.VISIBLE);
        //findViewById(R.id.cancelBtn).setOnClickListener(l);
        this.onCancelListener = l;
        return this;
    }

    public ConformDialog setOkListener(OnClickListener l) {
        //findViewById(R.id.okBtn).setOnClickListener(l);
        this.onOkListener = l;
        return this;
    }

    private OnClickListener onOkListener;
    private OnClickListener onCancelListener;

    public interface OnClickListener {
        void onClick(DialogInterface dialog, View view);
    }
}
