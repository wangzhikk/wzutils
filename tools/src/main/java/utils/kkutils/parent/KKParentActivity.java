package utils.kkutils.parent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.Serializable;

import utils.kkutils.AppTool;
import utils.kkutils.R;
import utils.kkutils.common.LogTool;

/**
 * Created by kk on 2017/3/23.
 */

public class KKParentActivity extends AppCompatActivity implements Serializable {
    ProgressDialog progressDialog;

    /***
     * 透明状态栏
     */
    public void setTouMingStatusBar(){
        setStatusBarColor(Color.TRANSPARENT);
    }
    /***
     * 透明状态栏 可以自己设置颜色
     */
    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    /***
     * 半透明状态栏
     */
    public void setBanTouMingStatusBar(boolean bantouming){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if(bantouming)window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
    /***
     * 显示一个弹出框
     *
     * @param msg
     */
    public static void showWaitingDialogStac(String msg) {
        try {
            if (AppTool.currActivity instanceof KKParentActivity) {
                ((KKParentActivity) AppTool.currActivity).showWaitingDialog("");
            }
        } catch (Exception e) {
            LogTool.ex(e);
        }
    }

    /***
     * 隐藏当前弹出框
     */
    public static void hideWaitingDialogStac() {
        try {
            if (AppTool.currActivity instanceof KKParentActivity) {
                ((KKParentActivity) AppTool.currActivity).hideWaitingDialog();
            }
        } catch (Exception e) {
            LogTool.ex(e);
        }
    }

    public void initData() {

    }

    /***
     * 显示一个弹出框
     *
     * @param msg
     */
    public void showWaitingDialog(String msg) {
        try {
            if (TextUtils.isEmpty(msg)) {
                msg = getString(R.string.dengdai);
            }
            final String msgFnal = msg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Activity activity= KKParentActivity.this;
                        if(activity.isFinishing())return;
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.setMessage(msgFnal);
                        } else {
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage(msgFnal);
                        }
                        progressDialog.show();
                    } catch (Exception e) {
                        LogTool.ex(e);
                    }
                }
            });
        } catch (Exception e) {
            LogTool.ex(e);
        }
    }

    /***
     * 隐藏当前弹出框
     */
    public void hideWaitingDialog() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        LogTool.ex(e);
                    }
                }
            });

        } catch (Exception e) {
            LogTool.ex(e);
        }
    }


}
