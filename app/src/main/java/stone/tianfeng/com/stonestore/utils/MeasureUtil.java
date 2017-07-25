package stone.tianfeng.com.stonestore.utils;

/** 
 * @author 欧阳麟宇  
 * @version 创建时间：2015-11-26 下午4:11:22 
 */
import android.app.Activity;
import android.util.DisplayMetrics;

public final class MeasureUtil {
	public static int[] getScreenSize(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return new int[] { metrics.widthPixels, metrics.heightPixels,(int) metrics.density };
	}

}
