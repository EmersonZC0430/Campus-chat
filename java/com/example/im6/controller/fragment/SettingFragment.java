package com.example.im6.controller.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.im6.R;
import com.example.im6.controller.activity.LoginActivity;
import com.example.im6.controller.activity.MainActivity;
import com.example.im6.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.adapter.EMACallback;

// SettingFragment
public class SettingFragment extends Fragment {
    private Button bt_setting_out;
    private TextView tv_setting_wxid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bt_setting_out = (Button) view.findViewById(R.id.bt_setting_out);
        tv_setting_wxid = view.findViewById(R.id.tv_setting_wxid);
    }

//Activity创建的时候执行
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //业务逻辑
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        //获取id并显示
        tv_setting_wxid.setText("账号:" + EMClient.getInstance().getCurrentUser());
        //退出登录的业务逻辑处理
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器退出登录
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //关闭数据库dbHelper
                                Model.getInstance().getDBManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 更新UI显示
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();

                                        //回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);

                                        //关闭当前设置页面
                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(getActivity(), "退出失败" + s, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });
            }
        });
    }
}
