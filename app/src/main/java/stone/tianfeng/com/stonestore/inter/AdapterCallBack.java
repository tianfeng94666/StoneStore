package stone.tianfeng.com.stonestore.inter;

import stone.tianfeng.com.stonestore.json.OrderListResult;

import java.util.Map;

public interface AdapterCallBack {

    void changeState(Map<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> checked);

}
