package com.auto.oneclean;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Processer mProcesser;
    UiDevice mDevice;
    private static final String CLEAN_PKG_NAME = "android.lite.clean";

    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.auto.oneclean", appContext.getPackageName());

        mDevice = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation());
        mProcesser = new Processer(mDevice, CLEAN_PKG_NAME);

        mProcesser.startApp();
        mProcesser.waitAMonent(3);
        goToOneStart();
        goToHomeInterface();

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
            userLayout.click();

            goToCloseRedPkg();

        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            mDevice.pressBack();
        }

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
        goToUpdate();
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
            } else {
                userLayout = new UiObject(new UiSelector().className("android.widget.LinearLayout")
                        .resourceId("android.lite.clean:id/a6r"));
                userLayout.click();
            }
            mProcesser.waitAMonent(1);
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            mDevice.pressBack();
        }
        goSignIn();
    }

    /**
     * 清理大师更新提示 点击忽略
     */
    public void goToUpdate() {
        mProcesser.pritLog("====================goToUpdate()=====================");
        mProcesser.waitAMonent(2);
        try {
            if (mProcesser.exitObjById("android.lite.clean:id/h5", 1)) {
                mDevice.pressBack();
            } else {
                goToHomeInterface();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            mDevice.pressBack();
        }
    }

    public static String sms, cellPhoneNumberSJ, cellPhoneNumber;

    /**
     * 登录注册
     */
    UiObject userSignIn;
    public HttpGet httpGet = new HttpGet();

    public void goSignIn() {

        mProcesser.pritLog("====================goSignIn()=====================");
        try {
            userSignIn = new UiObject(new UiSelector().className("android.widget.RelativeLayout")
                    .resourceId("android.lite.clean:id/vj"));
            userSignIn.click();
            mProcesser.pritLog("============== HttpGet()  初始化 请求数据 ===================");
            httpGet.getData();
            mProcesser.waitAMonent(30);
            UiObject userSignIns = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("android.lite.clean:id/wj"));
            //userSignIn = new UiObject(new UiSelector().className("android.widget.TextView"));

            while (mProcesser.exitObjById("android.lite.clean:id/vj", 1)) {
                userSignIn.click();
                mProcesser.waitAMonent(30);
            }
            cellPhoneNumberSJ = httpGet.getCellNumberSJ();
            mProcesser.waitAMonent(2);
            //cellPhoneNumber = httpGet.getCellNumberZJSJH("15686468495");
            //cellPhoneNumber = "15686468495";
            //httpGet.getSmsMsg(cellPhoneNumberSJ);

            //sms = httpGet.getSmsMsg(cellPhoneNumberSJ);

            if (mProcesser.exitObjById("android.lite.clean:id/wj", 1)) {
                mProcesser.pritLog(" =======   cellPhoneNumber : +++++++++++++++ " + cellPhoneNumberSJ);
                userSignIns.setText(cellPhoneNumberSJ);
            } else {
                userSignIn.click();
            }
            int i = 0;
            while (sms == null && i < 6) {

                mProcesser.pritLog("-----------cellPhoneNumber ---------短信获取次数------   " + i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mProcesser.pritLog("-----------cellPhoneNumber ---------------   " + sms);
                            sms = httpGet.getSmsMsg(cellPhoneNumberSJ);

                            TimeUnit.SECONDS.sleep(5);
                        } catch (
                                InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            mDevice.pressBack();
        }
        mProcesser.waitAMonent(10);
        smsInput(sms);
    }

    /**
     * 短信验证码输入
     */
    public void smsInput(String sms) {

        //UiObject smsinput = new UiObject(new UiSelector().className("android.widget.LinearLayout"));
        try {
            UiObject smsinput = new UiObject(new UiSelector().className("android.widget.LinearLayout"));
            UiObject smstext = smsinput.getFromParent(new UiSelector().className("android.widget.TextView"));
            smstext.setText(sms);
        } catch (UiObjectNotFoundException e) {
            mProcesser.waitAMonent(1);
            e.printStackTrace();
            mDevice.pressBack();
        }
    }

}
