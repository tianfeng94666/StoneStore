package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.fragment.StoneChooseFromSettingFragment;
import stone.tianfeng.com.stonestore.fragment.StoneFragment;
import stone.tianfeng.com.stonestore.fragment.StoneOrderFragemnt;
import stone.tianfeng.com.stonestore.json.StoneDetail;
import stone.tianfeng.com.stonestore.json.StoneEntity;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class StoneChooseMainActivity extends BaseActivity {


    public List<Fragment> fragmentList = new ArrayList<>();
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private PagerAdapter pagerAdapter;
    private Fragment stoneChooseFromSettingFragment,stoneChooseFromStoneHouseFragment;
    private StoneDetail stoneDetail;
    private int openType;
    private String itemId;
    private int type;
    private StoneEntity stoneEntity;
    private int isCanSelectStone;
    private int fromProduct;

    public int getFromProduct() {
        return fromProduct;
    }

    public void setFromProduct(int fromProduct) {
        this.fromProduct = fromProduct;
    }

    public StoneEntity getStoneEntity() {
        return stoneEntity;
    }

    public void setStoneEntity(StoneEntity stoneEntity) {
        this.stoneEntity = stoneEntity;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stone_choose_main);
        ButterKnife.bind(this);
        getDate(getIntent());
        initView();
    }

    private void getDate(Intent intent) {
        stoneDetail = (StoneDetail)intent.getSerializableExtra("stoneDetail");
        openType = intent.getIntExtra("openType", 0);
        itemId = intent.getStringExtra("itemId");
        type = intent.getIntExtra("type", 0);
//        isCanSelectStone = getIntent().getIntExtra("isCanSelectStone",0);
        isCanSelectStone=1;
        stoneEntity = (StoneEntity) intent.getSerializableExtra("stone");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDate(intent);

    }

    @Override
    public void loadNetData() {

    }

    private void initView() {



        switch (isCanSelectStone){
            case 0:
                stoneChooseFromStoneHouseFragment = new StoneChooseFromSettingFragment();
                fragmentList.add(stoneChooseFromStoneHouseFragment);
                tab.addTab(tab.newTab().setText("1"));
                mTitleList.add("选择主石规格");
                break;
            case 1:
                stoneChooseFromSettingFragment = new StoneFragment(2);
                fragmentList.add(stoneChooseFromSettingFragment);
                tab.addTab(tab.newTab().setText("1"));
                mTitleList.add("从裸钻库中挑选");
                break;
            case 2:
                tab.addTab(tab.newTab().setText("1"));
                tab.addTab(tab.newTab().setText("2"));
                //添加页卡标题
                mTitleList.add("选择主石规格");
                mTitleList.add("从裸钻库中挑选");
                stoneChooseFromStoneHouseFragment = new StoneChooseFromSettingFragment();
                fragmentList.add(stoneChooseFromStoneHouseFragment);
                stoneChooseFromSettingFragment = new StoneFragment(2);
                fragmentList.add(stoneChooseFromSettingFragment);
                break;
        }
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
        tab.setTabsFromPagerAdapter(pagerAdapter);
        idIgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private List<?> fragments;

        public PagerAdapter(FragmentManager fm, List<?> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }

    public StoneDetail getStoneDetail() {
        return stoneDetail;
    }

    public void setStoneDetail(StoneDetail stoneDetail) {
        this.stoneDetail = stoneDetail;
    }
}
