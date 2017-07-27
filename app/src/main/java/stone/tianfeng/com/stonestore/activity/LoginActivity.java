package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.net.OKHttpRequestUtils;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.SpUtils;
import stone.tianfeng.com.stonestore.utils.StringUtils;
import stone.tianfeng.com.stonestore.utils.ToastManager;
import stone.tianfeng.com.stonestore.viewutils.CountTimerButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/7.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.id_btn_register)
    Button idBtnRegister;
    @Bind(R.id.id_ed_name)
    EditText idEdName;
    @Bind(R.id.id_ed_password)
    EditText idEdPassword;
    @Bind(R.id.id_ed_code)
    EditText idEdCode;
    String name, pwd, phone, code;
    CountTimerButton mCountDownTimerUtils;
    private String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_2);
        ButterKnife.bind(this);
//        isNeedUpdate();
        getBackIntent();
        initView();


    }

    private void initView() {
        String token = BaseApplication.spUtils.getString(SpUtils.key_tokenKey);
        if (!StringUtils.isEmpty(token)) {
            BaseApplication.setToken(token);
            openActivity(MainActivity.class, null);
            finish();
            return;
        } else {
            openActivity(LoginActivity.class, null);
        }
        name = BaseApplication.spUtils.getString(SpUtils.key_username);
        pwd = BaseApplication.spUtils.getString(SpUtils.key_password);
        if (!StringUtils.isEmpty(name)) {
            idEdName.setText(name);
        }
        if (!StringUtils.isEmpty(pwd)) {
            idEdPassword.setText(pwd);
        }


        idBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNetData();
            }
        });

        Button textView = (Button) findViewById(R.id.tv_get_auth_code);
        mCountDownTimerUtils = new CountTimerButton(textView, 60000, 1000);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimerUtils.start();
                getNetCode();
            }
        });
    }


    /*得到没登陆前的实例*/
    public void getBackIntent() {
        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.getExtras() != null) {
            nextActivity = (Class<?>) callingIntent.getExtras().get(JUMP_TO_ACTIVITY);
        }
    }


    @Override
    public void loadNetData() {
        name = idEdName.getText().toString();
        pwd = idEdPassword.getText().toString();
        code = idEdCode.getText().toString();
        if (StringUtils.isEmpty(name)) {
            showToastReal("用户名不能为空！");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToastReal("密码不能为空！");
            return;
        }
        if (StringUtils.isEmpty(code)) {
            showToastReal("验证码不能为空！");
            return;
        }
        baseShowWatLoading();
        // 进行登录请求
        String lgUrl = AppURL.URL_LOGIN + "userName=" + name + "&password=" + pwd + "&phoneCode=" + code;
        L.e("netLogin" + lgUrl);
        VolleyRequestUtils.getInstance().getCookieRequest(this, lgUrl, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("OKHttpRequestUtils" + result);
                baseHideWatLoading();
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    String token = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("tokenKey").getAsString();
                    L.e("成功" + token);
                    BaseApplication.spUtils.saveString(SpUtils.key_tokenKey, token);
                    BaseApplication.spUtils.saveString(SpUtils.key_username, name);
                    BaseApplication.spUtils.saveString(SpUtils.key_password, pwd);
                    BaseApplication.setToken(token);
                    openActivity(MainActivity.class, null);
                    finish();
                    return;
                    //loginSuccess();
                } else if (error == 2) {
                    L.e("重新登录");
                    // netLogin();
                } else if (error == 3) {
                    showToastReal("未审核");
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                   showToastReal("数据加载错误:"+message);
                }

            }

            @Override
            public void onFail(String fail) {
                showToastReal("数据获取失败");
                baseHideWatLoading();
            }


        });

    }


    public void loginSuccess() {
        if (nextActivity != null) {
            final Intent intent = new Intent(LoginActivity.this, nextActivity);
            //intent.putExtra(GET_TO, "");
            (new android.os.Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    //设置切换动画，从右边进入，左边退出
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }
            }, 1000);
            return;
        }
        openActivity(MainActivity.class, null);
        finish();
//        Intent intent = new Intent();
//        intent.putExtra("fragmentid", getIntent().getIntExtra("fragmentid", -1));
//        setResult(94, intent);
        //
    }

    public void getNetCode() {
        name = idEdName.getText().toString();
        pwd = idEdPassword.getText().toString();
        if (StringUtils.isEmpty(name)) {
            mCountDownTimerUtils.onFinish();
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            mCountDownTimerUtils.onFinish();
            return;
        }
        baseShowWatLoading();
        // 进行登录请求
        String lgUrl = AppURL.URL_LOGING_CODE + "userName=" + name + "&password=" + pwd;
        L.e("url=" + lgUrl);
        VolleyRequestUtils.getInstance().getCookieRequest(this, lgUrl, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("OKHttpRequestUtils" + result);
                baseHideWatLoading();
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                }
                if (error == 1) {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    ToastManager.showToastReal(message);
                    mCountDownTimerUtils.onFinish();
                }
                if (error == 2) {
                    L.e("重新登录");
                }
                if (error == 3) {
                  showToastReal("未审核");
                }
            }

            @Override
            public void onFail(String fail) {
                showToastReal("数据获取失败");
            }
        });
    }

    public void onRegister(View v) {
        openActivity(RegisterActivity.class, null);
    }

    public void onUpdatePassword(View v) {
        openActivity(FrogetPwdActivity.class, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.requestQueue.cancelAll(this);
        finish();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastManager.showToastReal("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
        }
        return true;
    }
}
