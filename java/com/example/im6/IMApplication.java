package com.example.im6;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.example.im6.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class IMApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化easeui
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false); //设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);
        EaseUI.getInstance().init(this,options);

        //初始化数据模型层类
        Model.getInstance().init(this);

        //初始化全局上下文
        mContext = this;
    }

    //获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
