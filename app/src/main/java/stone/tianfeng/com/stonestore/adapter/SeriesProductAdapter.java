package stone.tianfeng.com.stonestore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.json.ModeListResult;
import stone.tianfeng.com.stonestore.json.StoneSearchInfoResult;
import stone.tianfeng.com.stonestore.net.ImageLoadOptions;
import stone.tianfeng.com.stonestore.utils.SpUtils;
import stone.tianfeng.com.stonestore.viewutils.SquareImageView;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class SeriesProductAdapter extends BaseAdapter {
    private final boolean isShowPice;
    private final int curpage;
    Context context;
    List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> list;
    StoneSearchResultAdapter.ChooseItemInterface chooseItem;

    public SeriesProductAdapter(List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> list, Context context,int curpage) {
        this.context = context;
        this.list = list;
        this.isShowPice = SpUtils.getInstace(context).getBoolean("isShowPrice",true);
        this.curpage =curpage;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        final ModeListResult.DataEntity.ModelEntity.ModelListEntity bean = list.get(position);
        if (view == null) {
            view = View.inflate(context, R.layout.adapter_goods_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(bean.getTitle());
        DecimalFormat df = new DecimalFormat("######0.00");

        holder.tvSumPrice.setText(df.format(Double.parseDouble(bean.getPrice())));
        if (bean.getPic() == null || !bean.getPic().equals(holder.productImg.getTag())) {
            // 如果不相同，就加载。改变闪烁的情况
            ImageLoader.getInstance().displayImage(bean.getPic(), holder.productImg, ImageLoadOptions.getOptions());
            holder.productImg.setTag(bean.getPic());
        }

            if (isShowPice ) {
                holder.llPrice.setVisibility(View.VISIBLE);
            } else {
                holder.llPrice.setVisibility(View.GONE);
            }

        return view;
    }


    class ViewHolder {
        @Bind(R.id.product_img)
        SquareImageView productImg;
        @Bind(R.id.img_container)
        LinearLayout imgContainer;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.menu_line)
        View menuLine;
        @Bind(R.id.tv_sum_price)
        TextView tvSumPrice;
        @Bind(R.id.ll_price)
        LinearLayout llPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
