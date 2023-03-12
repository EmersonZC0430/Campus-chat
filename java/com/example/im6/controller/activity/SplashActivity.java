package com.example.im6.controller.activity;

import androidx.annotation.NonNull;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.im6.R;
import com.example.im6.model.Model;
import com.example.im6.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends Activity {

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //如果当前Activity已经退出，就不处理直接返回
            if (isFinishing()) {
                return;
            }
            //判断进入主页面还是登录页面函数
            toMainOrLogin();
        }
    };
    //判断进入主页面还是登录页面函数
    private void toMainOrLogin() {

        Model.getInstance().getGlobalThreadPool().execute(() -> {
            //判断当前账号是否已经登录过,EMClient环信客服端api
            if (EMClient.getInstance().isLoggedInBefore()) {//登录过
                //获取当前登录用户的登录信息
                UserInfo account = Model.getInstance().getUSerAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                if (account == null) {
                    //显示意图跳转到登录页面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //登录成功后的方法
                    Model.getInstance().loginSuccess(account);
                    //跳转到主页面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                //跳转到主页面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {//没登陆过跳转登录页面
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            //结束当前页面
            finish();
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //使用handler发送3秒钟的延时消息
        handler.sendMessageDelayed(Message.obtain(), 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}