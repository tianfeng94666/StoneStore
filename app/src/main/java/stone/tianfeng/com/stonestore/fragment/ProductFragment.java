package stone.tianfeng.com.stonestore.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.activity.ClassifyActivity;
import stone.tianfeng.com.stonestore.activity.ConfirmOrderActivity;
import stone.tianfeng.com.stonestore.activity.CustomMadeActivity;
import stone.tianfeng.com.stonestore.activity.MainActivity;
import stone.tianfeng.com.stonestore.activity.OrderActivity;
import stone.tianfeng.com.stonestore.activity.QuickConfirmOrderActivity;
import stone.tianfeng.com.stonestore.activity.SimpleStyleInfromationActivity;
import stone.tianfeng.com.stonestore.activity.StyleInfromationActivity;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.base.BaseFragment;
import stone.tianfeng.com.stonestore.base.Global;
import stone.tianfeng.com.stonestore.base.MyAction;
import stone.tianfeng.com.stonestore.dialog.GridMenuDialog;
import stone.tianfeng.com.stonestore.inter.ClassifyOnSeachListener;
import stone.tianfeng.com.stonestore.inter.OnSeachListener;
import stone.tianfeng.com.stonestore.json.ClassTypeFilerEntity;
import stone.tianfeng.com.stonestore.json.ModeListResult;
import stone.tianfeng.com.stonestore.json.SearchValue;
import stone.tianfeng.com.stonestore.json.StoneSearchInfoResult;
import stone.tianfeng.com.stonestore.json.TypeFiler;
import stone.tianfeng.com.stonestore.net.ImageLoadOptions;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.SpUtils;
import stone.tianfeng.com.stonestore.utils.StringUtils;
import stone.tianfeng.com.stonestore.utils.ToastManager;
import stone.tianfeng.com.stonestore.utils.UIUtils;
import stone.tianfeng.com.stonestore.viewutils.BadgeView;
import stone.tianfeng.com.stonestore.viewutils.GridViewWithHeaderAndFooter;
import stone.tianfeng.com.stonestore.viewutils.ListMenuDialog;
import stone.tianfeng.com.stonestore.viewutils.LoadingWaitDialog;
import stone.tianfeng.com.stonestore.viewutils.PullToRefreshView;
import stone.tianfeng.com.stonestore.viewutils.SideFilterDialog;
import stone.tianfeng.com.stonestore.viewutils.SquareImageView;
import stone.tianfeng.com.stonestore.viewutils.xListView.XListView;
import zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class ProductFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.id_ig_sao)
    ImageView idIgSao;
    @Bind(R.id.tv_pager_amount)
    TextView tvPagerAmount;
    private LinearLayout layAllOrder, layFilter, layGvFileter, layout1;
    private GridViewWithHeaderAndFooter mCustomGridView;
    private TextView tvCclassify, tvCurentOrder, id_tv_select;
    private Context context;
    private ImageView igNor, igNor1;
    private RelativeLayout layout2, root_view;
    private SideFilterDialog filterDialog;
    private ListMenuDialog listMenuDialog;
    private PullToRefreshView mRefreshView;
    private GridMenuDialog gridMenuDialog;
    private List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> data = new ArrayList<>();
    /*推荐款  最新款*/


    private static final int PULL_REFRESH = 1;
    private static final int PULL_LOAD = 2;
    private int tempCurpage = 1;
    private int pullState = 1;
    private int curpage = 1;
    private int list_count;
    @Bind(R.id.id_et_seach)
    EditText idEtSeach;
    @Bind(R.id.ig_btn_seach)
    ImageView idIgSeach;
    @Bind(R.id.id_tv_filter)
    TextView idTvFilter;
    @Bind(R.id.id_his_order)
    TextView idTvHisOrder;
    @Bind(R.id.id_classify)
    TextView idTvClassify;
    @Bind(R.id.tv_reset)
    TextView idTvCurOrder;
    @Bind(R.id.iv_delete)
    ImageView ivDelete;
    /*搜索过的多选历史记录*/
    public static List<TypeFiler> multiselectKey = new ArrayList<>();
    /*搜索过的单选历史记录*/
    public static List<SearchValue> singleKey = new ArrayList<>();

    private int waitOrderCount;
    private ModeListResult modeListResult;
    private View view;
    private boolean isShowPrice;
    private boolean isCustomized;//是否是用户定制
    StoneSearchInfoResult.DataBean.StoneBean.ListBean selectStone;
    private String openType;
    private int totalAmount;

    BadgeView badge;
    String mcategory="";   /*下啦筛选关键字*/
    String myAction="";   /*判断是哪个页面的action*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.activity_order, null);
        ButterKnife.bind(this, view);
        isShowPrice = SpUtils.getInstace(getActivity()).getBoolean("isShowPrice", true);
        isCustomized = SpUtils.getInstace(getActivity()).getBoolean("isCustomized", true);
        context = getActivity();
        initView();
        initListener();
        String url = getDate();
        loadNetData(url);
        return view;
    }

    private String getDate() {
        String url = getInitUrl();

//        if(selectStone!=null){
//            url = url+"&weight="+selectStone.getWeight();
//        }
        return url;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (modeListResult == null || Global.STONE_POINT_CHANGE == 1) {
            isShowPrice = SpUtils.getInstace(getActivity()).getBoolean("isShowPrice", true);
            isCustomized = SpUtils.getInstace(getActivity()).getBoolean("isCustomized", true);
            loadNetData(getInitUrl());
        }
    }


    private void remind(int count) {
        boolean isVisible = false;
        if (count != 0) {
            isVisible = true;
        }
        //BadgeView的具体使用
        badge.setText(count + ""); // 需要显示的提醒类容
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge.setTextColor(Color.WHITE); // 文本颜色
        int hint = Color.rgb(200, 39, 73);
        badge.setBadgeBackgroundColor(hint); // 提醒信息的背景颜色，自己设置
        badge.setTextSize(10); // 文本大小
        badge.setBadgeMargin(3, 3); // 水平和竖直方向的间距
        badge.setBadgeMargin(5); //各边间隔
        if (isVisible) {
            badge.show();// 只有显示
        } else {
            badge.hide();//影藏显示
        }
    }


    private String getInitUrl() {
        String url = AppURL.URL_MODE_LIST + "tokenKey=" + BaseApplication.getToken() + "&cpage=" + curpage + getCheckBoxUrl()+ getRadioGroupUrl()+ "&pageNum=24";
        return url;
    }


    private void initListMenuDialog(List<ModeListResult.DataEntity.CustomList> customList) {
        listMenuDialog = new ListMenuDialog(getActivity(), customList);
        listMenuDialog.setOnListMenuSelectCloseClick(new ListMenuDialog.OnListMenuSelectCloseClick() {
            @Override
            public void onClose() {
                L.e("onClose");
                backgroundAlpha(1f);
                igNor.setImageResource(R.drawable.icon_list_nor);
            }

            @Override
            public void onSelect(final ModeListResult.DataEntity.CustomList select) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        id_tv_select.setText(StringUtils.idgui(select.getTitle()));
                        mcategory = select.getId() + "";
                        String url = getInitUrl();
                        url += "&category=" + mcategory;
                        loadNetData(url);
                    }
                });
            }
        });
    }


    private void initFilterDialog(List<ClassTypeFilerEntity> typeList) {
        filterDialog = new SideFilterDialog(context, typeList, MyAction.filterDialogAction, getStatusBarHeight());
         /*筛选界面      关闭事件*/
        filterDialog.setOnListMenuSelectCloseClick(new SideFilterDialog.OnListMenuSelectCloseClick() {
            @Override
            public void onClose() {
                L.e("关闭  filterDialog");
                backgroundAlpha(1f);
            }
        });

        /*筛选界面      确认搜索事件*/
        filterDialog.setOnSeachListener(new ClassifyOnSeachListener() {
            @Override
            public void onSeach(String action) {
                myAction = action;
                curpage = 1;
                String url = getInitUrl();
                url += getCheckBoxUrl();
                url += getRadioGroupUrl();
                loadNetData(url);
            }
        });

    }

    LoadingWaitDialog dialog;

    public void showWatiNetDialog() {
        dialog = new LoadingWaitDialog(getActivity());
        dialog.show();
    }

    public void dismissWatiNetDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }


    private void loadNetData(String url) {
        isShowPrice = SpUtils.getInstace(getActivity()).getBoolean("isShowPrice", true);
        showWatiNetDialog();
        L.e("开启搜索" + url);
        if (selectStone != null) {
            L.e("selectStone", selectStone.toString());
        }
        // 进行登录请求
        VolleyRequestUtils.getInstance().getCookieRequest(getActivity(), url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                dismissWatiNetDialog();
                L.e("loadNetData  " + result);
                JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
                String error = jsonResult.get("error").getAsString();
                if (isShowPrice) {
                    idTvHisOrder.setTextColor(getResources().getColor(R.color.text_color));
                } else {
                    idTvHisOrder.setTextColor(getResources().getColor(R.color.text_color3));
                }

                if (error.equals("0")) {
                    modeListResult = new Gson().fromJson(result, ModeListResult.class);
                    ModeListResult.DataEntity dataEntity = modeListResult.getData();
                    if (dataEntity == null) {
                        return;
                    }
                    totalAmount =Integer.parseInt(modeListResult.getData().getModel().getList_count());
                    Global.STONE_POINT_CHANGE = 0;
                    ModeListResult.DataEntity.ModelEntity modeEntity = dataEntity.getMode();
                    if (curpage == 1) {
                          /*搜索过的单选历史记录*/
                        ClassifyActivity.singleKey = dataEntity.getSearchKeyword();
                        singleKey = dataEntity.getSearchKeyword();
                        /*搜索过的多选历史记录*/
                        multiselectKey = dataEntity.getCategoryFiler();
                        waitOrderCount = Integer.valueOf(dataEntity.getWaitOrderCount());
                        if (waitOrderCount != 0) {
                            remind(waitOrderCount);
                        }
                    }
                    //下啦分类筛选
                    List<ModeListResult.DataEntity.CustomList> customList = dataEntity.getCustomList();
                    if (customList != null && customList.size() != 0) {
                        initListMenuDialog(customList);
                    }

                    List<ClassTypeFilerEntity> typeList = dataEntity.getTypeList();
                    if (typeList != null && typeList.size() != 0) {
                        initFilterDialog(typeList);
                    }
                    if (pullState != PULL_LOAD) {
                        data.clear();
                    }
                    list_count = Integer.valueOf(modeEntity.getList_count());
                    if (list_count == 0) {
                        data.clear();
                    } else {
                        List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> modelList = modeEntity.getModelList();
                        data.addAll(modelList);
                    }
                    endNetRequest();
                } else if (error.equals("2")) {
                    L.e("重新登录");
                    ToastManager.showToastReal(jsonResult.get("message").getAsString());
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
                dismissWatiNetDialog();
            }

        });


    }


    private void endNetRequest() {
        mGvAdapter.notifyDataSetChanged();
        tempCurpage = curpage;
        if (pullState == PULL_LOAD) {
            mRefreshView.onFooterRefreshComplete();
        } else if (pullState == PULL_REFRESH) {
            mRefreshView.onHeaderRefreshComplete();
        }
        pullState = 0;
    }

    View loadStateView;
    TextView hint_txt;

    public void initView() {
        ivLeft.setVisibility(View.GONE);
        mRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh_view);
        mRefreshView.setOnHeaderRefreshListener(this);
        mRefreshView.setOnFooterRefreshListener(this);
        mRefreshView.setVisibility(View.VISIBLE);
        igNor = (ImageView) view.findViewById(R.id.id_ig_nor);
        igNor1 = (ImageView) view.findViewById(R.id.id_ig_nor1);
        layAllOrder = (LinearLayout) view.findViewById(R.id.id_ly_all);
        layGvFileter = (LinearLayout) view.findViewById(R.id.id_gv_fileter);
        id_tv_select = (TextView) view.findViewById(R.id.id_tv_select);
        root_view = (RelativeLayout) view.findViewById(R.id.root_view);
        layout1 = (LinearLayout) view.findViewById(R.id.id_rel2);
        layout2 = (RelativeLayout) view.findViewById(R.id.id_rel3);
        //筛选
        layFilter = (LinearLayout) view.findViewById(R.id.id_ly_filter);
        tvCclassify = (TextView) view.findViewById(R.id.id_tv_classify);
        tvCurentOrder = (TextView) view.findViewById(R.id.id_cur_order);
        mCustomGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.id_gv_menu);
        loadStateView = View.inflate(getActivity(), R.layout.grid_food_layout, null);
        hint_txt = (TextView) loadStateView.findViewById(R.id.tv_hint_txt);
        //mCustomGridView.addFooterView(loadStateView);
        //没有数据显示
        mCustomGridView.setEmptyView(view.findViewById(R.id.lny_no_result));
        tvPagerAmount.getBackground().setAlpha(160);
        mCustomGridView.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("firstVisibleItem=" + firstVisibleItem);
                if (firstVisibleItem == 0) {
                    firstVisibleItem = 1;
                }

                tvPagerAmount.setText((int) (Math.ceil(firstVisibleItem / 24.0)) + "/" + (int) Math.ceil(totalAmount / 24.0));

            }
        });
        if (isScreenChange()) {
            mCustomGridView.setNumColumns(4);
        } else {
            mCustomGridView.setNumColumns(2);
        }
        mCustomGridView.setAdapter(mGvAdapter);

        badge = new BadgeView(getActivity(), idTvCurOrder);// 创建一个BadgeView对象，view为你需要显示提醒的控件
        //remind(1,badge1,true);

        idIgSao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idEtSeach.setText("");
            }
        });
        idEtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    curpage = 1;
                    String url = getInitUrl();
                    url += getkeyWordUrl();
                    loadNetData(url);
                    // search pressed and perform your functionality.
                }
                return false;
            }

        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 如果是橫屏時候
        try {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mCustomGridView.setNumColumns(4);
                Global.divideAmount = 4;
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mCustomGridView.setNumColumns(2);
                Global.divideAmount = 2;
            }
        } catch (Exception ex) {

        }

    }


    public String getCheckBoxUrl() {
        List<TypeFiler> filterList;
        if (myAction.equals(MyAction.classifyActivityAction)) {
            filterList = ClassifyActivity.hisCategoryFilterList;
        } else {
            filterList = ProductFragment.multiselectKey;
        }
        List<String> list = new ArrayList<>();
        int count = filterList.size();
        for (int i = 0; i < count; i++) {
            TypeFiler typeFiler = filterList.get(i);
            list.add(typeFiler.getGroupKey());
            // L.e(""+typeFiler.toString());
        }
        HashSet<String> hs = new HashSet<>(list); //此时已经去掉重复的数据保存在hashset中
        Iterator<String> iterator = hs.iterator();
        StringBuffer sbbuf = new StringBuffer();
        while (iterator.hasNext()) {
            StringBuffer sb = new StringBuffer();
            String tag = iterator.next();
            sb.append("&" + tag + "=");
            for (int i = 0; i < count; i++) {
                TypeFiler typeFiler = filterList.get(i);
                if (typeFiler.getGroupKey().equals(tag)) {
                    sb.append(typeFiler.getValue() + "|");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sbbuf.append(sb.toString());
        }
        L.e("typeFiler  check" + sbbuf.toString());
        return sbbuf.toString();
    }

    public String getRadioGroupUrl() {
        List<SearchValue> keywordList;
        if (myAction.equals(MyAction.classifyActivityAction)) {
            keywordList = ClassifyActivity.singleKey;
        } else {
            keywordList = singleKey;
        }
        String low, hig;
        StringBuilder sbPrice = new StringBuilder();
        if (keywordList != null && keywordList.size() != 0) {
            for (SearchValue hisKey : keywordList) {
                if (!StringUtils.isEmpty(hisKey.getValue())) {
                    sbPrice.append("&" + hisKey.getName() + "=" + hisKey.getValue());
                } else {
                    low = hisKey.getLow();
                    hig = hisKey.getHig();
                    if (!StringUtils.isEmpty(low) && !StringUtils.isEmpty(hig)) {
                        sbPrice.append("&" + hisKey.getName() + "=" + low + "|" + hig);
                    } else if (!StringUtils.isEmpty(low) && StringUtils.isEmpty(hig)) {
                        sbPrice.append("&" + hisKey.getName() + "=" + low + "|");
                    } else if (StringUtils.isEmpty(low) && !StringUtils.isEmpty(hig)) {
                        sbPrice.append("&" + hisKey.getName() + "=" + "|" + hig);
                    }
                }
            }
            L.e(sbPrice.toString());
        }
        L.e("typeFiler  radio" + sbPrice.toString());
        return sbPrice.toString();
    }

    /**
     * 关键字
     *
     * @return
     */
    public String getkeyWordUrl() {
        String url = "";
        String keyWord = idEtSeach.getText().toString();
        if (!StringUtils.isEmpty(keyWord)) {
            keyWord = StringUtils.removeSpacesUrl(keyWord);
            idTvFilter.setVisibility(View.VISIBLE);
            idTvFilter.setText(keyWord);
        }
        url = "&keyword=" + keyWord;
        return url;
    }


    private void initListener() {
        /*分类界面的确认搜索*/
        ClassifyActivity.setOnClassifySeachListener(new ClassifyOnSeachListener() {
            @Override
            public void onSeach(String action) {
                myAction = action;
                curpage = 1;
                String url = getInitUrl();
                url += getCheckBoxUrl();
                url += getRadioGroupUrl();
                loadNetData(url);
            }
        });

        /*弹出筛选页面dialog*/
        layFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterDialog != null) {
                    backgroundAlpha(0.7f);
                    filterDialog.showAsDropDown(root_view, getStatusBarHeight());
                }

            }
        });


        /*弹出下拉别表搜索事件*/
        layAllOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listMenuDialog != null) {
                    backgroundAlpha(0.7f);
                    listMenuDialog.showAsDropDown(layout1);
                    igNor.setImageResource(R.drawable.icon_list);
                }
            }
        });


        mCustomGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.get(position).getId();
                Intent intent;
                if (isCustomized) {
                    intent = new Intent(getActivity(), SimpleStyleInfromationActivity.class);
                } else {
                    intent = new Intent(getActivity(), StyleInfromationActivity.class);
                }
                Bundle pBundle = new Bundle();
                L.e("itemId" + data.get(position).getId());
                pBundle.putString("itemId", data.get(position).getId());
                pBundle.putInt("type", 0);
                pBundle.putString("openType", openType + "");
                pBundle.putInt("waitOrderCount", waitOrderCount);
                if (selectStone != null) {
                    pBundle.putSerializable("stone", selectStone);
                }
                intent.putExtras(pBundle);
                startActivityForResult(intent, 10);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


        tvCurentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openActivity(CustommadeInformationActivity.class, null);
            }
        });

        /*弹出 已被选中的标签dialog  GridView*/
        layGvFileter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridMenuDialog = new GridMenuDialog(getActivity());
                gridMenuDialog.setOnListMenuSelectCloseClick(new GridMenuDialog.OnListMenuSelectCloseClick() {
                    @Override
                    public void onClose() {
                        L.e("onClose");
                        backgroundAlpha(1f);
                        igNor1.setImageResource(R.drawable.icon_list_nor);
                    }

                    @Override
                    public void onSelect(final String select) {
                        L.e("当前选择" + select);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                id_tv_select.setText(select);
                            }
                        });
                    }
                });
                gridMenuDialog.setOnSeachListener(new OnSeachListener() {
                    @Override
                    public void onSeach(String seachUrl) {
                        String url = getInitUrl();
                        url += seachUrl;
                        loadNetData(url);
                    }
                });

                backgroundAlpha(0.7f);
                gridMenuDialog.showAsDropDown(layout2);
                igNor1.setImageResource(R.drawable.icon_list);
            }
        });

        /*开始搜索事件*/
        idIgSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idEtSeach.getText().toString().equals("")) {
                    ToastManager.showToastReal("搜索内容为空！");
                    return;
                }
                curpage = 1;
                String url = getInitUrl();
                url += getkeyWordUrl();
                loadNetData(url);
            }
        });

        /*关键字标签清空*/
        idTvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idTvFilter.setVisibility(View.GONE);
                idTvFilter.setText("");
                idEtSeach.setText("");
                loadNetData(getInitUrl());
            }
        });



        /*历史订单*/
        idTvHisOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPrice) {
                    openActivity(CustomMadeActivity.class, null);
                }
            }
        });


        idTvCurOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(isCustomized){
                    intent = new Intent(getActivity(), QuickConfirmOrderActivity.class);
                }else {
                    intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("waitOrderCount", waitOrderCount);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                //openActivity(ConfirmOrderActivity.class,null);
            }
        });

        idTvClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ClassifyActivity.class, null);
            }
        });

    }


    private BaseAdapter mGvAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_goods_list, parent, false);
                holder.tvModelNum = (TextView) convertView.findViewById(R.id.tv_modelNum);
                holder.llPrice = (LinearLayout) convertView.findViewById(R.id.ll_price);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_sum_price);
                holder.ig = (SquareImageView) convertView.findViewById(R.id.product_img);
                holder.tvDescript =(TextView)convertView.findViewById(R.id.tv_descript);
                holder.tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // holder.ig.setImageResource(R.drawable.no_image);
            holder.tvModelNum.setText(data.get(position).getModelNum());
            holder.tvTitle.setText(data.get(position).getTitle());
            holder.tvDescript.setText(data.get(position).getDescribe());
            holder.tvPrice.setText(UIUtils.stringChangeToInt(data.get(position).getPrice()) + "");
            if (data.get(position).getPic() == null || !data.get(position).getPic().equals(holder.ig.getTag())) {
                // 如果不相同，就加载。改变闪烁的情况
                if (UIUtils.isPad(getActivity())) {
                    ImageLoader.getInstance().displayImage(data.get(position).getPic(), holder.ig, ImageLoadOptions.getOptionsHigh());
                } else {
                    ImageLoader.getInstance().displayImage(data.get(position).getPicm(), holder.ig, ImageLoadOptions.getOptionsHigh());
                }
                holder.ig.setTag(data.get(position).getPic());
            }
            if (curpage == 1) {
                if (isShowPrice) {
                    holder.llPrice.setVisibility(View.VISIBLE);
                } else {
                    holder.llPrice.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        class ViewHolder {
            SquareImageView ig;
            TextView tvModelNum;
            TextView tvPrice;
            LinearLayout llPrice;
            TextView tvTitle;
            TextView tvDescript;
        }
    };

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

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (data.size() < list_count) {
            tempCurpage = curpage;
            curpage++;
            pullState = PULL_LOAD;
            loadNetData(getInitUrl());
        } else {
            hint_txt.setText("已加载全部数据喲");
            ToastManager.showToastReal("没有更多数据");
            view.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        tempCurpage = curpage;
        curpage = 1;
        pullState = PULL_REFRESH;
        hint_txt.setText("上拉加载更多");
        loadNetData(getInitUrl());
    }


    /*扫描二维码页面*/
    public void scan(View view) {
        Intent inten = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(inten, 0);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(requestCode + "");
        if (requestCode == 0 && data != null) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            idEtSeach.setText(result);
            String url = getInitUrl();
            url += getkeyWordUrl();
            loadNetData(url);
            L.e("onActivityResult result" + result);
        }

        if (requestCode == 10 && data != null) {
            Bundle bundle = data.getExtras();
            waitOrderCount = bundle.getInt("waitOrderCount");
            remind(waitOrderCount);
            L.e("waitOrderCount");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
