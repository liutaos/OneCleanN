/*
 * *******************************************************************************
 *         文 件：HttpGet.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月25日 11:03:46
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean;


import android.util.Log;
import android.widget.TextView;

import androidx.constraintlayout.solver.GoalRow;

import com.auto.oneclean.tools.PhoneCodeBean;
import com.auto.oneclean.tools.ProjectBean;
import com.auto.oneclean.tools.Root;
import com.auto.oneclean.tools.SmsBean;
import com.auto.oneclean.tools.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpGet {
    private static final String TAG = "com.aotu.oneclean";

    private Response response;
    private TextView mTextiew;
    private static String mResponseData = "";
    private static final String URL_API ="http://47.244.25.193:9091/api";
    //private static final String URL_API = "http://39.98.47.121:9091/api";  旧地址
    private static final String URL_loginName = "wurui";
    private static final String URL_password = "wurui456";
    private static final String URL_devSecretkey = "8969CAC5DE960CA29E76C0D10CC8D092";
    private static final String URL_projectCodeOrName = "腾讯清理大师";
    private static final String URL_projectId = "9691";
    private static final int URL_msgType = 1;
    private static final String msgOpType_SJ = "sj";
    private static final String msgOpType_ZDSJH = "zdsjh";


    //private Gson gson;
    private User mUser = new User();
    private Root mRoot = new Root();
    //private PhoneCodeBean mPhoneCodeBean;
    private ProjectBean mProjectRoot;
    //private SmsBean smsBean;

    private Map<String, String> mMap = new HashMap<String, String>();


    private static String Cell_phone_number;
    private static String TOCKEN;
    private static String mProjectId;
    private static String mSms_Numeber;

    /**
     * 请求数据
     */
    public void getData() {

        String SINGN_URL = URL_API + "/login/v1?" + "loginName=" + URL_loginName + "&password="
                + URL_password + "&devSecretkey=" + URL_devSecretkey;

        // -----------------登录平台
        httpRequest(SINGN_URL);
        sloveJSON(mResponseData);
        Log.i(TAG, "        =========== doInBackground: ==========" + mResponseData);
        // 刷新token
        if (mRoot.getCode() == "-1") {
            String TOUCKEN_URL = URL_API + "/refreshToken/v1?token=" + TOCKEN;
            httpRequest(TOUCKEN_URL);
            tockenUP(mResponseData);
        }
        Log.i(TAG, "        =========== doInBackground: =====2222=====" + mResponseData);
        //-----------------获取项目ID码
        String PROJECT_GET_URL = URL_API + "/getUserProject/v1?" + "token=" + TOCKEN;
        //查找项目
        //String PROJECT_ID_URL = URL_API + "/findProject/v1?" + "token=" + TOCKEN + "&projectCodeOrName=" + URL_projectId;
        httpRequest(PROJECT_GET_URL);
        projectJSON(mResponseData);
    }

    /**
     * 获取随机手机号
     */
    public String getCellNumberSJ() {
        //------------------取   号
        //http://39.98.47.121:9091/api/getPhoneNo/v1?token=xxx&projectId=yyy&msgOpType=sj&msgType=1
        String PROJECT_GET_PHONE_URL = URL_API + "/getPhoneNo/v1?token=" + TOCKEN + "&projectId="
                + mProjectId + "&msgOpType=" + msgOpType_SJ + "&msgType=" + URL_msgType;
        httpRequest(PROJECT_GET_PHONE_URL);
        phoneJSON(mResponseData);
        return Cell_phone_number;
    }

    /**
     * 指定手机号
     */
    public String getCellNumberZJSJH(String cellPhoneNumber) {
        //http://39.98.47.121:9091/api/getPhoneNo/v1?token=xxx&projectId=yyy&msgOpType=sj&msgType=1&phoneNo=111111111

        String PROJECT_CELL_NUMBER_URL = URL_API + "/getPhoneNo/v1?token=" + TOCKEN + "&projectId=" + mProjectId +
                "&msgOpType=" + msgOpType_ZDSJH + "&msgType=1" + "&phoneNo=" + cellPhoneNumber;
        //String PROJECT_CELL_NUMBER_URL = URL_API + "/getPhoneNo/v1?token=" + TOCKEN + "&projectId="+ mProjectId + "&msgOpType=" + msgOpType_SJ + "&msgType=1" + "&phoneNo=" + cellPhoneNumber;
        httpRequest(PROJECT_CELL_NUMBER_URL);
        phoneJSON(mResponseData);
        Cell_phone_number = cellPhoneNumber;
        return Cell_phone_number;
    }

    String old_sms;

    /**
     * 请求短信信息
     */
    public String getSmsMsg(String CellNumber) {
        mSms_Numeber = null;
        //Cell_phone_number = CellNumber;
        Processer processer = new Processer();
        String PROJECT_GET_SMS_URL = URL_API + "/getSms/v1?" + "token=" + TOCKEN + "&projectId="
                + mProjectId + "&phoneNo=" + CellNumber + "&msgType=" + URL_msgType;
        Log.e(TAG, "getSmsMsg:PROJECT_GET_SMS_URL " + PROJECT_GET_SMS_URL);
        //------------获取短信信息
        httpRequest(PROJECT_GET_SMS_URL);
        mSms_Numeber = msmJSON(mResponseData);
        return mSms_Numeber;
    }

    /**
     * 获取 token
     */
    public void getTocken() {
        Log.e(TAG, "==============           getTocken:           " + TOCKEN);
        httpRequest(URL_API + "/refreshToken/v1?token=" + TOCKEN);
        tockenUP(mResponseData);
    }

    /**
     * 请求登录
     */
    private void sloveJSON(String responseData) {

        Gson gson = new Gson();
        Log.i(TAG, "  ==========   sloveJSON:========== " + responseData);
        mRoot = gson.fromJson(responseData, Root.class);
        //TOCKEN = mRoot.getUser().getToken();
        String msg = mRoot.getMsg();
        //---------------------------token  保存 -------------
        if (mRoot.getCode().equals("0")) {
            TOCKEN = mRoot.getUser().getToken();
            mMap.put("token", TOCKEN);
        }
        //"msg":"token验证：token信息有误，请重新登录获取"
        if (mRoot.getCode() == "-1") {
            getTocken();
        }
    }

    /**
     * 退出登录
     */

    public void singOut() {
        httpRequest(URL_API+"/releasePhoneNo/v1?token=" + TOCKEN + "&phoneNo=" + Cell_phone_number);
        System.out.println("++++++++++++++++ 释放了++++++++++++++++++" + mResponseData);
        httpRequest(URL_API+"/loginOut/v1?token=" + TOCKEN);
        System.out.println("++++++++++++++++ 退出登录了++++++++++++++++++" + mResponseData);
    }

    /**
     * 获取项目ID 32 位码
     * 项目 32 位 ID码  85209ca823c642adb44374be7fe7930d
     */
    private void projectJSON(String jsonStr) {
        Log.i(TAG, "projectJSON:      jsonStr    " + jsonStr);
        Gson gson = new Gson();
        Type type = new TypeToken<ProjectBean>() {
        }.getType();
        mProjectRoot = gson.fromJson(jsonStr, type);
        System.out.println("==========mProjectRoot====msg===" + mProjectRoot.msg);
        System.out.println("==========mProjectRoot====projects===" + mProjectRoot.projects);
        if (mProjectRoot.projects != null) {
            for (int i = 0; i < mProjectRoot.projects.size(); i++) {
                mProjectId = mProjectRoot.projects.get(i).projectId;
                Log.e(TAG, "projectJSON: id            " + mProjectId);
                mMap.put("projectId", mProjectId);
            }
        }
        //"msg":"token验证：token信息有误，请重新登录获取"
        if (mRoot.getCode() == "-1") {
            getTocken();
        }
    }


    /**
     * 获取手机号
     */
    private void phoneJSON(String jsonStr) {
        Log.i(TAG, "  phoneJSON:      jsonStr    " + jsonStr);
        Gson gson = new Gson();
        Type type = new TypeToken<PhoneCodeBean>() {
        }.getType();
        PhoneCodeBean mPhoneCodeBean = gson.fromJson(jsonStr, type);
        Log.e(TAG, "          phoneJSON:    " + mPhoneCodeBean.getCode() + mPhoneCodeBean.getMsg());
        if (mPhoneCodeBean.phoneInfo != null) {
            Cell_phone_number = mPhoneCodeBean.phoneInfo.getPhoneno();
            Log.e(TAG, "          phoneJSON:    " + Cell_phone_number);
            mMap.put("cellNumber", Cell_phone_number);
        }
    }

    /**
     * 获取短信验证信息
     */
    private String msmJSON(String jsonStr) {
        mSms_Numeber = null;
        Gson gson = new Gson();
        Log.i(TAG, "  msmJSON:      jsonStr    " + jsonStr);
        Type type = new TypeToken<SmsBean>() {
        }.getType();
        SmsBean smsBean = gson.fromJson(jsonStr, type);
        System.out.println("    smsBean     " + smsBean.getCode());
        if (smsBean.getCode() == "-1" && smsBean.getMsg().equals("token验证：token信息有误，请重新登录获取")) {
            reopengetData();
        }
        System.out.println("==========msmJSON===getMsg  ====" + smsBean.getMsg());
        System.out.println("==========msmJSON====msgInfo ===" + smsBean.msgInfo);
        if (smsBean.msgInfo != null) {
            String textContent = smsBean.msgInfo.getMsgTextContent();
            Log.e(TAG, "msmJSON: textContent" + textContent);
            old_sms = textContent;
            mSms_Numeber = old_sms;
            //mMap.put("textContent", old_sms);
        }
        return mSms_Numeber;
        //singOut();
    }

    public void reopengetData() {
        httpRequest(URL_API+"/loginOut/v1?token=" + TOCKEN);
        getData();
        getSmsMsg(Cell_phone_number);
    }

    /**
     * 刷新 token
     */
    private void tockenUP(String jsonStr) {
        Gson gson = new Gson();
        mRoot = gson.fromJson(jsonStr, Root.class);
        Log.e(TAG, "run: ==========get  new  tocken======" + mRoot.getUser().getToken());
        TOCKEN = mRoot.getUser().getToken();
        //---------------------------token  保存 -------------
        mMap.put("token", TOCKEN);
    }

    /**
     * http 请求
     */
    private void httpRequest(String url) {
        Log.e(TAG, "httpRequest:       TOCKEN             " + TOCKEN + "\n        " + url);
        mResponseData = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            response = call.execute();
            mResponseData = response.body().string();
            //------------------            token   读取     ---------

            for (Map.Entry<String, String> entry : mMap.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();

                if (key.equals("token")) {
                    TOCKEN = value;
                } else if (key.equals("projectId")) {
                    mProjectId = value;
                } else if (key.equals("Cellnumber")) {
                    Cell_phone_number = value;
                } else if (key.equals("textContent")) {
                    //mSms_Numeber = value;
                    Log.e(TAG, "getSmsMsg:============== " + value);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "run: ID          " + " ==========" + TOCKEN);
        }
    }

}
