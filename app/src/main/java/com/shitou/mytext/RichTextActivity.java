package com.shitou.mytext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class RichTextActivity extends Activity implements OnClickListener {
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 100;
    private final int REQUEST_CODE_PICK_IMAGE = 200;
    private final String TAG = "RichTextActivity";
    private Context context;
    private LinearLayout line_rootView, line_addImg;
    private InterceptLinearLayout line_intercept;
    private RichTextEditor richText;
    private EditText et_name;
    private TextView tv_back, tv_title, tv_ok;
    private boolean isKeyBoardUp, isEditTouch;// 判断软键盘的显示与隐藏
    private File mCameraImageFile;// 照相机拍照得到的图片
    private FileUtils mFileUtils;
    private String ROLE = "add";// 当前页面是新增还是查看详情 add/modify

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.richtext);
        context = this;
        init();
    }

    private void init() {
        if (getIntent() != null)
            ROLE = getIntent().getStringExtra("role");

        mFileUtils = new FileUtils(context);

        line_addImg = (LinearLayout) findViewById(R.id.line_addImg);
        line_rootView = (LinearLayout) findViewById(R.id.line_rootView);
        line_intercept = (InterceptLinearLayout) findViewById(R.id.line_intercept);

        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_name = (EditText) findViewById(R.id.et_name);
        richText = (RichTextEditor) findViewById(R.id.richText);
        initRichEdit();
        if ("modify".equals(ROLE)) {
            tv_ok.setText("修改");
            tv_title.setText("查看详情");
            line_intercept.setIntercept(true);
            richText.setIntercept(true);
            getData();
        } else {
            tv_ok.setText("提交");
            tv_title.setText("新增");
        }
    }

    private void initRichEdit() {
        ImageView img_addPicture, img_takePicture;
        img_addPicture = (ImageView) line_addImg
                .findViewById(R.id.img_addPicture);
        img_addPicture.setOnClickListener(this);
        img_takePicture = (ImageView) line_addImg
                .findViewById(R.id.img_takePicture);
        img_takePicture.setOnClickListener(this);

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEditTouch = false;
                    line_addImg.setVisibility(View.GONE);
                }
            }

        });
        richText.setLayoutClickListener(new RichTextEditor.LayoutClickListener() {
            @Override
            public void layoutClick() {
                isEditTouch = true;
                line_addImg.setVisibility(View.VISIBLE);
            }
        });

        line_rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = line_rootView.getRootView()
                                .getHeight() - line_rootView.getHeight();
                        if (isEditTouch) {
                            if (heightDiff > 500) {// 大小超过500时，一般为显示虚拟键盘事件,此判断条件不唯一
                                isKeyBoardUp = true;
                                line_addImg.setVisibility(View.VISIBLE);
                            } else {
                                if (isKeyBoardUp) {
                                    isKeyBoardUp = false;
                                    isEditTouch = false;
                                    line_addImg.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
    }

    private void getData() {
        et_name.setText("模拟几条数据");

        richText.insertText("第一行");
        richText.insertText("接下来是张图片-王宝强");
        richText.insertImageByURL("http://baike.soso.com/p/20090711/20090711100323-24213954.jpg");
        richText.insertText("下面是一副眼镜");
        richText.insertImageByURL("http://img4.3lian.com/sucai/img6/230/29.jpg");
        richText.insertImageByURL("http://pic9.nipic.com/20100812/3289547_144304019987_2.jpg");
        richText.insertText("上面是一个树妖");
        richText.insertText("最后一行");
    }

    private void openCamera() {
        try {
            File PHOTO_DIR = new File(mFileUtils.getStorageDirectory());
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录

            mCameraImageFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCameraImageFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    private Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy_MM_dd_HH_mm_ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            richText.insertImage(mFileUtils.getFilePathFromUri(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA
                && resultCode == Activity.RESULT_OK) {
            richText.insertImage(mCameraImageFile.getAbsolutePath());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFileUtils.deleteRichTextImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if ("修改".equals(tv_ok.getText())) {
                    tv_ok.setText("提交");
                    line_intercept.setIntercept(false);
                    richText.setIntercept(false);
                } else {
                    Log.i(TAG, "---richtext-data:" + richText.getRichEditData());
                    Toast.makeText(context, "信息已打印,请到控制台查看", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_back:
                finish();
                break;

            case R.id.img_addPicture:
                // 打开系统相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.img_takePicture:
                // 打开相机
                openCamera();
                break;

        }
    }

}
