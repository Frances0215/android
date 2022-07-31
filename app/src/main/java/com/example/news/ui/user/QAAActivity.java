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

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QAAActivity extends AppCompatActivity {
    private Button BtnSend;
    private EditText InputBox;
    private List<Message> mData;
    private ChatAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qaa);
        this.getSupportActionBar().hide();
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
                            answer = "新闻类型又称为频道，新闻的默认分类为推荐、财经和股票，可通过语音输入”添加频道“"+
                                    "来进行添加，例如语音输入”添加娱乐“，即可添加娱乐频道,也可通过语音输入”删除频道“进行删除";
                            break;
                        case "2":
                            answer = "在新闻主页中长按左下角的”按住说话“按钮即可进行语音输入";
                            break;
                        case "3":
                            answer = "在新闻主页中下拉即可刷新";
                            break;
                        case "4":
                            answer = "听闻APP采用的是翻页式阅读方式，不是传统的下滑式设计，听完当前段落记得右滑翻页哦";
                            break;
                        case "5":
                            answer = "听闻采用的是一键登录和账号密码登录两种方式，为了保证您的账号安全，使用一键登录的用户记得及时设置密码哦";
                            break;
                        case "6":
                            answer = "新闻搜索有语音输入和键盘输入两种方式，如果选用语音输入，请长按主页左下角按钮说话，说完后松开，就会自动识别您的语音啦！" +
                                    "若采用键盘输入，请直接点击主页上方的搜索栏进行输入，然后点击搜索栏右边的按钮进行搜索";
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

        Message Message=new Message(com.example.news.ui.user.Message.MessageType_Time,"信息收集客服【小闻】为您服务。");
        Messages.add(Message);

        Message=new Message(Message.MessageType_From,"你好呀~我是小闻。" +
                "在这里，您可以向开发者提出未得到解决的关于本应用的疑问或是改进建议。" );
        Messages.add(Message);

        Message=new Message(Message.MessageType_From,"常见问题：\n" + "1、如何添加或删除新闻类型\n"+ "2、如何进行语音输入\n"+
                "3、如何刷新新闻\n"+
                "4、如何进行新闻阅读\n"+
                "5、关于登录\n"+
                "6、如何进行新闻搜索");
        Messages.add(Message);
        return Messages;
    }


    public void back_to_mine(View view) {
        QAAActivity.this.finish();
    }
}
