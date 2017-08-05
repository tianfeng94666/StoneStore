package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mob.tools.gui.PullToRequestView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.adapter.SeriesProductAdapter;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.json.ModeListResult;
import stone.tianfeng.com.stonestore.net.ImageLoadOptions;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.SpUtils;
import stone.tianfeng.com.stonestore.utils.ToastManager;
import stone.tianfeng.com.stonestore.viewutils.CustomGridView;
import stone.tianfeng.com.stonestore.viewutils.GridViewWithHeaderAndFooter;
import stone.tianfeng.com.stonestore.viewutils.PullToRefreshView;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class ProductSeriesActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnFooterRefreshListener,
        PullToRefreshView.OnHeaderRefreshListener {
    @Bind(R.id.iv_product_series)
    ImageView ivProductSeries;
    @Bind(R.id.gv_product)
    CustomGridView gvProduct;
    @Bind(R.id.id_ig_home)
    ImageView idIgHome;
    @Bind(R.id.tv_home)
    TextView idTvHome;
    @Bind(R.id.tab1_count)
    TextView tab1Count;
    @Bind(R.id.id_fl_tab1)
    FrameLayout idFlTab1;
    @Bind(R.id.id_ig_information)
    ImageView idIgInformation;
    @Bind(R.id.tv_information)
    TextView idTvInformation;
    @Bind(R.id.tab2_count)
    TextView tab2Count;
    @Bind(R.id.id_fl_tab2)
    FrameLayout idFlTab2;
    @Bind(R.id.id_ig_help)
    ImageView idIgHelp;
    @Bind(R.id.tv_help)
    TextView idTvHelp;
    @Bind(R.id.id_fl_tab3)
    LinearLayout idFlTab3;
    @Bind(R.id.iv_designer)
    ImageView ivDesigner;
    @Bind(R.id.tv_designer)
    TextView tvDesigner;
    @Bind(R.id.id_fl_tab4)
    LinearLayout idFlTab4;
    @Bind(R.id.iv_mine)
    ImageView ivMine;
    @Bind(R.id.tv_mine)
    TextView tvMine;
    @Bind(R.id.id_fl_tab5)
    LinearLayout idFlTab5;
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshView pullRefreshView;

    private String key;
    private int curpage=1;
    private ModeListResult modeListResult;
    private List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> list;
    private int isShowPrice;
    private boolean isCustomized;//是否是用户定制
    private SeriesProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_series);
        ButterKnife.bind(this);
        isCustomized = SpUtils.getInstace(this).getBoolean("isCustomized", true);
        getDate();
    }

    private void getDate() {
        key = getIntent().getStringExtra("key");
        baseShowWatLoading();
        loadNetData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isScreenChange()) {
            gvProduct.setNumColumns(4);
        } else {
            gvProduct.setNumColumns(2);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isScreenChange()) {
            gvProduct.setNumColumns(4);
        } else {
            gvProduct.setNumColumns(2);
        }
    }

    public boolean isScreenChange() {

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
        //竖屏
            return false;
        }
        return false;
    }

    private void initView(final List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> list) {
        ImageLoader.getInstance().displayImage("http://appapi2.fanerweb.com/html/pages/xl/banner.jpg",ivProductSeries, ImageLoadOptions.getOptionsHigh());
        pullRefreshView.setOnFooterRefreshListener(this);
        pullRefreshView.setOnHeaderRefreshListener(this);
         adapter = new SeriesProductAdapter(list, this,curpage);
//        View view = View.inflate(this,R.layout.head_product_series,null);
//        gvProduct.addHeaderView();
        gvProduct.setAdapter(adapter);
        gvProduct.setFocusable(false);
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
            if(isCustomized){
                 intent = new Intent(ProductSeriesActivity.this, SimpleStyleInfromationActivity.class);
            }else {
                 intent = new Intent(ProductSeriesActivity.this, StyleInfromationActivity.class);
            }
                Bundle pBundle = new Bundle();
                pBundle.putString("itemId", list.get(position).getId());
                pBundle.putInt("type", 0);
                intent.putExtras(pBundle);
                startActivity(intent);
                //设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        ivProductSeries.setFocusable(false);
        idIgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openActivity(MainActivity.class,"selectPosition",0);
                finish();

            }
        });

    }

    @Override
    public void loadNetData() {
        String url = AppURL.URL_MODE_LIST + "tokenKey=" + BaseApplication.getToken() + "&cpage=" + curpage + "&category=" + key;
        L.e("url",url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("loadNetData  " + result);
                baseHideWatLoading();
                JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
                String error = jsonResult.get("error").getAsString();
                if (error.equals("0")) {
                    modeListResult = new Gson().fromJson(result, ModeListResult.class);
                    ModeListResult.DataEntity dataEntity = modeListResult.getData();
                    if (dataEntity == null) {
                        return;
                    }
                    if (list == null) {
                        list = new ArrayList<ModeListResult.DataEntity.ModelEntity.ModelListEntity>();
                    }
                    if(curpage==1){
                        isShowPrice = modeListResult.getData().getMode().getIsShowPrice();
                    }

                   List tempList = modeListResult.getData().getMode().getModelList();
                    if(tempList.size()==0){
                        showToastReal("没有数据了！");
                    }
                    list.addAll(modeListResult.getData().getMode().getModelList());
                    initView(list);
                    endRefresh();
                } else if (error.equals("2")) {
                    L.e("重新登录");
                    ToastManager.showToastReal(jsonResult.get("message").getAsString());
                    openActivity(MainActivity.class, "selectPosition", 1);
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    showToastReal("数据加载错误:+" + message);
                }
            }

            @Override
            public void onFail(String fail) {
                showToastReal("数据获取失败");
                baseHideWatLoading();
            }

        });
    }

    private void endRefresh() {
        pullRefreshView.onHeaderRefreshComplete();
        pullRefreshView.onFooterRefreshComplete();
    }


    @Override
    public void onClick(View v) {
        ToastManager.showToastReal(v.getId());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        ++curpage;
        loadNetData();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        curpage = 1;
        list.clear();
        loadNetData();
    }
}
