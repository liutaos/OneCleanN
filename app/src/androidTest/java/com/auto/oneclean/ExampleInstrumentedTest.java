/*
 * *******************************************************************************
 *         文 件：ExampleInstrumentedTest.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月25日 11:03:46
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObject2Condition;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.auto.oneclean.tools.FileTools;
import com.auto.oneclean.tools.ReadTextFile;
import com.auto.oneclean.tools.RootCmd;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = "com.auto.oneclean";
    private Processer mProcesser;
    UiDevice mDevice;
    private static final String CLEAN_PKG_NAME = "android.lite.clean";

    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private final String SDCARD = "/storage/emulated/0/Out/";

    public static String sms, cellPhoneNumberSJ, cellPhoneNumber;

    public List<String> mApplist = new ArrayList();
    public boolean next = true;
    Context appContext;
    public boolean canclled = true;
    RootCmd rootCmd;
    Thread update;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.auto.oneclean", appContext.getPackageName());
        mDevice = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation());
        mProcesser = new Processer(mDevice, CLEAN_PKG_NAME);
        rootCmd = new RootCmd();
        ReadTextFile read = new ReadTextFile();
        //  无线循环运行 直至没有手机号码停止运行
        while (true) {

            update = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (canclled) {
                        mProcesser.waitAMonent(2);
                        try {
                            //if (mProcesser.exitObjById("android.lite.clean:id/h5", 1)) {
                            if (mDevice.hasObject(By.text("忽略"))) {
                                mProcesser.pritLog("====================goToUpdate()=====================");
                                UiObject2 ignore = mDevice.wait(Until.findObject(By.text("忽略")), 500);
                                ignore.click();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProcesser.waitAMonent(1);
                        } finally {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.e(TAG, "run: ======          join ()");
                }
            });
            canclled = true;
            unstallApp();
            mProcesser.pritLog("======      正在清理 清理大师数据  = =======" + CLEAN_PKG_NAME);
            //mDevice.executeShellCommand("pm clear " + CLEAN_PKG_NAME);
            clear();
            mProcesser.pritLog("======      已清理 清理大师数据  = =======" + CLEAN_PKG_NAME);
            //sms = null;
            Thread.sleep(3 * 1000);
            String phone_number = read.read("/storage/emulated/0/tmp_number.txt");
            cellPhoneNumber = getStringNoBlank(phone_number);
            if (cellPhoneNumber == "" || cellPhoneNumber == null) {
                mProcesser.pritLog("脚本执行结束  号码为空");
                break;
            }
            Thread.sleep(3 * 1000);
            read.write("/storage/emulated/0/tmp_number.txt");
            Thread.sleep(3 * 1000);
            mProcesser.startApp();
            mProcesser.waitAMonent(10);
            goToOneStart();
            goToUpdate();
            mProcesser.waitAMonent(2);
            goToHomeInterface();
            mProcesser.waitAMonent(1);
            update.join(3);
            goToCloseRedPkg();
            if(sms == null){
                Log.e(TAG, "useAppContext:   短信获取失败 进行下一个号码" );
                continue;
            }
            mProcesser.waitAMonent(2);
            try {
                if (next) {
                    update.join(3);
                    mainTasks();
                } else {
                    break;
                }


            } catch (Exception e) {
                e.printStackTrace();
                mProcesser.pritLog("程序异常重启运行中。。。。");
                try {
                    mDevice.executeShellCommand("am force-stop " + CLEAN_PKG_NAME);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                mProcesser.startApp();
                mProcesser.waitAMonent(10);
                update.join(3);
                if (mDevice.hasObject(By.text("清鲤福利"))) {
                    mDevice.wait(Until.findObject(By.text("清鲤福利")), 500).click();
                    mProcesser.waitAMonent(2);
                    update.join(3);
                    mainTasks();
                }
            }
            canclled = false;
            update.join();
            mProcesser.pritLog("===================进行下一个号码=====================");

        }
    }

    public static String getStringNoBlank(String str) {

        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return str;
        }
    }

    /**
     * 清理清理大师数据
     */
    public void clear() {
        String result = rootCmd.execRootCmd("pm clear " + CLEAN_PKG_NAME);
        Log.i("com.auto.oneclean", "清理清理大师数据   = " + result);
    }

    /**
     * 卸载黑名单应用
     */
    public void unstallApp() {
        //白名单
        mApplist.add("de.robv.android.xposed.installer");
        mApplist.add("android.lite.clean");
        mApplist.add("zpp.wjy.xxsq");
        mApplist.add("com.nhnhnhnnnnnnjjjijhjg.apt.yeshenqinglidashi");
        mApplist.add("com.tencent.mm");
        mApplist.add("com.xiezaiyy");
        mApplist.add("com.topjohnwu.magisk");
        mApplist.add("com.auto.oneclean");
        mApplist.add("com.auto.oneclean.test");
        mApplist.add("com.auto.onewechat");
        mApplist.add("com.auto.onewechat.test");

        String listPackage = rootCmd.execRootCmd("pm list package -3");
        mProcesser.pritLog("包名：" + listPackage);

        String[] ss = listPackage.split("package:");

        for (String s : ss) {
            if (!mApplist.contains(s)) {
                Log.e("com.auto.oneclean", "正在卸载  package = " + s);
                //黑名单
                String result = rootCmd.execRootCmd("pm uninstall " + s);
                Log.i("com.auto.oneclean", "卸载  package = " + result);
            }
        }
    }

    /**
     * 清理大师主页
     */
    public void goToHomeInterface() {
        mProcesser.pritLog("====================goToHomeInterface()=====================");
        UiObject userLayout;
        try {
            userLayout = new UiObject(new UiSelector().className("android.widget.LinearLayout")
                    .resourceId("android.lite.clean:id/a6r"));
            update.join(3);
            userLayout.click();
            if (mDevice.hasObject(By.text("登录任务中心"))) {
                UiObject2 sigin = mDevice.wait(Until.findObject(By.text("登录任务中心")), 500);
                update.join(3);
                sigin.click();
                goSignIn();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            if(sms == null){
                Log.e(TAG, "useAppContext:   短信获取失败 进行下一个号码" );
                return;
            }
            //mDevice.pressBack();
        }finally {
            if(sms == null){
                Log.e(TAG, "useAppContext:   短信获取失败 进行下一个号码" );
                return;
            }
        }
        mProcesser.waitAMonent(1);
        //goToCloseRedPkg();
    }

    /**
     * 清理大师首次启动页 点击启动
     */

    public void goToOneStart() {
        mProcesser.pritLog("============== goToOneStart ===================");
        UiObject userOK = new UiObject(new UiSelector().className("android.widget.RelativeLayout")
                .resourceId("android.lite.clean:id/s4"));
        try {
            userOK.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        mProcesser.waitAMonent(3);

    }

    /**
     * 打开红包
     */
    public void goToCloseRedPkg() {
        mProcesser.pritLog("====================goToCloseRedPkg()=====================");
        UiObject userOpenRedPkg, userLayout;
        try {
            if (mProcesser.exitObjById("android.lite.clean:id/x3", 1)) {
                userOpenRedPkg = new UiObject(new UiSelector().className("android.widget.ImageView")
                        .resourceId("android.lite.clean:id/x3"));
                userOpenRedPkg.click();
                mProcesser.waitAMonent(1);
                goSignIn();
            } else {
                userLayout = new UiObject(new UiSelector().className("android.widget.LinearLayout")
                        .resourceId("android.lite.clean:id/a6r"));
                userLayout.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            mDevice.pressBack();
        }

    }

    /**
     * 清理大师更新提示 点击忽略
     */
    public void goToUpdate() {
        update.start();
        goToHomeInterface();
    }


    /**
     * 登录注册
     */

    public HttpGet httpGet = new HttpGet();
    public UiObject userSignIn;

    public void goSignIn() {

        mProcesser.pritLog("====================goSignIn()=====================");
        try {
            while (mDevice.hasObject(By.text("手机号码一键登录"))) {
                UiObject2 userSignin = mDevice.wait(Until.findObject(By.text("手机号码一键登录")), 500);
                userSignin.click();
                mProcesser.waitAMonent(40);
                Thread.sleep(40 * 1000);
            }
            mProcesser.pritLog("============== HttpGet()  初始化 请求数据 ===================");
            httpGet.getData();
            mProcesser.waitAMonent(2);
            UiObject userSignIns = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("android.lite.clean:id/wj"));
            //-----------------     取手机号   随机取      ----
            /*cellPhoneNumberSJ = httpGet.getCellNumberSJ();

            if (mProcesser.exitObjById("android.lite.clean:id/wj", 1)) {
                mProcesser.pritLog(" =======   cellPhoneNumber : +++++++++++++++ " + cellPhoneNumberSJ);
                userSignIns.setText(cellPhoneNumberSJ);
            }
            sms = httpGet.getSmsMsg(cellPhoneNumberSJ);*/

            //-----------------     取手机号   指定号码      ----
            cellPhoneNumber = httpGet.getCellNumberZJSJH(cellPhoneNumber);
            //-----------------------      等待短信验证码 ------------
            boolean result = false;
            int i = 0;
            sms = null;
            //------------------------输入手机号
            if (mProcesser.exitObjById("android.lite.clean:id/wj", 1)) {
                mProcesser.pritLog(" =======   cellPhoneNumber : +++++++++++++++    " + cellPhoneNumber);
                userSignIns.setText(cellPhoneNumber);
            }
            while (!result && sms == null) {
                sms = httpGet.getSmsMsg(cellPhoneNumber);
                mProcesser.pritLog("-----------cellPhoneNumber ---------短信获取次数------   " + i);
                try {
                    TimeUnit.SECONDS.sleep(11);
                    if (sms == null) {
                        sms = httpGet.getSmsMsg(cellPhoneNumber);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //TimeUnit.SECONDS.sleep(11);
                //sms = httpGet.getSmsMsg(cellPhoneNumberSJ);

                mProcesser.pritLog("-----------  SMS  ---------------   " + sms);
                i++;
                if (i == 6) {
                    System.out.println("======   ==== result = false ======");
                    result = true;
                }
            }
            if (sms == null) {
                next = false;
                System.out.println("==================   SMS  信息== 空  下一步 =========     " + sms);
                return;
            }
            String sms_number = Pattern.compile("[^0-9]").matcher(sms).replaceAll("");
            System.out.println("==================   SMS  信息==================     " + sms);
            sms = sms_number;
            smsInput(sms);

        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            //mDevice.pressBack();
        }

    }

    /**
     * 短信验证码输入
     */
    public void smsInput(String sms_number) throws Exception {
        System.out.println("     legacySetText      " + sms_number);
        if (sms_number == null) {
            System.out.println("          空         ");
            return;
        }
        UiObject smstext1 = new UiObject(new UiSelector().className("android.widget.TextView").instance(2));
        smstext1.legacySetText(sms_number);
        sms = null;
        mProcesser.waitAMonent(2);
        httpGet.singOut();
        //okGotIt();
        if (mDevice.hasObject(By.text("知道了"))) {
            UiObject2 objNext = mDevice.wait(Until.findObject(By.text("知道了")), 500);
            objNext.click();
        }

    }

    /**
     * 知道了 按钮 点击
     */
    public void okGotIt() throws Exception {

        //UiObject ok_next = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/et"));
        UiObject2 ok_next;
        while (mDevice.hasObject(By.text("知道了"))) {
            ok_next = mDevice.wait(Until.findObject(By.text("知道了")), 500);
            ok_next.click();
            mProcesser.waitAMonent(2);
            Log.i(TAG, "点击知道了=====1111======");
            Thread.sleep(1000);
        }
    }

    /**
     * 个人中心 点击各种 项目
     */

    public void mainTasks() throws Exception {

        //金币明细  Details of gold coins
        System.out.println("=================  mainTasks () 执行业务模块 ========================");
        mProcesser.waitAMonent(2);
        System.out.println("=================  mainTasks () =  金币明细  开始========================");
        if (mDevice.hasObject(By.text("当前网络不畅通，可点击重试"))) {
            //android.lite.clean:id/a0j   android.widget.FrameLayout
            UiObject refreshNetwork = new UiObject(new UiSelector().className("android.widget.FrameLayout").resourceId("android.lite.clean:id/a0j"));
            refreshNetwork.click();
            mProcesser.waitAMonent(3);
        }
        UiObject2 detailed;
        update.join(3);
        detailed();
        mProcesser.waitAMonent(3);
        System.out.println("=================  mainTasks () =  金币明细  结束========================");
        while (!mDevice.hasObject(By.text("任务攻略"))) {
            detailed();
        }
        update.join(3);
        if (mDevice.hasObject(By.text("已完成"))) {
            int step = 200 / 10;
            int x = 1080 / 2;
            if (!mDevice.hasObject(By.text("去试玩"))) {
                mDevice.swipe(x, 1080, x, 0, step);
            }
            if (mDevice.wait(Until.findObjects(By.text("已完成")), 500).size() == 3) {
                if (!mDevice.hasObject(By.text("金币明细"))) {
                    mDevice.swipe(x, 360, x, 1080, step);
                }
                detailed();
                writeOut();
                Log.e(TAG, "mainTasks:  所有任务完成 下一个号码");
                return;
            }
        }

        update.join(3);
        // ========================  清理手机任务=========================
        clickClear();
        mProcesser.waitAMonent(2);
        update.join(3);
        //===========================  试玩  ============================
        playApp(3, 2);
        mProcesser.waitAMonent(2);

        //============================ 观看  ===========================
        //WwatchVideo();
        //mProcesser.waitAMonent(2);
        update.join(3);
        //========================  乐园  =================================13279508005
        game();

        mProcesser.pritLog("任务执行完毕================");
        mProcesser.waitAMonent(2);

        writeOut();
    }

    /**
     * 刷新金币
     */
    public void detailed() throws Exception {
        boolean closeDeataild = false;
        //UiObject2 detailed;
        UiObject detailed1, titleDetailde;
        detailed1 = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/zy"));
        UiObject2 detailed2 = mDevice.wait(Until.findObject(By.text("金币明细")), 500);
        if (detailed2 != null) {
            mDevice.waitForWindowUpdate(CLEAN_PKG_NAME, 3 * 1000);
            //detailed1 = mDevice.wait(Until.findObject(By.text("金币明细")), 500);
            detailed2.click();
            mDevice.waitForWindowUpdate(CLEAN_PKG_NAME, 3 * 1000);
            Thread.sleep(3000);
            //Log.e(TAG, "  detailed1   " + detailed2);
            titleDetailde = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/rw"));
            if (titleDetailde.getText().equals("金币明细")) {
                mProcesser.waitAMonent(1);
                mDevice.pressBack();
            }
            Thread.sleep(1000);

        } else if (mDevice.hasObject(By.text("清鲤福利"))) {
            mDevice.wait(Until.findObject(By.text("清鲤福利")), 500).click();
            mProcesser.waitAMonent(2);
            mainTasks();
        }
    }


    /**
     * 点击清理任务
     */
    public void clickClear() throws Exception {
        Log.e(TAG, "clickClear: ============   clickClear   =======  ");
        //detailed();
        int step = 200 / 10;
        int x = 1080 / 2;
        if (!mDevice.hasObject(By.text("去试玩"))) {
            mDevice.swipe(x, 1080, x, 0, step);
        }

        List<UiObject2> getitem = mDevice.wait(Until.findObject(By.res("android.lite.clean:id/a0i")), 500).getChildren().get(1).getChildren();
        if (getitem.get(5).getText().equals("已完成")) {
            if (!mDevice.hasObject(By.text("金币明细"))) {
                mDevice.swipe(x, 360, x, 1080, step);
            }
            Log.e(TAG, "clickClear:    已完成任务  进行下一项任务 ");
            detailed();
            Thread.sleep(1000);
            return;
        }
        mProcesser.clickListView("android.lite.clean:id/a0i", 1, 1);
        UiObject cleanPhone = new UiObject(new UiSelector().className("android.widget.Button").instance(0));
        mProcesser.waitAMonent(2);
        if (!mDevice.hasObject(By.text("清理"))) {
            goToHomeInterface();
            return;
        }
        if (cleanPhone != null) {
            cleanPhone.click();
        } else {
            mDevice.wait(Until.findObject(By.text("清鲤福利")), 500).click();
        }
        mProcesser.waitAMonent(6);
        //领取金币
        Thread.sleep(6 * 1000);
        mProcesser.waitAMonent(1);
        while (mDevice.hasObject(By.text("领取"))) {
            Log.e(TAG, "clickClear: ============   receive.click()   =======  ");
            mProcesser.waitAMonent(1);
            UiObject receive = new UiObject(new UiSelector().className("android.widget.RelativeLayout").resourceId("android.lite.clean:id/gr"));
            receive.click();
        }

        mProcesser.waitAMonent(1);
        okGotIt();
        if (!mDevice.hasObject(By.text("金币明细"))) {
            mDevice.swipe(x, 360, x, 1080, step);
        }
        //  领金币后刷新金币
        detailed();
        Thread.sleep(1000);
    }


    /**
     * 试玩项目
     *
     * @throws Exception
     */
    String unPkg;

    public void playApp(int i, int position) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    UiObject2 objNext = mDevice.wait(Until.findObject(By.text("确定")), 500);
                    if (objNext != null) {
                        Log.e(TAG, "=========     点击  程序ANR提示错误的 确定    =============");
                        objNext.click();
                        mProcesser.waitAMonent(1);
                    }
                }
            }
        }).start();
        UiObject2 detailed;
        //detailed();
        //}
        int step = 200 / 10;
        int x = 1080 / 2;
        //解锁屏幕 从屏幕底部往上面0坐标滑动

        if (!mDevice.hasObject(By.text("去试玩"))) {

            mDevice.swipe(x, 1080, x, 0, step);
        }
        List<UiObject2> getitem = mDevice.wait(Until.findObject(By.res("android.lite.clean:id/a0i")), 500).getChildren().get(position).getChildren();
        if (getitem.get(5).getText().equals("已完成")) {
            if (!mDevice.hasObject(By.text("金币明细"))) {
                mDevice.swipe(x, 360, x, 1080, step);
            }
            Log.e(TAG, "playApp:    已完成任务  进行下一项任务 ");
            detailed();
            Thread.sleep(1000);
            return;
        }
        //scroll(Direction direction, float percent)
        mProcesser.waitAMonent(3);
        // --------------------------试玩
        mProcesser.clickListView("android.lite.clean:id/a0i", position, 1);
        Thread.sleep(3 * 1000);
        if (!mDevice.hasObject(By.text("下载"))) {
            mProcesser.waitAMonent(2);
            mDevice.pressBack();
            mProcesser.waitAMonent(2);
            if (!mDevice.hasObject(By.text("金币明细"))) {
                mDevice.swipe(x, 360, x, 1080, step);
            }
            detailed();
            Log.e(TAG, "============   没有下载项 进行下一项任务 =============");

            return;
        }

        //创建Random类对象
        Random random = new Random();
        int j, k;
        //===================   判断下载的项是否超过 5 或 3 ============
        List<UiObject2> objs = mDevice.wait(Until.findObjects(By.text("下载")), 500);
        boolean next = true;
        if (objs.size() <= i) {
            k = objs.size();
        } else {
            k = i;
            next = false;
        }
        for (j = 0; j <= k; j++) {
            List<UiObject2> obj = mDevice.wait(Until.findObjects(By.text("下载")), 500);
            mProcesser.pritLog("============= 第几个循环    j  ==================" + j);
            //产生随机数
            int number;
            if (next) {
                number = j;
                obj.get(0).click();
            } else {
                number = random.nextInt(obj.size() - 1);
                obj.get(number).click();
            }
            mProcesser.waitAMonent(1);

            boolean download = false;
            while (!download) {
                mProcesser.pritLog("正在下载。。。。。。。。。。。。。。。。。。。。。。");
                Thread.sleep(10 * 1000);
                mProcesser.waitAMonent(1);
                if (mDevice.hasObject(By.text("取消"))) {
                    mProcesser.pritLog("下载完成自动安装。。。。。。。。。。。。。。。。。。。。。。");
                    download = true;
                    mProcesser.waitAMonent(1);
                    Thread.sleep(500);
                }
                //自动打开已下载的APP失败 处理
                if (mDevice.hasObject(By.text("安装"))) {
                    UiObject2 install = mDevice.wait(Until.findObject(By.text("安装")), 500);
                    install.click();
                }
            }
            mProcesser.waitAMonent(1);
            Thread.sleep(1000);
            while (mDevice.hasObject(By.text("下一步"))) {
                UiObject2 objNext = mDevice.wait(Until.findObject(By.text("下一步")), 500);
                objNext.click();
                mProcesser.waitAMonent(1);
                Thread.sleep(1000);
            }
            mProcesser.waitAMonent(2);
            Thread.sleep(1500);
            if (mDevice.hasObject(By.text("安装"))) {
                Log.e(TAG, "playApp: 正在安装--------------");
                UiObject2 objInstall = mDevice.wait(Until.findObject(By.text("安装")), 500);
                if (objInstall == null) {
                    break;
                } else {
                    objInstall.click();
                }
                mProcesser.waitAMonent(2);
                Thread.sleep(1000);
            }
            mProcesser.waitAMonent(2);
            boolean lising = false;
            while (!lising) {
                mProcesser.pritLog(" =======   等待安装完成    ========");
                Thread.sleep(5 * 1000);
                lising = mDevice.hasObject(By.text("立即清理"));
                if (mDevice.hasObject(By.text("立即清理"))) {
                    UiObject2 objCleer = mDevice.wait(Until.findObject(By.text("立即清理")), 500);
                    objCleer.click();
                    lising = true;
                }
            }
            mProcesser.waitAMonent(1);
            //------------点击领取
            open();
            mProcesser.waitAMonent(2);
            String pkg = mDevice.getCurrentPackageName();
            unPkg = pkg;
            mProcesser.pritLog("包名======  1 ========" + pkg);

            if (unPkg.equals("android.lite.clean")) {
                j--;
                continue;
            }
            //---------------关闭打开的应用
            closeAPP();
            if (mDevice.hasObject(By.text("试玩失败"))) {
                UiObject2 failedtrial;
                while (mDevice.hasObject(By.text("知道了"))) {
                    failedtrial = mDevice.wait(Until.findObject(By.text("知道了")), 500);
                    failedtrial.click();
                    mProcesser.waitAMonent(2);
                    Log.i(TAG, "点击知道了===========");
                }
                break;
            }
            //领取金币
            mProcesser.waitAMonent(3);
            okGotIt();
            mProcesser.waitAMonent(1);
        }
        mProcesser.waitAMonent(2);
        mDevice.pressBack();
        mProcesser.waitAMonent(1);

        if (!mDevice.hasObject(By.text("金币明细"))) {
            mDevice.swipe(x, 360, x, 1080, step);
        }
        //  领金币后刷新金币
        detailed();
        mProcesser.waitAMonent(2);

    }

    /**
     * 打开安装的应用
     *
     * @throws Exception
     */
    public void open() throws UiObjectNotFoundException {
        UiObject2 objClear;
        mProcesser.waitAMonent(3);
        if (mDevice.hasObject(By.text("打开领取"))) {
            objClear = mDevice.wait(Until.findObject(By.text("打开领取")), 500);
            objClear.click();
            mProcesser.waitAMonent(1);
        } else {
            int step = 200 / 10;
            int x = 1080 / 2;
            //解锁屏幕 从屏幕底部往上面0坐标滑动
            mDevice.swipe(x, 1080, x, 0, step);
            //scroll(Direction direction, float percent)
            objClear = mDevice.wait(Until.findObject(By.text("打开领取")), 500);
            mProcesser.waitAMonent(3);
            if (mDevice.hasObject(By.text("打开领取"))) {
                objClear.click();
            } else {
                mDevice.swipe(x, 1080, x, 0, step);
                mProcesser.waitAMonent(3);
                if (mDevice.hasObject(By.text("打开领取"))) {
                    objClear.click();
                }
            }
            mProcesser.waitAMonent(3);
        }
        mProcesser.waitAMonent(2);
    }

    /**
     * 关闭自动打开的应用
     *
     * @throws Exception
     */
    public void closeAPP() throws Exception {
        //  ===============   关闭自动打开的APP

        if (!unPkg.equals("android.lite.clean")) {
            mProcesser.waitAMonent(1);
            mDevice.executeShellCommand("am force-stop " + unPkg);
            mProcesser.pritLog("======      已关闭   = =======" + unPkg);
            mProcesser.waitAMonent(1);
            mDevice.executeShellCommand("pm uninstall " + unPkg);
            mProcesser.pritLog("======      已卸载   = =======" + unPkg);
        }
        mProcesser.waitAMonent(2);
        if (mDevice.hasObject(By.text("立即清理"))) {
            UiObject2 objClear = mDevice.wait(Until.findObject(By.text("立即清理")), 500);
            objClear.click();
            mProcesser.waitAMonent(1);
            Thread.sleep(500);
        }
    }

    /**
     * 观看视频
     */
    public void WwatchVideo() throws Exception {
        int step = 200 / 10;
        int x = 1080 / 2;
        //解锁屏幕 从屏幕底部往上面0坐标滑动

        if (!mDevice.hasObject(By.text("去观看"))) {

            mDevice.swipe(x, 1080, x, 0, step);
            mProcesser.waitAMonent(1);
        }
        UiObject detailed = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/zy"));
        mProcesser.clickListView("android.lite.clean:id/a0i", 3, 2);
        mProcesser.waitAMonent(10);
        while (mDevice.hasObject(By.text("去观看"))) {
            mProcesser.clickListView("android.lite.clean:id/a0i", 3, 2);
            mProcesser.waitAMonent(2);
        }
        //点击观看
        UiObject resualt = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("android.lite.clean:id/a3q"));
        if (resualt != null) {
            resualt.click();
            mProcesser.waitAMonent(2);
            //resualt = new UiObject(new UiSelector().className("android.widget.RelativeLayout").resourceId("android.lite.clean:id/vr"));
            //resualt.click();
            UiObject2 resualts = mDevice.wait(Until.findObject(By.text("刷新")), 500);
            resualts.click();
            mProcesser.waitAMonent(2);
            mDevice.getInstance().click(360, 990);
            //if (mDevice.click(360, 990)) {
            mProcesser.waitAMonent(12);
            //}
        }


        //x,y是坐标,坐标获取方式
        /*if (mDevice.click(360, 450)) {
            mProcesser.waitAMonent(15);
        } */

        mDevice.pressBack();
        okGotIt();
        if (!mDevice.hasObject(By.text("金币明细"))) {
            mDevice.swipe(x, 0, x, 1080, step);
        }
        while (!mDevice.hasObject(By.text("金币明细"))) {
            mProcesser.waitAMonent(1);
        }
        mProcesser.waitAMonent(1);
        detailed.click();
        mProcesser.waitAMonent(2);
    }

    /**
     * 乐园
     */

    public void game() throws Exception {
        playApp(5, 4);
    }

    FileTools mFile;

    /**
     * 备注金币信息到SD卡
     */

    public void writeOut() throws UiObjectNotFoundException {
        mFile = new FileTools();
        Calendar now = Calendar.getInstance();
        //   System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
        //    System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        mProcesser.waitAMonent(3);
        UiObject jBNumber = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/zw"));
        if (jBNumber != null) {
            String jb = "==" + jBNumber.getText() + " \n";
            //getSDCardPath();
            //mFile.writeTxtToFile(cellPhoneNumberSJ + jb, SDCARD, (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + "-金币.txt");
            mFile.writeTxtToFile(cellPhoneNumber + jb, SDCARD, (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + "-金币.txt");
            mProcesser.pritLog("SD 卡 输出的信息   " + cellPhoneNumber + jb);
        }
        mProcesser.pritLog("SD 卡 输出的路径   " + SDCARD + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + "-金币.txt");
    }

    /**
     * 得到sdcard的路径
     *
     * @return 返回一个字符串数组  下标0:内置sdcard  下标1:外置sdcard
     */
    public static String[] getSDCardPath() {
        String[] sdCardPath = new String[2];
        File sdFile = Environment.getExternalStorageDirectory();
        File[] files = sdFile.getParentFile().listFiles();
        for (File file : files) {
            if (file.getAbsolutePath().equals(sdFile.getAbsolutePath())) {//外置
                sdCardPath[1] = sdFile.getAbsolutePath();
                System.out.println(sdCardPath[1]);
            } else if (file.getAbsolutePath().contains("sdcard")) {//得到内置sdcard
                sdCardPath[0] = file.getAbsolutePath();
                System.out.println(sdCardPath[2]);
            }
        }

        return sdCardPath;
    }

}
