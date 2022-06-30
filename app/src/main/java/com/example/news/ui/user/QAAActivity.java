package com.example.news.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.news.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QAAActivity extends Activity {
    private Button BtnSend;
    private EditText InputBox;
    private List<Message> mData;
    private ChatAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qaa);
        final ListView mListView=(ListView)findViewById(R.id.MainList);
        mData=LoadData();
        mAdapter=new ChatAdapter(this, mData   );
        mListView.setAdapter(mAdapter);
        mListView.smoothScrollToPositionFromTop(mData.size(), 0);

        InputBox=(EditText)findViewById(R.id.InputBox);
        InputBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        InputBox.setGravity(Gravity.TOP);
        InputBox.setSingleLine(false);
        InputBox.setHorizontallyScrolling(false);

        BtnSend=(Button)findViewById(R.id.BtnSend);
        BtnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(InputBox.getText().toString()!="")
                {
                    //获取时间
                    Calendar c=Calendar.getInstance();
                    StringBuilder mBuilder=new StringBuilder();
                    mBuilder.append(Integer.toString(c.get(Calendar.YEAR))+"年");
                    mBuilder.append(Integer.toString(c.get(Calendar.MONTH))+"月");
                    mBuilder.append(Integer.toString(c.get(Calendar.DATE))+"日");
                    mBuilder.append(Integer.toString(c.get(Calendar.HOUR_OF_DAY))+":");
                    mBuilder.append(Integer.toString(c.get(Calendar.MINUTE)));
                    //构造时间消息
                    Message Message = new Message(com.example.news.ui.user.Message.MessageType_Time, mBuilder.toString());
                    mData.add(Message);
                    //构造输入消息
                    String question = InputBox.getText().toString();
                    Message=new Message(Message.MessageType_To,question);
                    mData.add(Message);
                    //构造返回消息，如果这里加入网络的功能，那么这里将变成一个网络机器人
                    String answer;
                    switch (question){
                        case "1":
                            answer = "进入【我的-个人资料编辑】页面，点击相应的信息可进行更改，记得点保存哟！";
                            break;
                        case "2":
                            answer = "进入【实时位置】页面，点击右下角红色警报图标，即可进入危险区域设置界面。";
                            break;
                        default:
                            answer = "好的，已收到您的疑问或改进建议。我们将尽快处理或完善，谢谢您的宝贵意见~ ";
                    }
//
                    Message=new Message(Message.MessageType_From,answer);
                    mData.add(Message);
                    //更新数据
                    mAdapter.Refresh();
                }
                //清空输入框
                InputBox.setText("");
                //关闭输入法
                imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_IMPLICIT_ONLY);
                //滚动列表到当前消息
                mListView.smoothScrollToPositionFromTop(mData.size(), 0);
            }
        });
    }

    private List<Message> LoadData()
    {
        List<Message> Messages=new ArrayList<Message>();

        Message Message=new Message(com.example.news.ui.user.Message.MessageType_Time,"信息收集客服【小颐】为您服务。");
        Messages.add(Message);

        Message=new Message(Message.MessageType_From,"你好呀~我是小颐。" +
                "在这里，您可以向开发者提出未得到解决的关于本应用的疑问或是改进建议。" );
        Messages.add(Message);
        return Messages;
    }


    public void back_to_mine(View view) {
        QAAActivity.this.finish();
    }
}
