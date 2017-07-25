package stone.tianfeng.com.stonestore.viewutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import stone.tianfeng.com.stonestore.R;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class ScaleImageView extends ImageView {
    private Context mContext;
    private static int SCREENWIDTH;
    private double widthPercent;
    private double heithPercent;//高度是宽度的倍数

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        SCREENWIDTH = getScreenWidth();
//        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.ScaleImageView, defStyleAttr, 0);
//        widthPercent = a.getFloat(R.styleable.ScaleImageView_width_percent, 1);
//        heithPercent = a.getFloat(R.styleable.ScaleImageView_height_percent, 1);
//        a.recycle();
    }


    private int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension((int) (SCREENWIDTH), (int) SCREENWIDTH);
    }
}
