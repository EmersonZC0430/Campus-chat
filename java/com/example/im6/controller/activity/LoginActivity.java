package com.example.im6.controller.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.im6.R;
import com.example.im6.model.Model;
import com.example.im6.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.time.Instant;

public class LoginActivity extends Activity {
private EditText et_login_name;
private EditText et_login_pwd;
private Button bt_login_register;
private Button bt_login_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//初始化控件
        initListener(); //初始化监听
    }
//初始化控件
    private void initView(){
        //根据id获取输入的用户名,密码,注册，登录
        et_login_name = (EditText)findViewById(R.id.et_login_name);
        et_login_pwd = (EditText)findViewById(R.id.et_login_pwd);
        bt_login_register = (Button)findViewById(R.id.bt_login_register);
        bt_login_login = (Button)findViewById(R.id.bt_login_login);

    }
//初始化监听
    private void initListener() {
        //注册按钮点击事件处理
        bt_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            regist();
            }
        });
        //登录按钮点击事件处理
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            login();
            }
        });
    }
//登录的业务逻辑处理
    private void login() {
        // 1获取输入的用户名和密码
        String loginName = et_login_name.getText().toString();
        String loginPwd = et_login_pwd.getText().toString();

        // 2校验输入的用户名和密码
        if(TextUtils.isEmpty(loginName)||TextUtils.isEmpty(loginPwd)){
            Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        //3登录逻辑处理,开子线程去服务器验证
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                    //对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(loginName));
                    //保存用户账号信息到本地数据库
                        Model.getInstance().getUSerAccountDao().addAccount(new UserInfo(loginName));
                        //切换到主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                //跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                    //提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"登录失败"+s,Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

//注册的业务逻辑处理
    private void regist() {
        // 1获取输入的用户名和密码
        String registName = et_login_name.getText().toString();
        String registPwd = et_login_pwd.getText().toString();
        // 2校验输入的用户名和密码
        if(TextUtils.isEmpty(registName)||TextUtils.isEmpty(registPwd)){
            Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
         //3去服务器注册账号,开一个子线程
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //用环信底层方法创建,去环信服务器注册账号
                try {
                    EMClient.getInstance().createAccount(registName,registPwd);
                    //注册成功提醒（更新页面显示）
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册失败"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}