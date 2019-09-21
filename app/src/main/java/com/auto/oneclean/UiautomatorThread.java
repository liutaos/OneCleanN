package com.auto.oneclean;


import android.util.Log;

class UiautomatorThread extends Thread {
    String pName;
    String cName;
    String mName;
    private String TAG="UiautomatorThread";

    public UiautomatorThread(String pkgName,String className,String methodName){
        this.pName=pkgName;
        this.cName=className;
        this.mName=methodName;
    }
    @Override
    public void run() {
        super.run();
        String command = generateCommand(pName, cName, mName);
        CMDUtils.CMD_Result rs= CMDUtils.runCMD(command,true,true);
        Log.e(TAG, "run: " + rs.error + "-------" + rs.success);
    }

    /**
     * 生成命令
     * @param pkgName uiautomator包名
     * @param clsName uiautomator类名
     * @param mtdName uiautomator方法名
     * @return
     */
    public  String generateCommand(String pkgName, String clsName, String mtdName) {
        String command = "am instrument -w -r -e debug false -e class '"
                + pkgName + "." + clsName + "#" + mtdName + "' "
                + pkgName + ".test/androidx.test.runner.AndroidJUnitRunner";
        Log.e("test1: ", command);
        return command;
    }

}