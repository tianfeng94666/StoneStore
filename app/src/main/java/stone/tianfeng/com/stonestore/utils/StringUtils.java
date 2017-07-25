package stone.tianfeng.com.stonestore.utils;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mwqi on 2014/6/8.
 */
public class StringUtils {
	public final static String UTF_8 = "utf-8";



	public static String formatedPrice(Double total) {
		DecimalFormat df = new DecimalFormat("0.00");
		String priceFormatted = df.format(total);
		return priceFormatted;
	}


     /*去除空格*/
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 得到资源文件中的字符串
	 * @param id
	 * @return
	 */
	public static String getString(int id){
		return UIUtils.getContext().getResources().getString(id);
	}
	/**
	 * 得到资源文件中的字符串数组
	 * @param arrayId
	 * @return
	 */
	public static String[] getStringArray(int arrayId){
		return UIUtils.getContext().getResources().getStringArray(arrayId);
	}
	
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}


	/***
	 *
	 */
	public static String removeSpacesUrl(String str){
		//处理后输出:我爱,五星红旗
		String regEx = "[' ']+"; // 一个或多个空格
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("|").trim();
	}

	public static StringBuilder purUrlCut(String key,List<String> list){
		StringBuilder sb = new StringBuilder();
		sb.append("&"+key+"=");
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i);
			sb.append(id);
			if(i!=list.size()-1){
				sb.append("|");
			}
		}
		return sb;
	}
//
//	public void SET(){
//		// ="&"+getGroupKey+"="
//		StringBuffer sb=new StringBuffer();
//		for (Map.Entry<String,List<FilterBean>> entry :  ExpandableGridAdapter.allSelctMap.entrySet()) {
//			String key = entry.getKey();
//			List<FilterBean> list= entry.getValue();
//			if (key.equals(tag)){
//
//			}
//			sb.append("&"+ key+"=");
//			for (int i=0;i<list.size();i++){
//				sb.append(list.get(i).getName());
//				if(i!=list.size()-1){
//					sb.append("|");
//				}
//
//			}
//		}
//		L.e("updateUrl   "+sb.toString());
//	}
//
	/**
	 * 
	 *  Function: 判断多个字符串
	 * 
	 *  @author YangShao 2015年7月16日 下午3:42:32
	 *  @param agrs
	 *  @return
	 */
	public static boolean isEmptys(String... agrs) {
		for (int i = 0; i < agrs.length; i++) {
			if (agrs[i] ==null && "".equalsIgnoreCase(agrs[i].trim()) && "null".equalsIgnoreCase(agrs[i].trim())) {
				return false;
			}
		}
		return true;
	}




	public static String idgui(String s){
		try {
			int changdu = s.getBytes("UTF-8").length;
			if(changdu > 3){
				s = s.substring(s.length()-2, s.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 返回一个高亮spannable
	 * @param content 文本内容
	 * @param color   高亮颜色
	 * @param start   起始位置
	 * @param end     结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * @param resId 文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}
	
	  public static short[] convertToShort(int i)   
	    {   
	        short[] a=new short[2];   
	        a[0] = (short) (i & 0x0000ffff);          //将整型的低位取出,   
	        a[1] = (short) (i >> 16);                     //将整型的高位取出.   
	        return a;   
	    }   
	
	  public static byte[] short2Byte(short a){  
	        byte[] b = new byte[2];  
	        b[0] = (byte) (a >> 8);  
	        b[1] = (byte) (a);  
	        return b;  
	    }  
	  
	  public static byte[] shortToByteArray(short s) {
		  byte[] shortBuf = new byte[2];
		  for(int i=0;i<2;i++) {
		     int offset = (shortBuf.length - 1 -i)*8;
		     shortBuf[i] = (byte)((s>>>offset)&0xff);
		  }
		  return shortBuf;
		 }
	  
	  /** 
	     * 注释：int到字节数组的转换！ 
	     * 
	     * @param number 
	     * @return 
	     */ 
	    @SuppressLint("UseValueOf")
		public static byte[] intToByte(int number) { 
	        int temp = number; 
	        byte[] b = new byte[4]; 
	        for (int i = 0; i < b.length; i++) { 
	            b[i] = new Integer(temp & 0xff).byteValue();// 
	            temp = temp >> 8; // 向右移8位 
	        } 
	        return b; 
	    }
	public static boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
}
