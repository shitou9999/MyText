package com.shitou.mya.HtmlTextView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shitou.mya.R;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class HtmlTextViewActivity extends AppCompatActivity {
    String html = "下面是图片了哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈" +
            "哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊 " +"<img src='http://www.qqpk.cn/Article/UploadFiles/201411/20141116135722282.jpg'/>" +
            "这也是图片" +"<img src='http://h.hiphotos.baidu.com/image/pic/item/d000baa1cd11728b2027e428cafcc3cec3fd2cb5.jpg'/>" +
            "还有一张"+  "<img src='http://img.61gequ.com/allimg/2011-4/201142614314278502.jpg' />"
            +"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊"+"<img src='http://baike.soso.com/p/20090711/20090711100323-24213954.jpg' />";
    HtmlTextView main;

//    Html.forHtml中已经将html内容中的超链接和图片转义成为 UrlSpan 和 ImageSpan，进而在 TextView 中完成显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_text_view);

        main = (HtmlTextView) this.findViewById(R.id.main_text);
        main.setHtmlFromString(html,false);
        // make links and image work
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                int what = msg.what;
                if (what == 200) {
                    MessageSpan ms = (MessageSpan) msg.obj;
                    Object[] spans = (Object[]) ms.getObj();
                    for (Object span : spans) {
                        if (span instanceof ImageSpan) {
                            Log.d(TAG, "点击了图片"+((ImageSpan) span).getSource());
                            //处理自己的逻辑
                            Toast.makeText(HtmlTextViewActivity.this, "点击了图片"+((ImageSpan) span).getSource(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
        main.setMovementMethod(ImageSpanLinkMethod.getInstance(handler, ImageSpan.class));
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HtmlTextViewActivity.this, "点击了文字", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
