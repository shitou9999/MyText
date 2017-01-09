package com.shitou.mya.HtmlTextView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

/**
 * 当前类注释: LocalImageGetter 加载本地图片显示
 */
public class LocalImageGetter implements Html.ImageGetter {

    Context context;

    public LocalImageGetter(Context context) {
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        int id = context.getResources().getIdentifier(source, "drawable", context.getPackageName());
        if (id == 0) {
            // the drawable resource wasn't found in our package, maybe it is a
            // stock android drawable?
            id = context.getResources().getIdentifier(source, "drawable", "android");
        }
        if (id == 0) {
            // prevent a crash if the resource still can't be found
//            LogUtil.e("source could not be found: " + source);
            return null;
        } else {
            Drawable d = context.getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        }
    }
}
