package com.example.im6.controller.activity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.im6.R;
import com.example.im6.controller.fragment.ChatFragment;
import com.example.im6.controller.fragment.ContactListFragment;
import com.example.im6.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();
        initListener();
    }

    private void initView() {
        rg_main = findViewById(R.id.rg_main);
    }

    private void initDate() {
        //创建三个Fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();

    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId) {
                    //会话列表页面
                    case R.id.rb_main_chat:
                        fragment = chatFragment;
                        break;
                    //联系人列表页面
                    case R.id.rb_main_contact:
                        fragment = contactListFragment;
                        break;
                    //设置页面
                    case R.id.rb_main_setting:
                        fragment = settingFragment;
                        break;
                }
                //实现fragment切换的方法，选择界面
                switchFragment(fragment);
            }
        });
        //默认选择会话列表页面
        rg_main.check(R.id.rb_main_chat);
    }

    //实现fragment切换的方法
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务替换fragment
        fragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit();
    }

}
