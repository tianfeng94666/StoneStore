package stone.tianfeng.com.stonestore.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import stone.tianfeng.com.stonestore.base.BaseApplication;
import stone.tianfeng.com.stonestore.utils.ToastManager;
import stone.tianfeng.com.stonestore.utils.UIUtils;

import org.json.JSONObject;


/**
 * volley get post请求
 * @author wenjundu 2015-07-03
 */
public class VolleyRequestUtils {
	public static void cleanCookie () {
		CookieStringtRequest.cookie = "";
		CookieStringtRequest.isTruncate = true;
	}

	private  VolleyRequestUtils(){

	}
	static VolleyRequestUtils mInstance;
	public static VolleyRequestUtils getInstance() {
		if (mInstance == null) {
			synchronized (OKHttpRequestUtils.class) {
				if (mInstance == null) {
					mInstance = new VolleyRequestUtils();
				}
			}
		}
		return mInstance;
	}

	//！！！！！！！！get请求为了保证cookie一致  后来不要使用该方法！！！！！！！！！！
	public  void getCookieRequest(Context context,String url,final HttpStringRequsetCallBack callback) {

		JsonObjectRequest jsonObjectRequest = null;
		if(!UIUtils.getNetConnecState(context)){
			ToastManager.showToastReal("请检查网络连接！");
		}
		try {
			jsonObjectRequest = new NormalPostRequest( Request.Method.GET, url, null,
                    new Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if(callback!=null)
                            callback.onSuccess(response.toString());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if(callback!=null)
                        callback.onFail(error.toString());
                }

            });
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		jsonObjectRequest.setTag(context);
		BaseApplication.requestQueue.add(jsonObjectRequest);
	}


	//新加接口 ，保证cookie一致的volley网络请求回调这个接口
	public  interface HttpStringRequsetCallBack{
		void onSuccess(String result);
		void onFail(String fail);
	}




}
