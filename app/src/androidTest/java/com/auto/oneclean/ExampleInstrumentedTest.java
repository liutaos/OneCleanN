package com.auto.oneclean;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
    UiObject userSignIn;
    public HttpGet httpGet = new HttpGet();
    boolean result = true;

    public void goSignIn() {

        mProcesser.pritLog("====================goSignIn()=====================");
        try {
            userSignIn = new UiObject(new UiSelector().className("android.widget.RelativeLayout")
                    .resourceId("android.lite.clean:id/vj"));
            userSignIn.click();
            mProcesser.pritLog("============== HttpGet()  初始化 请求数据 ===================");
            httpGet.getData();
            mProcesser.waitAMonent(30);
            while (mProcesser.exitObjById("android.lite.clean:id/vj", 1)) {
                mProcesser.waitAMonent(1);
                userSignIn.click();
                mProcesser.waitAMonent(30);
            }
            mProcesser.waitAMonent(1);

            mProcesser.waitAMonent(2);
            UiObject userSignIns = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("android.lite.clean:id/wj"));
            //-----------------     取手机号   随机取      ----
            cellPhoneNumberSJ = httpGet.getCellNumberSJ();
            if (mProcesser.exitObjById("android.lite.clean:id/wj", 1)) {
                mProcesser.pritLog(" =======   cellPhoneNumber : +++++++++++++++ " + cellPhoneNumberSJ);
                userSignIns.setText(cellPhoneNumberSJ);
            } else {
                userSignIn.click();
            }
            int i = 0;
            while (!result) {
                mProcesser.pritLog("-----------cellPhoneNumber ---------短信获取次数------   " + i);
                try {
                    Log.e("com.aotu.clean", "      Thread  run: ");
                    mProcesser.pritLog("-----------cellPhoneNumber ---------------   " + sms);
                    //Thread.sleep(5 * 1000);
                    TimeUnit.SECONDS.sleep(11);
                    i++;
                    if (sms == null) {
                        sms = httpGet.getSmsMsg(cellPhoneNumberSJ);
                        if (i == 6) {
                            System.out.println("======   ==== result = false ======");
                            result = true;
                        }
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
        StringBuffer sb = new StringBuffer(sms_number);
        System.out.println(sb.charAt(0));
        System.out.println(sb.charAt(1));
        System.out.println(sb.charAt(2));
        System.out.println(sb.charAt(3));
        System.out.println(sb.charAt(4));
        System.out.println(sb.charAt(5));
        try {
            UiObject smsinput = new UiObject(new UiSelector().className("android.widget.LinearLayout"));
            UiObject smstext1 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(0));
            UiObject smstext2 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(2));
            UiObject smstext3 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(3));
            UiObject smstext4 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(4));
            UiObject smstext5 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(5));
            UiObject smstext6 = smsinput.getFromParent(new UiSelector().className("android.widget.TextView").index(6));

            smstext1.setText(String.valueOf(sb.charAt(0)));
            smstext2.setText(String.valueOf(sb.charAt(1)));
            smstext3.setText(String.valueOf(sb.charAt(2)));
            smstext4.setText(String.valueOf(sb.charAt(3)));
            smstext5.setText(String.valueOf(sb.charAt(4)));
            smstext6.setText(String.valueOf(sb.charAt(5)));

            mProcesser.waitAMonent(2);
        } catch (UiObjectNotFoundException e) {
            mProcesser.waitAMonent(1);
            e.printStackTrace();
            //mDevice.pressBack();
        }
    }

}
