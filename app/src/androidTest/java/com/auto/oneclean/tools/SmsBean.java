/*
 * *******************************************************************************
 *         文 件：SmsBean.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月21日 11:30:38
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */
package com.auto.oneclean.tools;

/**
 * Auto-generated: 2019-09-20 17:14:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SmsBean {

    private String code;
    private String msg;
    public MsgInfo msgInfo;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }


    public static class MsgInfo {

        private String phoneno;
        private String msgId;
        private String msgTextContent;
        private String msgVoiceContent;
        private String msgType;

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgTextContent(String msgTextContent) {
            this.msgTextContent = msgTextContent;
        }

        public String getMsgTextContent() {
            return msgTextContent;
        }

        public void setMsgVoiceContent(String msgVoiceContent) {
            this.msgVoiceContent = msgVoiceContent;
        }

        public String getMsgVoiceContent() {
            return msgVoiceContent;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgType() {
            return msgType;
        }

    }
}