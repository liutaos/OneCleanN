package com.auto.oneclean;


import android.util.Log;
import android.widget.TextView;

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

    private static final String URL_API = "http://39.98.47.121:9091/api ";
    private static final String URL_loginName = "wurui";
    private static final String URL_password = "wurui456";
    private static final String URL_devSecretkey = "8969CAC5DE960CA29E76C0D10CC8D092";
    private static final String URL_projectCodeOrName = "腾讯清理大师";
    private static final String URL_projectId = "9691";
    private static final int URL_msgType = 1;
    private static final String msgOpType_SJ = "sj";
    private static final String msgOpType_ZDSJH = "zdsjh";


    private Gson gson;
    private User mUser = new User();
    private Root mRoot = new Root();
    private PhoneCodeBean mPhoneCodeBean;
    private ProjectBean mProjectRoot;
    private SmsBean smsBean;

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
        httpRequest(SINGN_URL, 1);
        Log.i(TAG, "        =========== doInBackground: ==========" + mResponseData);
        // 刷新token
        if (mRoot.getCode() == "-1") {
            String TOUCKEN_URL = URL_API + "refreshToken/v1?token=" + TOCKEN;
            httpRequest(TOUCKEN_URL, 2);
        }
        Log.i(TAG, "        =========== doInBackground: =====2222=====" + mResponseData);

        //-----------------获取项目ID码
        String PROJECT_GET_URL = URL_API + "/getUserProject/v1?" + "token=" + TOCKEN;
        //查找项目
        //String PROJECT_ID_URL = URL_API + "/findProject/v1?" + "token=" + TOCKEN + "&projectCodeOrName=" + URL_projectId;
        httpRequest(PROJECT_GET_URL, 3);


    }

    /**
     * 获取随机手机号
     */
    public String getCellNumberSJ() {
        //------------------取   号
        //http://39.98.47.121:9091/api/getPhoneNo/v1?token=xxx&projectId=yyy&msgOpType=sj&msgType=1
        String PROJECT_GET_PHONE_URL = URL_API + "/getPhoneNo/v1?token=" + TOCKEN + "&projectId="
                + mProjectId + "&msgOpType=" + msgOpType_SJ + "&msgType=" + URL_msgType;
        httpRequest(PROJECT_GET_PHONE_URL, 4);

        return Cell_phone_number;
    }

    /**
     * 指定手机号
     */
    public String getCellNumberZJSJH(String cellPhoneNumber) {

        String PROJECT_CELL_NUMBER_URL = URL_API + "/getPhoneNo/v1?token=" + TOCKEN + "&projectId="
                + mProjectId + "&msgOpType=" + msgOpType_ZDSJH + "&msgType=1" + "&phoneNo=" + cellPhoneNumber;

        httpRequest(PROJECT_CELL_NUMBER_URL, 5);
        Cell_phone_number = cellPhoneNumber;
        return Cell_phone_number;
    }

    /**
     * 请求短信信息
     */
    public String getSmsMsg(String CellNumber) {
        Processer processer = new Processer();
        String PROJECT_GET_SMS_URL = URL_API + "/getSms/v1?" + "token=" + TOCKEN + "&projectId="
                + mProjectId + "&phoneNo=" + CellNumber + "&msgType=" + URL_msgType;
        Log.e(TAG, "getSmsMsg: " + PROJECT_GET_SMS_URL);
        //------------获取短信信息
        httpRequest(PROJECT_GET_SMS_URL, 6);
        return mSms_Numeber;
    }

    /**
     * 获取 token
     */
    private void getTocken() {
        Log.e(TAG, "==============           getTocken:           " + TOCKEN);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(URL_API + "refreshToken/v1?token=" + TOCKEN)
                .build();
        final Call call = okHttpClient.newCall(request);

        try {
            response = call.execute();
            mResponseData = response.body().string();
            Log.e(TAG, "run: ===========http Get getTocken ==============" + mResponseData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求登录
     */
    private void sloveJSON(String responseData) {

        gson = new Gson();
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
     * 获取项目ID 32 位码
     * 项目 32 位 ID码  85209ca823c642adb44374be7fe7930d
     */
    private void projectJSON(String jsonStr) {
        Log.i(TAG, "projectJSON:      jsonStr    " + jsonStr);
        gson = new Gson();
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
        gson = new Gson();
        Type type = new TypeToken<PhoneCodeBean>() {
        }.getType();
        mPhoneCodeBean = gson.fromJson(jsonStr, type);
        Log.e(TAG, "          phoneJSON:    " + mPhoneCodeBean.getCode() + mPhoneCodeBean.getMsg());
        if (mPhoneCodeBean.phoneInfo != null) {
            Cell_phone_number = mPhoneCodeBean.phoneInfo.getPhoneno();
            mMap.put("cellNumber", Cell_phone_number);
        }
    }

    /**
     * 获取短信验证信息
     */
    private void msmJSON(String jsonStr) {
        Log.i(TAG, "  msmJSON:      jsonStr    " + jsonStr);
        gson = new Gson();
        Type type = new TypeToken<SmsBean>() {
        }.getType();
        smsBean = gson.fromJson(jsonStr, type);
        System.out.println("==========msmJSON===getMsg  ====" + smsBean.getMsg());
        System.out.println("==========msmJSON====msgInfo ===" + smsBean.msgInfo);
        if (smsBean.msgInfo != null) {
            String textContent = smsBean.msgInfo.getMsgTextContent();
            Log.e(TAG, "msmJSON: textContent" + textContent);
            mSms_Numeber = textContent;
        }
    }

    /**
     * 刷新 token
     */
    private void tockenUP(String jsonStr) {
        Gson gson = new Gson();
        mRoot = (Root) gson.fromJson(jsonStr, Root.class);
        Log.e(TAG, "run: ==========get  new  tocken======" + mRoot.getUser().getToken());
        TOCKEN = mRoot.getUser().getToken();
        //---------------------------token  保存 -------------
        mMap.put("token", TOCKEN);
    }

    /**
     * http 请求
     */
    private void httpRequest(String url, final int ID) {
        Log.e(TAG, "httpRequest:       TOCKEN             " + TOCKEN + "\n        " + ID + url);

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        try {
            response = call.execute();
            mResponseData = response.body().string();
            Log.e(TAG, "run: ID          " + ID);
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
                    mSms_Numeber = value;
                }

            }

            switch (ID) {
                case 1://登录验证获取token
                    sloveJSON(mResponseData);
                case 2://刷新token
                    tockenUP(mResponseData);
                case 3://获取项目32位ID码
                    projectJSON(mResponseData);
                case 4://随机取号
                    phoneJSON(mResponseData);
                    //case 5://指定号码
                    //phoneJSON(mResponseData);
                case 6://获取短信验证信息
                    msmJSON(mResponseData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "run: ID          " + ID + " ==========" + TOCKEN);
        }
    }

}
