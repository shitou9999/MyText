package com.shitou.mya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private Button btn_add;
    private RelativeLayout rela_content;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RichTextActivity.class).putExtra("role", "add");
                startActivity(intent);
            }
        });

        rela_content = (RelativeLayout) findViewById(R.id.rela_content);
        rela_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RichTextActivity.class).putExtra("role", "modify");
                startActivity(intent);
            }
        });
    }
}
