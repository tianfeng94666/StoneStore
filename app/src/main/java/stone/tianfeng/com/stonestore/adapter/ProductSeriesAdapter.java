package stone.tianfeng.com.stonestore.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.json.Pictures;
import stone.tianfeng.com.stonestore.net.ImageLoadOptions;
import stone.tianfeng.com.stonestore.utils.MeasureUtil;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class ProductSeriesAdapter extends BaseAdapter {
    private final int imageWidth;
    private final int imageHeight;
    List<Pictures> list;
    Context context;

    public ProductSeriesAdapter(List<Pictures> list, Context context,int imageWidth,int imageHeight) {
        this.list = list;
        this.context = context;
        this.imageWidth =imageWidth;
        this.imageHeight =imageHeight;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_product_v, null);
            viewHoder = new ViewHolder(convertView);
            convertView.setTag(viewHoder);
        }else {
           viewHoder = (ViewHolder) convertView.getTag();
        }
//        viewHoder.ivProductSeries.setMaxWidth(imageWidth);
//        viewHoder.ivProductSeries.setMaxHeight(imageHeight);
        viewHoder.ivProductSeries.setLayoutParams(new LinearLayout.LayoutParams(imageWidth,imageHeight));
        ImageLoader.getInstance().displayImage(list.get(position).getPic(), viewHoder.ivProductSeries,ImageLoadOptions.getOptionsHigh());
        Log.e("item","图片加载"+position);
        return convertView;
    }



    class ViewHolder {
        @Bind(R.id.iv_product_series)
        ImageView ivProductSeries;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
