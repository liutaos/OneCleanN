package com.auto.oneclean;

import android.content.Context;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static org.junit.Assert.assertEquals;

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
        //读取指定号码
        //appContext.getApplicationContext().getResources().getText(R.id.clearBtn);

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
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            //mDevice.pressBack();
        }
        mProcesser.waitAMonent(1);
        goToCloseRedPkg();
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

    public HttpGet httpGet = new HttpGet();
    public UiObject userSignIn;

    public void goSignIn() {

        mProcesser.pritLog("====================goSignIn()=====================");
        try {
            if (mProcesser.exitObjById("android.lite.clean:id/vj", 1)) {
                userSignIn = new UiObject(new UiSelector().className("android.widget.RelativeLayout").resourceId("android.lite.clean:id/vj"));
                userSignIn.click();

                mProcesser.waitAMonent(30);
                while (mProcesser.exitObjById("android.lite.clean:id/vj", 1)) {
                    mProcesser.waitAMonent(1);
                    userSignIn.click();
                    mProcesser.waitAMonent(30);
                }
            }
            mProcesser.pritLog("============== HttpGet()  初始化 请求数据 ===================");
            httpGet.getData();
            mProcesser.waitAMonent(2);
            UiObject userSignIns = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("android.lite.clean:id/wj"));
            //-----------------     取手机号   随机取      ----
            cellPhoneNumberSJ = httpGet.getCellNumberSJ();
            if (mProcesser.exitObjById("android.lite.clean:id/wj", 1)) {
                mProcesser.pritLog(" =======   cellPhoneNumber : +++++++++++++++ " + cellPhoneNumberSJ);
                userSignIns.setText(cellPhoneNumberSJ);
            }
            sms = httpGet.getSmsMsg(cellPhoneNumberSJ);
            boolean result = false;
            int i = 0;
            while (!result && sms == null) {
                mProcesser.pritLog("-----------cellPhoneNumber ---------短信获取次数------   " + i);
                try {
                    Log.e("com.aotu.clean", "      Thread  run: ");
                    TimeUnit.SECONDS.sleep(11);
                    //TimeUnit.SECONDS.sleep(11);
                    sms = httpGet.getSmsMsg(cellPhoneNumberSJ);
                    mProcesser.pritLog("-----------cellPhoneNumber ---------------   " + sms);

                    i++;
                    if (i == 6) {
                        System.out.println("======   ==== result = false ======");
                        result = true;
                    }
                } catch (
                        InterruptedException e) {
                    e.printStackTrace();
                }
                String sms_number = Pattern.compile("[^0-9]").matcher(sms).replaceAll("");
                sms = sms_number;
                System.out.println("==================   SMS  信息==================     " + sms);

            }
            //cellPhoneNumber = httpGet.getCellNumberZJSJH("15686468495");
            //cellPhoneNumber = "15686468495";
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
    public void smsInput(String sms_number) {
        if (sms_number == null) return;
        System.out.println(sms_number);

        try {

            UiObject smstext1 = new UiObject(new UiSelector().className("android.widget.TextView").instance(2));
            smstext1.legacySetText(sms_number);
            mProcesser.waitAMonent(2);
        } catch (UiObjectNotFoundException e) {
            mProcesser.waitAMonent(1);
            e.printStackTrace();
            //mDevice.pressBack();
            mProcesser.pritLog("================错误============");
        }
    }

}
