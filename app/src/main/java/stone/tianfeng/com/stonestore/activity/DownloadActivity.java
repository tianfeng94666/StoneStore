package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class DownloadActivity extends BaseActivity {
    @Bind(R.id.tv_download_name)
    TextView tvDownloadName;
    @Bind(R.id.iv_downlaoad)
    ImageView ivDownlaoad;
    @Bind(R.id.tv_websit)
    TextView tvWebsit;
    String type;
    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        getData();
        initView();
    }



    private void getData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }

    private void initView() {
        final String st;

            tvDownloadName.setText("安卓最新版");
//            SpannableString ss = new SpannableString("");
            tvWebsit.setText(R.string.android_download);
            st = "https://www.pgyer.com/kbRT";
            ivDownlaoad.setImageResource(R.drawable.android_download);

        tvWebsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(st));
                startActivity(intent);
                //设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void loadNetData() {

    }
}
