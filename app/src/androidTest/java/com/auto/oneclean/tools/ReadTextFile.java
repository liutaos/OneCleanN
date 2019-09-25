/*
 * *******************************************************************************
 *         文 件：ReadTextFile.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月25日 11:03:46
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

package com.auto.oneclean.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ReadTextFile {

    /**
     * 逐行读取手机号码
     *
     * @param filePath
     * @return
     */
    public String read(String filePath) {
        BufferedReader reader = null;
        String firstLine;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath)));
            firstLine = reader.readLine();
            if (firstLine != "") {
                System.out.println("第一行：" + firstLine);
                reader.close();
            } else {
                System.out.println("手机号码为空");
            }
            return firstLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 删除已读取的手机的号码
     *
     * @param filePath
     */
    public void write(String filePath) {
        int lineDel = 1;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));

            StringBuffer sb = new StringBuffer(4096);
            String temp = null;
            int line = 0;
            while ((temp = br.readLine()) != null) {
                line++;
                if (line == lineDel) continue;
                sb.append(temp).append("\r\n ");
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(sb.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
