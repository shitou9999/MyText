package com.shitou.mya.HtmlTextView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 当前类注释: UrlImageGetter 加载网络图片
 */
public class UrlImageGetter implements Html.ImageGetter {

    Context context;
    TextView container;
    int width ;

    public UrlImageGetter(TextView t, Context context) {
        this.context = context;
        this.container = t;
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

//    成功的时候，我们把BitmapDrawable的bitmap赋上我们异步得到的Bitmap对象在重新绘制，
//    就可以显示出图片,由于图片可能没有固定的尺寸，为了美观我们同意把图片的宽度设置成和手机屏幕的宽度一致
    //异步加载显示图片的流程是每收到一个异步完成就直接给textview再赋一遍string全值
    @Override
    public Drawable getDrawable(String source) {  //getDrawable中的source就是 img标签里src的值也就是图片的路径
        final UrlDrawable urlDrawable = new UrlDrawable();
        //其实是下载一个图片罢了
        ImageLoader.getInstance().loadImage(source, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //下载完成
                float scaleWidth = ((float) width)/loadedImage.getWidth();//屏幕的宽度/图片的宽度=缩放比
                Matrix matrix = new Matrix(); // 取得想要缩放的matrix参数

                matrix.postScale(scaleWidth, scaleWidth);//执行缩放
                loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
                urlDrawable.bitmap = loadedImage;
                urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());//设置绘制图片的大小
//invalidate()是用来刷新View的，必须是在UI线程中进行工作。比如在修改某个view的显示时，调用invalidate()才能看到重新绘制的界面
                container.invalidate();
                container.setText(container.getText()); // 解决图文重叠
            }
        });
        return urlDrawable;
    }

//    当没有异步加载的时候用来getDrawable的返回
    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;
        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}
