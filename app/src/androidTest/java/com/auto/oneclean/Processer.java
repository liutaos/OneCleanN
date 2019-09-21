package com.auto.oneclean;

import android.content.Context;
import android.content.Intent;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class Processer {

    private static final int LAUNCH_TIMEOUT = 3000;
    private long timeout=3000l;
    String packageName;
    UiDevice mDevice;
    public Processer (){

    }
    public Processer(UiDevice device, String pkgName){
        mDevice=device;
        packageName=pkgName;
    }
    private UiObject2 getObjById(String id, float sencend){
        return mDevice.wait(Until.findObject(By.res(id)), sencend > 0 ? (long) (sencend * 1000) : 200);
    }
    private UiObject2 getObjByText(String text, float sencend){
        return mDevice.wait(Until.findObject(By.text(text)), sencend > 0 ? (long) (sencend * 1000) : 200);
    }

    //点击
    public void click(String id) throws Exception{
        clickById(id,0);
    }

    //延迟点击
    public void clickById(String id, float sencend) throws Exception{
        if(exitObjById(id,sencend)){
            getObjById(id,0).click();
        }
//        mDevice.findObject(By.res(packageName, id)).click();
    }
    public void clickByText(String text, float sencend) throws Exception{
        if(exitObjByText(text,sencend)){
            getObjByText(text,0).click();
        }
    }

    //延迟点击
    public void setTextById(String id, String text ,float sencend) throws Exception{
        if(exitObjById(id,sencend)){
            getObjById(id,0).setText(text);
        }
    }

    //判断是否存在该对象
    public boolean exitObjById(String id, float sencend) throws Exception{
        waitAMonent((int)sencend);
//        List<UiObject2> object2s = mDevice.findObjects(By.clazz("android.widget.TextView"));
//        for (UiObject2 obj:object2s) {
//            pritLog("obj.getText()----"+obj.getText());
//            pritLog("resName="+obj.getResourceName());
//            pritLog("Description="+obj.getContentDescription());
//        }
        return mDevice.hasObject(By.res(id));
    }

    //判断是否存在该对象
    public boolean exitObjByText(String text, float sencend) throws Exception{
        sleep(sencend);
        return mDevice.hasObject(By.text(text));
    }

    //延迟2秒滑动
    public void swipe(String id, Direction direction) {
        getObjById(id,3).swipe(direction,0.8f,4000);
    }

    //点击列表第几个元素
    public void clickListView(String id,int position,float sencend) {
        //点击第几个
        getObjById(id,sencend).getChildren().get(position).click();
    }

    public void sleep(float sencend){
        try {
            Thread.sleep(sencend > 0 ? (long) (sencend * 1000) : 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitAMonent(int sencend){
        pritLog("waitAMonent");
        mDevice.waitForWindowUpdate(packageName,sencend * 1000);
    }

    public void startApp(){
        // Wait for launcher
        // Start from the home screen
        mDevice.pressHome();
        pritLog("按Home键");
        pritLog("启动App");
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context appContext = androidx.test.InstrumentationRegistry.getTargetContext();
        final Intent intent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appContext.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)),LAUNCH_TIMEOUT);
    }


    public void pritLog(String info){
        System.out.println(info);
    }
}
