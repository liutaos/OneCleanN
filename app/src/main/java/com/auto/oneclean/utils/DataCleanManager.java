package com.auto.oneclean.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.auto.oneclean.MainActivity;
import com.auto.oneclean.interfaces.InfoCallback;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 应用数据清除管理器
 */
public class DataCleanManager {

    private InfoCallback infoCallback; //删除信息回调

    public final String appPackageName = "android.lite.clean";
    public DataCleanManager(InfoCallback callback) {
        this.infoCallback = callback;
    }
    /**
     * 清除其他应用的所有数据
     */
    public boolean cleanOtherAppData(String packageName) {
        File file = new File("/data/data/" + packageName);
        System.out.println("包名为：" + packageName);
        Log.e("  cleanOtherAppData   ", "============== "+packageName );
        if (file.exists()) {
            DataOutputStream os = null;
            Process p = null;

            try {
                if (infoCallback != null) {
                    infoCallback.getInfo("清除内部数据命令 执行开始" + "\n");
                }
                p = Runtime.getRuntime().exec("su");// 获得root权限
                os = new DataOutputStream(p.getOutputStream());

                os.writeBytes("pm clear " + appPackageName + " \n"); //拿到对应app包的读写执行权限
                os.flush();
                os.writeBytes("exit\n"); //退出命令
                os.flush();
                Log.d("cleanOtherAppData", "===========cleanOtherAppData  +++++++++    =======");

                os.close();
                p.destroy();

                return true;

            } catch (IOException e) {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (p != null) {
                    p.destroy();
                }
            }
        }

        return false;
    }

}