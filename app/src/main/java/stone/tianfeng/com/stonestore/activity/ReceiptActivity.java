package stone.tianfeng.com.stonestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import stone.tianfeng.com.stonestore.R;
import stone.tianfeng.com.stonestore.adapter.BaseViewHolder;
import stone.tianfeng.com.stonestore.adapter.CommonAdapter;
import stone.tianfeng.com.stonestore.base.AppURL;
import stone.tianfeng.com.stonestore.base.BaseActivity;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.json.InvocieResult;
import stone.tianfeng.com.stonestore.net.OKHttpRequestUtils;
import stone.tianfeng.com.stonestore.net.VolleyRequestUtils;
import stone.tianfeng.com.stonestore.utils.L;
import stone.tianfeng.com.stonestore.utils.StringUtils;
import stone.tianfeng.com.stonestore.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/12/15 8:53
 * @version  发票
 *
 */
public class ReceiptActivity extends BaseActivity {


    private ListView mListView;
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.id_tv_invTitle)
    EditText edInvTitle;


    @Bind(R.id.id_tv_confir)
    TextView tvConfir;
    @Bind(R.id.id_tv_cancle)
    TextView tvCancle;
    List<InvocieResult.DataEntity.InvoiceTypeEntity> mData;
    ListViewAdapter mAdapter;
    int invTypeId;
    String invTypeTitle;

    final int SET_INVOICE = 1;
    final int UPDATE_INVOICE = 2;
    int INVOICE_TYPE;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        ButterKnife.bind(this);
        getDate();
        initView();
        loadNetData();
    }

    private void initView() {
        titleText.setText("开发票");
        mData = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//开启单选模式
        mAdapter = new ListViewAdapter(mData, R.layout.adapter_receipt_item);
        mListView.setAdapter(mAdapter);
        tvConfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edInvTitleStr = edInvTitle.getText().toString();
                if (StringUtils.isEmpty(edInvTitleStr)) {
                    ToastManager.showToastReal("请填写抬头发票");
                    return;
                }
                if (StringUtils.isEmpty(invTypeTitle)) {
                    ToastManager.showToastReal("请填写抬头发票内容");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("invTitle", edInvTitleStr);
                intent.putExtra("invTypeId", invTypeId + "");
                intent.putExtra("invTypeTitle", invTypeTitle);
                intent.putExtra("type", INVOICE_TYPE);
                setResult(13, intent);
                finish();
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("invTitle", "");
                intent.putExtra("invTypeId", "");
                intent.putExtra("invTypeTitle", "");
                setResult(13, intent);
                finish();
            }
        });
    }

    private void getDate() {
        INVOICE_TYPE = getIntent().getIntExtra("type", 1);
        type = getIntent().getStringExtra("isStone");
        if (type == null) {
            type = "1";
        }
        invTypeTitle = getIntent().getStringExtra("invTitle");
        L.e(INVOICE_TYPE + "");
        if (StringUtils.isEmpty(invTypeTitle)) {
            edInvTitle.setText(invTypeTitle);
        }
    }

    @Override
    public void loadNetData() {
        String url;
        if (type.equals("2")) {
            url = AppURL.URL_STONE_INVOICE + "tokenKey=" + BaseApplication.getToken();
        } else {
            url = AppURL.URL_MODELINVOICE_PAGE + "tokenKey=" + BaseApplication.getToken();
        }


        L.e(url);
        // 进行登录请求
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    InvocieResult invocieResult = new Gson().fromJson(result, InvocieResult.class);
                    InvocieResult.DataEntity data = invocieResult.getData();
                    if (data == null) {
                        return;
                    }
                    mData = data.getInvoiceType();
                    mAdapter.setListData(mData);
                } else if (error == 2) {
                    L.e("重新登录");
                } else if (error == 3) {
                   showToastReal("未审核");
                }else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                    showToastReal("数据加载错误:+"+message);
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

    public class ListViewAdapter extends CommonAdapter<InvocieResult.DataEntity.InvoiceTypeEntity> {
        private int temp = -1;

        public ListViewAdapter(List<InvocieResult.DataEntity.InvoiceTypeEntity> invoiceType, int itemLayoutId) {
            super(invoiceType, itemLayoutId);
            L.e("ListViewAdapter" + temp);
        }

        @Override
        public void convert(final int position, BaseViewHolder helper, InvocieResult.DataEntity.InvoiceTypeEntity item) {
            L.e("convert" + temp);
            final RadioButton radioButton = helper.getView(R.id.backup_record_item_btn);
            radioButton.setText(item.getTitle());
            radioButton.setId(position);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    temp = radioButton.getId();
                    invTypeId = getItem(position).getId();
                    invTypeTitle = getItem(position).getTitle();
                    notifyDataSetChanged();
                }
            });
            if (position == temp) {
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }
        }

    }
}
