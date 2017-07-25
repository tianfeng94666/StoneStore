package stone.tianfeng.com.stonestore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        String token = BaseApplication.spUtils.getString(SpUtils.key_tokenKey);


        if (!StringUtils.isEmpty(token)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BaseApplication.setToken(token);
            openActivity(MainActivity.class, null);
            finish();
            return;
        } else {
            openActivity(LoginActivity.class, null);
        }
    }

    @Override
    public void loadNetData() {

    }
}
