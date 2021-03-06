package utils.kkutils.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

import java.io.File;

import utils.kkutils.AppTool;
import utils.kkutils.R;
import utils.kkutils.common.FileTool;
import utils.kkutils.common.LogTool;
import utils.kkutils.common.StringTool;

/**
 * Created by kk on 2016/5/18.
 * 用于更新的
 */
public class Version {
    public int versionCode;//版本号
    public String versionName = "";//版本名称
    public String versionSize = "";//版本大小
    public String updateUrl = "";//下载地址
    public String updateDesc = "";//版本描述
    public String updateTime = "";//更新时间
    public String isForce = "";//是否强制更新 0-否 1-是


    /***
     * 检测更新
     *
     * @param newVersion
     */
    public static boolean checkUpDate(final Context context, final Version newVersion) {
        int currVersionCode = getCurrVersionCode(context);
        LogTool.s("检查更新： 当前版本" + currVersionCode + "  服务端版本" + newVersion.versionCode);
        boolean hasNew = currVersionCode < newVersion.versionCode;
        newVersion.updateDesc = "" + newVersion.updateDesc.replace("\\n", "\n");
        if (hasNew) {//有新的版本
            String showText = context.getString(R.string.banben_mingcheng)
                    + newVersion.versionName
                    + "\n"+context.getString(R.string.gengxin_shijian)
                    + newVersion.updateTime
                    + "\n"+context.getString(R.string.gengxin_daxiao)
                    + newVersion.versionSize
                    + "\n"+context.getString(R.string.gengxin_neirong)
                    + newVersion.updateDesc;


            final AlertDialog dialogShowMsg = new AlertDialog.Builder(context)
                    .setTitle(R.string.ninyou_xinde_banben)
                    .setMessage(showText)
                    .create();

            final boolean isForce = "1".equals(newVersion.isForce);
            if (isForce) {//强制更新
                dialogShowMsg.setCancelable(false);
                dialogShowMsg.setButton(ProgressDialog.BUTTON_NEGATIVE, context.getString(R.string.tuichu_chengxu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppTool.exitApp();
                    }
                });
            } else {
                dialogShowMsg.setCancelable(true);
                dialogShowMsg.setButton(ProgressDialog.BUTTON_NEGATIVE, context.getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogShowMsg.dismiss();
                    }
                });
            }
            dialogShowMsg.setButton(ProgressDialog.BUTTON_POSITIVE, context.getString(R.string.liji_gengxin), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDownLoadProgressDialog(context, newVersion, isForce);
                }
            });

            dialogShowMsg.show();
        }
        return hasNew;
    }

    /**
     * 显示下载进度框
     *
     * @param context
     * @param newVersion
     */
    public static void showDownLoadProgressDialog(final Context context, Version newVersion, final boolean isForce) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(context.getString(R.string.zhengzai_nuli_xiazai));
        String path= FileTool.getCacheDir("apk")+"tem.apk";

        final long id = KKDownLoadTool.downLoad( newVersion.updateUrl,path, new KKDownLoadTool.DownLoadProgressListener() {
            @Override
            public void onProgress(long id, String url, final String localPath, long allSize, long currDownLoadSize, boolean isComplete) {
                if (isComplete && allSize == currDownLoadSize&&localPath!=null) {//完成过后就是安装
                    LogTool.s("installApk pre " + localPath + "  " + new File(localPath).length());
                    if(StringTool.isEmpty(localPath))return;
                    AppTool.uiHandler.postDelayed(new Runnable() {//延迟一秒钟，  担心可能没下载完毕
                        @Override
                        public void run() {
                            installApk(context, localPath);
                            if (!isForce) progressDialog.dismiss();
                        }
                    }, 1000);
                } else {//下载进度
                    progressDialog.setProgress((int) ((currDownLoadSize * 100) / allSize));
                }
            }
        });
        if (!isForce) {
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, context.getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    KKDownLoadTool.removeDownLoad(id);
                    progressDialog.dismiss();
                }
            });
            progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, context.getString(R.string.houtai_xiazai), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                }
            });
        }
        progressDialog.show();
    }


    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static int getCurrVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装Apk
     *
     * @param context
     * @param path
     */
    private static void installApk(final Context context, final String path) {


        LogTool.s("installApk ing " + path + "  " + new File(path).length());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        context.startActivity(intent);


    }
}
