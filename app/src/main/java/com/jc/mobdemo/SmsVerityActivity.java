package com.jc.mobdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jc.mobdemo.sms.SendSmsActivity;
import com.jc.mobdemo.tool.ActivityTool;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;

public class SmsVerityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verity);
        SMSSDK.initSDK(this, "143608699cfec", "5ffc563abc95ccfda164d97e8bcb1403");
    }

    public void openSmsGui(View v) {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

                    // 提交用户信息
//                    registerUser(country, phone);
                    Log.e("SMS", country + " : " + phone);

                }
            }
        });
        registerPage.show(this);
    }

    public void openContactList(View v) {
        //打开通信录好友列表页面
        ContactsPage contactsPage = new ContactsPage();
        contactsPage.show(this);
    }

    public void selfGui(View v) {
        ActivityTool.startAct(this,SendSmsActivity.class);
    }
}
