package com.facebook.customprogressbar;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.customprogressbar.view.ProgressButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ProgressButton.OnProgressButtonClickListener{

    private LinearLayout ll1, ll2, ll3;
    private TranslateAnimation mShowAction, mHiddenAction;
    Drawable drawable;
    private static final int DOWNLOAD = 1001;
    private static final int DOWNLOAD_END = 1002;
    public  boolean  isDownload=true;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD:
                    mProBtn.setProgress(msg.arg1);
                    mProBtn.setText((String) msg.obj);
                    break;
                case DOWNLOAD_END:
                    mProBtn.setProgress(msg.arg1 - 1);
                    mProBtn.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
        };
    };
    private ProgressButton mProBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getAndSaveScreenImage();
//        initView();
        mProBtn = (ProgressButton) findViewById(R.id.mProBtn);
        mProBtn.setMax(100);
        mProBtn.setText("正在下载");
        mProBtn.setProgress(0);
        mProBtn.setTextsize(40f);
        mProBtn.setOnProgressButtonClickListener(this);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            /***
             * 每隔一秒向UI传递消息
             */
            int i = 0;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (i++ < 100) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.obj = "正在下载";
                    msg.arg1 = i;
                    msg.what = DOWNLOAD;
                    handler.sendMessage(msg);
                }
                Message msg = Message.obtain();
                msg.what = DOWNLOAD_END;
                msg.arg1 = i;
                msg.obj = "下载完成";
                handler.sendMessage(msg);
            }
        });
        Math.round(9);
    }

//    private void initView() {
//        ll1 = (LinearLayout) findViewById(R.id.ll1);
//        ll2 = (LinearLayout) findViewById(R.id.ll2);
//        ll3 = (LinearLayout) findViewById(R.id.ll3);
//        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(1000);
//        mShowAction.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                 ll2.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                -1.0f);
//        mHiddenAction.setDuration(1000);
//        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                   ll2.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        ll1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /***
//                 * ll2向下移动动画
//                 */
//                ll3.startAnimation(mShowAction);
//            }
//        });
//        ll3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /***
//                 * ll向上移动动画
//                 //                 */
//                ll3.startAnimation(mHiddenAction);
//            }
//        });
//        //通过加载XML动画设置文件来创建一个Animation对象；
////        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_right);
////        LayoutAnimationController controller = new LayoutAnimationController(animation);
////        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
////        controller.setDelay(0.3f);
////        ll1.setLayoutAnimation(controller);
////        ll1.startLayoutAnimation();
//        /***
//         * 控件显示与隐藏动画
//         */
//    }

    /****
     * 截屏代码
     */
    private void getAndSaveScreenImage() {
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //获取屏幕
        View decoreview = this.getWindow().getDecorView();
        decoreview.setDrawingCacheEnabled(true);
        bitmap = decoreview.getDrawingCache();
        String screenImgaePath = getSdCardPath() + File.separator + "screenimage/";
        File screenImageFile = new File(screenImgaePath);
        String filePath = screenImgaePath + File.separator + "/Scree_1.png";
        File fileScreen = new File(filePath);
        if (!fileScreen.exists()) {
            fileScreen.mkdirs();
        } else {
            try {
                fileScreen.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入文件
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileScreen);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);//输出图片
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            ;
        }
    }

    /****
     * 获得Sd卡存储路径
     *
     * @return
     */
    private String getSdCardPath() {
        File screenImgFile = null;
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            screenImgFile = Environment.getExternalStorageDirectory();
        } else {
            screenImgFile = new File(getFilesDir().getAbsolutePath() + "screenImagePath/");
        }
        return screenImgFile.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressButton(View v) {
        Toast.makeText(MainActivity.this," 进度条按钮被点击",Toast.LENGTH_SHORT).show();
    }
}
