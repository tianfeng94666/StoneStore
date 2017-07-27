package stone.tianfeng.com.stonestore.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.activity.MainActivity;
import stone.tianfeng.com.stonestore.activity.ProductSeriesActivity;
import stone.tianfeng.com.stonestore.adapter.ProductSeriesAdapter;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.base.BaseFragment;
import stone.tianfeng.com.stonestore.json.Home2Result;
import stone.tianfeng.com.stonestore.json.HomeResult;
import stone.tianfeng.com.stonestore.json.Pictures;
import stone.tianfeng.com.stonestore.net.OKHttpRequestUtils;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.MeasureUtil;
import stone.tianfeng.com.stonestore.utils.ToastManager;
import stone.tianfeng.com.stonestore.utils.UIUtils;
import stone.tianfeng.com.stonestore.viewutils.CircleImageView;
import stone.tianfeng.com.stonestore.viewutils.CustomGridView;
import stone.tianfeng.com.stonestore.viewutils.FlyBanner;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/21 17:01
 * @version    主页
 *
 */
public class HomeFragment extends BaseFragment {

    CustomGridView mCustomGridView;
    //得到宽度
    int mCusGridViewWidth;
    @Bind(R.id.id_ig_icon)
    CircleImageView idIgIcon;
    @Bind(R.id.id_tv_name)
    TextView idTvName;
    @Bind(R.id.id_ig_setting)
    ImageView idIgSetting;
    @Bind(R.id.id_ig_stone)
    ImageView idIgStone;

    FlyBanner flyBanner;
    @Bind(R.id.gv_series)
    CustomGridView gvProductSeries;
    @Bind(R.id.gv_success)
    CustomGridView gvSucces;
    @Bind(R.id.fl_flybanner)
    FrameLayout flFlybanner;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray;
    private int screenWidth;

    public HomeFragment() {

    }


    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    private void initView() {
        screenWidth = MeasureUtil.getScreenSize(getActivity())[0];
        gvProductSeries.setFocusable(false);
        gvSucces.setFocusable(false);
        initNetBanner();

        final List<Pictures> list = new ArrayList<>();
        list.addAll(homeResult.getData().getClassAd_1());
        ProductSeriesAdapter productSeriesAdapter = new ProductSeriesAdapter(list, getActivity(),  (screenWidth / 2), (int) ((screenWidth / 2) / 1.60));
        gvProductSeries.setAdapter(productSeriesAdapter);
        gvProductSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductSeriesActivity.class);
                intent.putExtra("key", list.get(position).getKey());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


        List listsucces = new ArrayList();
        listsucces.addAll(homeResult.getData().getClassAd_2());
        ProductSeriesAdapter successAdapter = new ProductSeriesAdapter(listsucces, getActivity(), screenWidth, (int) (screenWidth / 2.5));
        gvSucces.setAdapter(successAdapter);


        UIUtils.setGridViewHeightBasedOnChildren(gvProductSeries, 2);
        UIUtils.setGridViewHeightBasedOnChildren(gvSucces, 1);
        list.clear();
        listsucces.clear();
        list.addAll(homeResult.getData().getClassAd_1());
        listsucces.addAll(homeResult.getData().getClassAd_2());

        productSeriesAdapter.notifyDataSetChanged();
        successAdapter.notifyDataSetChanged();
        Log.e("转屏", ";a;a;a");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        baseShowWatLoading();
        loadNetData();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("life", "onresume");
        baseShowWatLoading();
        loadNetData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initView();
    }

    /**
     * 加载网页图片
     */
    private void initNetBanner() {
//        flFlybanner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,screenWidth/2));
        flyBanner = (FlyBanner) view.findViewById(R.id.flybanner);
        ViewGroup.LayoutParams lp = flyBanner.getLayoutParams();
        lp.height = (int) (screenWidth / 1.56);
        flyBanner.setLayoutParams(lp);
        List<Pictures> list = homeResult.getData().getScrollAd();
        List<String> imgesUrl = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            imgesUrl.add(list.get(i).getPic());
        }
        flyBanner.setImagesUrl(imgesUrl);

        flyBanner.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.mipmap.point_select);
            } else {
                tips[i].setBackgroundResource(R.mipmap.point_unselect);
            }
        }
    }


    Home2Result homeResult;
    List<HomeResult.DataEntity.FunctionsListEntity> functionsList;

    public void loadNetData() {
        final String homeurl = AppURL.URL_HOME2 + "tokenKey=" + BaseApplication.getToken();
        L.e("netLogin" + homeurl);
        VolleyRequestUtils.getInstance().getCookieRequest(getActivity(), homeurl, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                baseHideWatLoading();
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    homeResult = new Gson().fromJson(result, Home2Result.class);
                    if (homeResult.getData() == null) {
                        return;
                    }
                    initView();
                } else if (error == 2) {
                    loginToServer(MainActivity.class);
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    ToastManager.showToastReal("数据加载错误！");
                }

            }

            @Override
            public void onFail(String fail) {
                ToastManager.showToastReal("数据获取失败");
                baseHideWatLoading();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public class ViewHolder {
        ImageView ig;
        TextView tv;
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mImageViews[position % mImageViews.length];
        }


    }
}

