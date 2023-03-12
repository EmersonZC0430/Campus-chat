package com.example.im6.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.im6.R;
import com.example.im6.controller.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

//ChatFragment 会话列表界面
//会话列表页面
public class ChatFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();

        //跳转到会话详情页面
        setConversationListItemClickListener(conversation -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);

            //传递参数
            intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());


            if(conversation.getType() == EMConversation.EMConversationType.GroupChat){
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
            }

            startActivity(intent);
        });

        //清空集合数据
        conversationList.clear();

        //监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }
    private final EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //设置数据
            EaseUI.getInstance().getNotifier().notify(list);
            //刷新页面
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}