/*
 * *******************************************************************************
 *         文 件：MainActivity.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月25日 11:03:46
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.oneclean.interfaces.InfoCallback;
import com.auto.oneclean.utils.CopyFile;
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
    public String inPut;
    private final String SDCARD = "/storage/emulated/0/Pictures/";
    private final String TMP = "/storage/emulated/0/tmp_number.txt";

    private InfoCallback mInfoCallback;
    SharedPreferences userSettings;
    TextView oldFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userSettings = getSharedPreferences("old_file", 0);
        oldFile = findViewById(R.id.old_file);
        oldFile.setText("上次的文件名：" + userSettings.getString("olde_file", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText inputs = findViewById(R.id.input_phone_number);

        inputs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inPut = s.toString();
                SharedPreferences.Editor editor = userSettings.edit();
                editor.putString("olde_file", inPut);
                editor.commit();
            }
        });

        findViewById(R.id.runBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                oldFile.setText("上次的文件名：" + userSettings.getString("olde_file", ""));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CopyFile copy = new CopyFile();
                        copy.copyFile(SDCARD + inPut + ".txt", TMP);
                    }
                }).start();
                new UiautomatorThread("com.auto.oneclean", "ExampleInstrumentedTest", "useAppContext").start();
                Toast.makeText(getApplicationContext(), "运行任务中。。。。。", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
