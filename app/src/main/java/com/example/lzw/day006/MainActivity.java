package com.example.lzw.day006;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.android.PermissionUtils;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_CODE = 1000;
    private TextView tv_text;
    private ImageView img;
    private EditText et_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text=(TextView)findViewById(R.id.tv_text);
        img=(ImageView)findViewById(R.id.img);
        et_text=(EditText)findViewById(R.id.et_text);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCan();
            }
        });
        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCapture(0);
            }
        });
        //不带logo
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCapture(1);
            }
        });
    }


    //生成图片
    private void setCapture(int type) {
        String message = et_text.getText().toString().trim();
        if (TextUtils.isEmpty(message)){
            Toast.makeText(MainActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        if (type==1){
           bitmap=null;
        }
        try {
           Bitmap bitmap1= CodeCreator.createQRCode(message,200,200,bitmap);
           img.setImageBitmap(bitmap1);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
    //跳转扫一扫
    private void sCan() {
        //动态获取方法
        PermissionUtils.permission(this, new PermissionUtils.PermissionListener() {
            @Override
            public void success() {
                Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
//                ZxingConfig config = new ZxingConfig();
//                config.setShowbottomLayout(false);
//                config.setPlayBeep(true);
//                intent.putExtra(Constant.INTENT_ZXING_CONFIG,config);

                startActivityForResult(intent,RESULT_CODE);
        }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CODE&&resultCode==RESULT_OK){
            String resultData=data.getStringExtra(Constant.CODED_CONTENT);
           if (resultData.contains("http")){
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("web_url",resultData);
                startActivity(intent);
           }else {
               tv_text.setText(resultData);
           }

        }
    }
}
