package stone.tianfeng.com.stonestore.viewutils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.activity.MainActivity;
import stone.tianfeng.com.stonestore.base.BaseFilterData;
import stone.tianfeng.com.stonestore.base.MyAction;
import stone.tianfeng.com.stonestore.fragment.ProductFragment;
import stone.tianfeng.com.stonestore.json.ClassTypeFilerEntity;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.UIUtils;

import java.util.List;


/*
 * 创建人：Yangshao
 * 创建时间：2016/10/24 18:02
 * @version    侧滑筛选菜单
 *
 */

public  class SideFilterDialog extends BaseFilterData {

    private View mPopView;
    private Context mContext;

    private PopupWindow popupWindow;
    private ExpandableListView expandableGridView;
    private List<ClassTypeFilerEntity> mTypeListData;
    /*确定和重置按钮*/
    private TextView idTvConfirfilterr;
    private TextView idTvResetfilter;

    //得到 状态栏的高度
    int mStatusBarHeight;
    public SideFilterDialog(Context context, List<ClassTypeFilerEntity> typeListData, String action,int statusBarHeight) {
        this.mStatusBarHeight=statusBarHeight;
        this.mContext = context;
        this.mTypeListData = typeListData;
        initView(context);
        initBaseFilterData(context, action);
        initListener();
    }



    private void initListener() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                L.e("SideFilterDialog  消失");
                dismiss();
            }
        });
        idTvResetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*单选始终有默认值  只需要情况里面的Value*/
                for (int i = 0; i <  ((ProductFragment)((MainActivity)(context)).productFragment).singleKey.size(); i++) {
                    ((ProductFragment)((MainActivity)(context)).productFragment).singleKey.get(i).setValue("");
                }
                ((ProductFragment)((MainActivity)(context)).productFragment).multiselectKey.clear();
                adapter.isResetGridTextAdapter();
            }
        });


        /*提交  开始搜索*/
        idTvConfirfilterr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSeachListener != null) {
                    onSeachListener.onSeach(MyAction.filterDialogAction);
                }
                //getCheckBoxUrl();
                // getRadioGroupUrl();
                dismiss();
            }
        });

    }

    public boolean isScreenChange() {

        Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
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
    public void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopView = inflater.inflate(R.layout.dialog_filter_dialog, null);
        expandableGridView = (ExpandableListView) mPopView.findViewById(R.id.list);
        idTvConfirfilterr = (TextView) mPopView.findViewById(R.id.id_tv_confirfilterr);
        idTvResetfilter = (TextView) mPopView.findViewById(R.id.id_tv_resetfilter);
        if(isScreenChange()){
            popupWindow = new PopupWindow(mPopView, getWindowWidth() /5*2, getWindowHeight()-mStatusBarHeight);
        }else {
            popupWindow = new PopupWindow(mPopView, getWindowWidth() - 150, getWindowHeight()-mStatusBarHeight);
        }
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());     //点击外部消失这句很重要
        // 点击外面的控件也可以使得PopUpWindow dimiss
        popupWindow.setOutsideTouchable(true);

    }

    public void getWindowHight(){
        DisplayMetrics metric = new DisplayMetrics();
        UIUtils.getWindowManager().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 2
    }


    @Override
    public ExpandableListView getExpandableGridView() {
        return expandableGridView;
    }

    @Override
    public List<ClassTypeFilerEntity> getTypeListData() {
        return mTypeListData;
    }

    // 隐藏菜单
    public void dismiss() {
        if (onListMenuCloseClick != null) {
            popupWindow.dismiss();
            onListMenuCloseClick.onClose();
        }

    }

    public void setOnListMenuSelectCloseClick(OnListMenuSelectCloseClick onListMenuCloseClick) {
        this.onListMenuCloseClick = onListMenuCloseClick;
    }

    OnListMenuSelectCloseClick onListMenuCloseClick;

    @Override
    public void loadNetData() {

    }

    public interface OnListMenuSelectCloseClick {
        void onClose();
    }

    // 下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(RelativeLayout parent, int y) {
        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, y);
    }

    public int getWindowWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public int getWindowHeight() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
