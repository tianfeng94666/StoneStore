package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.json.AddressItmeResult;
import stone.tianfeng.com.stonestore.net.OKHttpRequestUtils;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.StringUtils;
import stone.tianfeng.com.stonestore.utils.ToastManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/28 15:40
 * @version  添加收货地址或者修改
 *
 */
public class AddAddressActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.id_rel_title)
    RelativeLayout idRelTitle;
    @Bind(R.id.id_tv1)
    TextView idTv1;
    @Bind(R.id.id_tv4)
    TextView idTv4;
    @Bind(R.id.id_tv44)
    TextView idTv44;
    @Bind(R.id.id_rl_selectcity)
    RelativeLayout idRlSelectcity;
    @Bind(R.id.id_tv6)
    TextView idTv6;
    @Bind(R.id.id_btn_save)
    Button btnSave;
    @Bind(R.id.id_tv_address)
    TextView idTvAddress;
    @Bind(R.id.id_tv_name)
    EditText idTvName;
    @Bind(R.id.id_ed_phone)
    EditText idEdPhone;
    Boolean IS_MODIFY_ADDRESS = false; //true为修改  false为添加
    String id;
    @Bind(R.id.id_ed_adress)
    EditText idEdAdress;
    public static OnRefurbishListener onRefurbishListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.update_address);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        initOnClick();
    }

    protected void initView() {
        titleText.setText(R.string.shop_address_manager);
    }

    public void getIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        id = extras.getString("ID");
        IS_MODIFY_ADDRESS = extras.getBoolean("IS_MODIFY_ADDRESS");
        if (IS_MODIFY_ADDRESS) {
            loadNetData();
        }
    }

    private void initOnClick() {
        idRlSelectcity.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void loadNetData() {

        String url = AppURL.URL_MODIFY_ADDRESS + "tokenKey=" + BaseApplication.getToken() + "&id=" + id;
        L.e("loadNetData" + ":" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    AddressItmeResult addressItme = new Gson().fromJson(result, AddressItmeResult.class);
                    if (addressItme.getData() != null) {
                        idTvName.setText(addressItme.getData().getAddress().getName());
                        idEdPhone.setText(addressItme.getData().getAddress().getPhone());
                        provinceId = addressItme.getData().getAddress().getProvince_id();
                        cityId = addressItme.getData().getAddress().getCity_id();
                        areaId = addressItme.getData().getAddress().getArea_id();
                        L.e("provinceId" + provinceId + "cityId" + cityId + "areaId" + areaId);
                        adress = addressItme.getData().getAddress().getAddr();
                        idTvAddress.setText(addressItme.getData().getAddress().getPlace());
                        idEdAdress.setText(addressItme.getData().getAddress().getAddr());
                    }

                }
                if (error == 1) {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    ToastManager.showToastReal(message);
                }
                if (error == 2) {
                    loginToServer(AddressListActivity.class);
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    ToastManager.showToastReal("数据加载错误:+"+message);
                }
            }

            @Override
            public void onFail(String fail) {
                showToastReal("数据获取失败");
            }
        });


    }


    public void onBack(View view) {
        finish();
    }

    public interface OnRefurbishListener {
        void onRefush();
    }

    public static void setOnRefurbishListener(OnRefurbishListener onRefurbishListener) {
        AddAddressActivity.onRefurbishListener = onRefurbishListener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_rl_selectcity:
                Intent intent = new Intent(AddAddressActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, 11);
                //设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case R.id.id_btn_save:
                saveNet();
                break;
        }
    }

    private void saveNet() {
        String url = null;
        adress = idEdAdress.getText().toString();
        if (StringUtils.isEmpty(adress)) {
            ToastManager.showToastReal("请填写地址");
            return;
        }
        if (StringUtils.isEmpty(cityId)) {
            ToastManager.showToastReal("请填写地区");
            return;
        }
        if (IS_MODIFY_ADDRESS) {
            url = AppURL.URL_MODIFY_ADDRESS_DO + "tokenKey=" + BaseApplication.getToken() + "&name=" + idTvName.getText().toString() +
                    "&provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId + "&phone=" + idEdPhone.getText().toString()
                    + "&addr=" + adress + "&isDefault=" + 0 + "&itemId=" + id;
        } else {
            url = AppURL.URL_ADD_ARDRESS + "tokenKey=" + BaseApplication.getToken() + "&name=" + idTvName.getText().toString() +
                    "&provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId + "&phone=" + idEdPhone.getText().toString()
                    + "&addr=" + adress + "&isDefault=" + 0;

        }
        L.e("saveNet    " + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    if (onRefurbishListener != null) {
                        onRefurbishListener.onRefush();
                    }
                    finish();
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    ToastManager.showToastReal(message);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });


    }

    String city, area, county, name;
    String provinceId, cityId, areaId, adress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("onActivityResult" + requestCode);
        if (requestCode == 11) {
            if (data == null) {
                return;
            }
            provinceId = data.getStringExtra("provinceId");
            cityId = data.getStringExtra("cityId");
            areaId = data.getStringExtra("areaId");
            city = data.getExtras().getString("city");
            area = data.getStringExtra("area");
            county = data.getStringExtra("county");
            L.e("onActivityResult" + city + "/" + area + "/" + county);
            idTvAddress.setText(city + "/" + area + "/" + county);
        }
    }

}
