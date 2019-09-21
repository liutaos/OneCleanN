package com.auto.oneclean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.auto.oneclean.interfaces.InfoCallback;
import com.auto.oneclean.utils.DataCleanManager;
import com.auto.oneclean.utils.RootUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";
    private String start_tag;
    private String end_tag;
    private RadioGroup radioGroup;
    private String selectPkName;
    public final String appPackageName = "android.lite.clean";

    private InfoCallback mInfoCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.rg_pkg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                selectPkName = radioButton.getText().toString();
            }
        });
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
        findViewById(R.id.runBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runMyUiautomator(view);
            }
        });

    }

    public void initData() {

        List<ApplicationInfo> applicationInfoList = getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo info : applicationInfoList) {
            String packageName = info.packageName;
            start_tag = "com.auto.";
            end_tag = ".test";
            if (packageName.startsWith(start_tag) && packageName.endsWith(end_tag)) {
                String testName = packageName.replace(start_tag, "").replace(end_tag, "");
                Log.e(TAG, "initView: packageName=" + testName);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(testName);
                radioGroup.addView(radioButton);
            }
        }
    }


    /**
     * 点击按钮对应的方法
     *
     * @param v
     */
    public void runMyUiautomator(View v) {
        if (!TextUtils.isEmpty(selectPkName)) {

            String packgeName = start_tag + selectPkName;
            Log.i(TAG, "runMyUiautomator: ");
            new UiautomatorThread(packgeName, "ExampleInstrumentedTest", "useAppContext").start();
        } else {
            Toast.makeText(this, "请选择一个应用", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearData() {
        new TaskClear().execute();

    }


    class TaskClear extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "正在清除清理大师数据", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(MainActivity.this, "清理完成！", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

             CMDUtils.runCMD(" pm clear " + appPackageName, true, true);
            return "  ";
        }
    }

}
