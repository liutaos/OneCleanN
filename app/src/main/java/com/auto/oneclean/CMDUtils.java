/*
 * *******************************************************************************
 *         文 件：CMDUtils.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月19日 14:34:20
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CMDUtils {

    private static final String TAG = "CMDUtils";

    public static class CMD_Result {
        public int resultCode;
        public String error;
        public String success;

        public CMD_Result(int resultCode, String error, String success) {
            this.resultCode = resultCode;
            this.error = error;
            this.success = success;
        }

    }

    /**
     * 执行命令
     *
     * @param command         命令
     * @param isShowCommand   是否显示执行的命令
     * @param isNeedResultMsg 是否反馈执行的结果
     * @retrun CMD_Result
     */
    public static CMD_Result runCMD(String command, boolean isShowCommand,
                                    boolean isNeedResultMsg) {
        if (isShowCommand)
            Log.i(TAG, "runCMD:" + command);
        CMD_Result cmdRsult = null;
        int result;
        Process process = null;
        PrintWriter pw = null;
        try {
            process = Runtime.getRuntime().exec("su"); //获取root权限
            pw = new PrintWriter(process.getOutputStream());
            pw.println(command);
            pw.flush();
            result = process.waitFor();
            Log.e(TAG, "==================runCMD: "+command);
            if (isNeedResultMsg) {
                StringBuilder successMsg = new StringBuilder();
                StringBuilder errorMsg = new StringBuilder();
                BufferedReader successResult = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                BufferedReader errorResult = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
                cmdRsult = new CMD_Result(result, errorMsg.toString(),
                        successMsg.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "run CMD:" + command + " failed");
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return cmdRsult;
    }
}