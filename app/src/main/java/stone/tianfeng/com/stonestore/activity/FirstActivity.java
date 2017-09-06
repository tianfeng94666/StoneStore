package stone.tianfeng.com.stonestore.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.utils.SpUtils;
import stone.tianfeng.com.stonestore.utils.StringUtils;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class FirstActivity extends BaseActivity {
    @Bind(R.id.iv_first)
    ImageView ivFirst;
    @Bind(R.id.tv_first)
    TextView tvFirst;
    WebView myWebView;
    @Bind(R.id.iv_first2)
    ImageView ivFirst2;
    private CountDownTimer countdowntimer;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_first);
        startAnimation();
//        init();
        ButterKnife.bind(this);
        token = BaseApplication.spUtils.getString(SpUtils.key_tokenKey);
        tvFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdowntimer.cancel();
                countdowntimer.onFinish();
            }
        });
        docountdown();

    }

    private void startAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivFirst, "alpha", 1.0f, 0.8f, 0.6f, 0.4f, 0.2f, 0.0f);
        ivFirst2 = (ImageView) findViewById(R.id.iv_first2);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivFirst2, "alpha", 0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator2);
//                set.playSequentially(alphaAnim, scaleXAnim, scaleYAnim, rotateAnim, transXAnim, transYAnim);
        set.setDuration(4000);
        set.start();

    }

    private void init() {
        String url = "http://c.eqxiu.com/s/lPiERT7d?eqrcode=1&from=timeline&isappinstalled=0";
        myWebView = (WebView) findViewById(R.id.my_webview);
        myWebView.loadUrl(url);
        //加载本地中的html
        //myWebView.loadUrl("file:///android_asset/www/test2.html");
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        myWebView.setWebViewClient(new WebViewClient());
        //得到webview设置
        WebSettings webSettings = myWebView.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //将WebAppInterface于javascript绑定
//        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webSettings.setPluginState(WebSettings.PluginState.ON);
        myWebView.setWebChromeClient(new WebChromeClient());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    //倒计时
    private void docountdown() {
        countdowntimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvFirst.setVisibility(View.VISIBLE);
                tvFirst.setText("跳过 " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (!StringUtils.isEmpty(token)) {
                    BaseApplication.setToken(token);
                    openActivity(MainActivity.class, null);
                    finish();
                } else {
                    openActivity(LoginActivity.class, null);
                }
            }
        };
        countdowntimer.start();
    }

    @Override
    public void loadNetData() {

    }
}
