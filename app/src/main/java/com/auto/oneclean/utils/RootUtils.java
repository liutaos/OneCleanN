/*
 * *******************************************************************************
 *         文 件：RootUtils.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月19日 13:47:36
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean.utils;

import java.io.DataOutputStream;

/**
 * root工具类
 */
public class RootUtils {

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * pkgCodePath参数
     *
     * @param pkgCodePath 外存下的包名中的文件路径
     *                    可通过 {@link android.content.ContextWrapper}中的getPackageCodePath()方法 获取
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd="pm clear " + pkgCodePath;
            process = Runtime.getRuntime().exec("su "); //切换到root，只在刚安装时，会弹出对话框确认一次
            os = new DataOutputStream(process.getOutputStream());

            os.writeBytes(cmd + " \n"); //pkgCodePath目录拿到读写执行权限
            os.flush();

            os.writeBytes("exit \n"); //退出命令
            os.flush();

        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
