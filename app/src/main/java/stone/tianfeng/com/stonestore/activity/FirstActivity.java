package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
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
