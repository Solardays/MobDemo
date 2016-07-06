package com.jc.mobdemo.sms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jc.mobdemo.R;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class SendSmsActivity extends AppCompatActivity {
    private static final  String TAG = "SendSmsActivity";
    private EditText etPhone, etVerityCode;
    private String phoneNum;
    private EventHandler eventHandler;
    private String country = "86";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etVerityCode = (EditText) findViewById(R.id.et_verity_code);

        SMSSDK.initSDK(this, "143608699cfec", "5ffc563abc95ccfda164d97e8bcb1403");
        Log.i(TAG, "onCreate--------------------");
        eventHandler = new EventHandler() {
            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
                Log.i(TAG, "beforeEvent--------------------");
            }

            @Override
            public void onRegister() {
                super.onRegister();
                Log.i(TAG, "onRegister--------------------");
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
                Log.i(TAG, "onUnregister--------------------");
            }

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    Log.i(TAG, "回调完成\n--------------------");
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Log.i(TAG, "提交验证码成功---------");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.i(TAG, "获取验证码成功---------");
                      /*  HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country = (String) phoneMap.get("country");
                        String phone = (String) phoneMap.get("phone");

                        // 提交用户信息
//                    registerUser(country, phone);
                        Log.i(TAG, country + " : " + phone);*/
                        Log.i(TAG, "--------------------");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        Log.i(TAG, "支持发送验证码的国家\n--------------------");
                        ArrayList<HashMap<String, Object>> countrys = (ArrayList<HashMap<String, Object>>) data;
                        for (HashMap<String, Object> map : countrys) {
                            for (String key : map.keySet()) {
                                Log.i(TAG, "key:" + key + " value:" + map.get(key));
                            }
                        }
                        Log.i(TAG, "--------------------");
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume--------------------");
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eventHandler);
        Log.i(TAG, "onPause--------------------");
    }

    public void smsSend(View v) {
        phoneNum = etPhone.getText().toString();
        HashMap<Character, ArrayList<String[]>> groupedCountryList = SMSSDK.getGroupedCountryList();
        String[] countryByMCC = SMSSDK.getCountryByMCC("46001");
        String[] country = SMSSDK.getCountry("42");
//        SMSSDK.getSupportedCountries();
//        HashMap<Character, ArrayList<String[]>> groupedCountryList = SMSSDK.getGroupedCountryList();
//        for (Character key : groupedCountryList.keySet()) {
//            Log.i(TAG, "key:" + key + " value:" + groupedCountryList.get(key));
//            for (String[] str:groupedCountryList.get(key)){
//                Log.i(TAG, " value:" + str);
//            }
//        }

        if (!TextUtils.isEmpty(phoneNum)) {
            SMSSDK.getVerificationCode(country[1], phoneNum, new OnSendMessageHandler(){
                @Override
                public boolean onSendMessage(String s, String s1) {
                    Log.i(TAG, " value:" + s+"  "+ s1);
                    return false;
                }
            });
//            SMSSDK.getVoiceVerifyCode(this.country, phoneNum);
        }

    }

    public void smsVerity(View v) {
        String code = etVerityCode.getText().toString();
        if (!TextUtils.isEmpty(code)) {
            //验证
            SMSSDK.submitVerificationCode(country, phoneNum, code);
        }
    }


}
