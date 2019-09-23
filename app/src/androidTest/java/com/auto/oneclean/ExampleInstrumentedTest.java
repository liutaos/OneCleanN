package com.auto.oneclean;

import android.content.Context;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

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
        mProcesser.waitAMonent(5);
        //goToOneStart();
        goToUpdate();
        mProcesser.waitAMonent(2);
        goToHomeInterface();
        mProcesser.waitAMonent(20);
        try {
            mainTasks();
            //open();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=============  个人中心 错误  ============");
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
            userLayout.click();
        } catch (Exception e) {
            e.printStackTrace();
            mProcesser.waitAMonent(1);
            //mDevice.pressBack();
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
                } catch (InterruptedException e) {
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
            okGotIt();
        } catch (UiObjectNotFoundException e) {
            mProcesser.waitAMonent(1);
            e.printStackTrace();
            //mDevice.pressBack();
            mProcesser.pritLog("================错误============");
        }
    }

    /**
     * 知道了 按钮 点击
     */
    public void okGotIt() throws UiObjectNotFoundException {

        UiObject ok_next = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/et"));
        ok_next.click();
        mProcesser.waitAMonent(1);
    }

    /**
     * 个人中心 点击各种 项目
     */

    public void mainTasks() throws Exception {

        //金币明细  Details of gold coins
        System.out.println("=================mainTasks () = ========================");
        UiObject detailed = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("android.lite.clean:id/zy"));
        /*detailed.click();
        mProcesser.waitAMonent(2);
        mDevice.pressBack();
        System.out.println("=================mainTasks () =  金币明细========================");
        // 清理手机任务

        mProcesser.clickListView("android.lite.clean:id/a0i",1,1);
        UiObject cleanPhone = new UiObject(new UiSelector().className("android.widget.Button").instance(0));

        cleanPhone.click();
        mProcesser.waitAMonent(2);
        //领取金币
        UiObject receive = new UiObject(new UiSelector().className("android.widget.RelativeLayout").resourceId("android.lite.clean:id/gr"));
        receive.click();
        mProcesser.waitAMonent(1);
        okGotIt();
*/

        detailed.click();
        mProcesser.waitAMonent(2);
        mDevice.pressBack();
        // --------------------------试玩
        mProcesser.clickListView("android.lite.clean:id/a0i", 2, 1);
        mProcesser.waitAMonent(2);
        //==============================[618,242][684,278]
        //Find out the new added note entry
        List<UiObject2> obj = mDevice.wait(Until.findObjects(By.text("下载")), 500);
        int i;
        //创建Random类对象
        Random random = new Random();
        for (i = 0; i < 3; i++) {
            //产生随机数
            int number = random.nextInt(obj.size() - 0 + 1) + 0;

            obj.get(number).click();
            boolean download = true;
            while (download) {
                mProcesser.waitAMonent(30);
                if (mDevice.hasObject(By.text("下一步")) || mDevice.hasObject(By.text("安装"))) {
                    download = false;
                }
            }
            mProcesser.waitAMonent(1);
            if (mDevice.hasObject(By.text("下一步"))) {
                UiObject2 objNext = mDevice.wait(Until.findObject(By.text("下一步")), 500);
                objNext.click();
                mProcesser.waitAMonent(1);
                UiObject2 objInstall = mDevice.wait(Until.findObject(By.text("安装")), 500);
                objInstall.click();
            } else if (mDevice.hasObject(By.text("安装"))) {
                UiObject2 objInstall = mDevice.wait(Until.findObject(By.text("安装")), 500);
                objInstall.click();
            }
            mProcesser.waitAMonent(2);
            boolean lising = false;
            while (!lising) {
                Thread.sleep(10 * 1000);
                lising = mDevice.hasObject(By.text("立即清理"));
                mProcesser.pritLog(" =======   等待安装完成    ========");
                if (mDevice.hasObject(By.text("立即清理"))) {
                    UiObject2 objCleer = mDevice.wait(Until.findObject(By.text("立即清理")), 500);
                    objCleer.click();
                }
            }
            mProcesser.waitAMonent(1);
            //------------点击领取
            open();
            mProcesser.startApp();
            //领取金币
            mProcesser.waitAMonent(1);
            okGotIt();
        }
        mProcesser.waitAMonent(2);
        mDevice.pressBack();
        mProcesser.waitAMonent(1);
        detailed.click();
        mProcesser.waitAMonent(2);
        mDevice.pressBack();
        //============================ 观看  ===========================
        mProcesser.waitAMonent(2);
        videw();
        mProcesser.waitAMonent(1);
        detailed.click();
        mProcesser.waitAMonent(2);


    }


    /**
     * 打开安装的应用
     *
     * @throws Exception
     */
    public void open() throws Exception {
        UiObject2 objClear;
        if (mDevice.hasObject(By.text("打开领取"))) {
            objClear = mDevice.wait(Until.findObject(By.text("打开领取")), 500);
            objClear.click();
            mProcesser.waitAMonent(2);
        } else {
            int step = 200 / 10;
            int x = 1080 / 2;//
            //解锁屏幕 从屏幕底部往上面0坐标滑动
            mDevice.swipe(x, 1080, x, 0, step);
            //scroll(Direction direction, float percent)
            mProcesser.waitAMonent(1);
            if (mDevice.hasObject(By.text("打开领取"))) {
                objClear = mDevice.wait(Until.findObject(By.text("打开领取")), 500);
                objClear.click();
            } else {
                mDevice.swipe(x, 1080, x, 0, step);
                mProcesser.waitAMonent(2);
                objClear = mDevice.wait(Until.findObject(By.text("打开领取")), 500);
                objClear.click();
            }

            mProcesser.waitAMonent(2);
        }
        mProcesser.waitAMonent(1);
    }

    /**
     * 观看视频
     */
    public void videw() throws Exception {
        mProcesser.clickListView("android.lite.clean:id/a0i", 3, 1);
        mProcesser.waitAMonent(2);
        //点击观看
        //x,y是坐标,坐标获取方式
        mDevice.click(360, 450);
        mProcesser.waitAMonent(12);
        mDevice.pressBack();
        okGotIt();
    }

    /**
     * 乐园
     */

    public void  game(){



    }

}
