package com.jc.mobdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jc.mobdemo.login.LoginDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MainActivity extends AppCompatActivity implements PlatformActionListener {

    private LoginDialog loginDialog;
    private Platform platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    public void smsVerity(View v) {
        startAct(this, SmsVerityActivity.class);
    }

    public void share(View v) {
        startAct(this, ShareActivity.class);
    }

    public void startAct(Context context, Class clazz) {
        context.startActivity(new Intent(this, clazz));

    }

    public void oauthLogin(View v) {
        loginDialog = new LoginDialog(this);
        loginDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });
        loginDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                if (item.get("ItemText").equals("微博")) {
                    platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                } else if (item.get("ItemText").equals("微信")) {
                    platform = ShareSDK.getPlatform(Wechat.NAME);
                } else if (item.get("ItemText").equals("QQ")) {
                    platform = ShareSDK.getPlatform(QQ.NAME);

                }
                platform.setPlatformActionListener(MainActivity.this);
                platform.SSOSetting(false);  //设置false表示使用SSO授权方式
                if (platform.isValid()){
                    platform.removeAccount();
                }

                platform.authorize();
                platform.removeAccount();
                //移除授权
//                platform.removeAccount(true);
                loginDialog.dismiss();
            }
        });
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博授权成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信授权成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "QQ授权成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "授权失败啊" + msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }

            String accessToken = platform.getDb().getToken(); // 获取授权token
            String openId = platform.getDb().getUserId(); // 获取用户在此平台的ID
            String nickname = platform.getDb().get("nickname"); // 获取用户昵称
            Toast.makeText(getApplicationContext(), "accessToken:"+accessToken+"\nopenId"+openId+"\nnickname"+nickname, Toast.LENGTH_LONG).show();
        }

    };

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (platform.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(3);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 5;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(4);
    }
}
