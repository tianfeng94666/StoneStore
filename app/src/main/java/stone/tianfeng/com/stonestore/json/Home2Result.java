package stone.tianfeng.com.stonestore.json;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Home2Result {
    /**
     * response :
     * error : 0
     * message :
     * data : {"scrollAd":[{"key":"scrollAd_1","pic":"http://appapi2.fanerweb.com/images/index/scrollAd-1.jpg"},{"key":"scrollAd_2","pic":"http://appapi2.fanerweb.com/images/index/scrollAd-2.jpg"},{"key":"scrollAd_3","pic":"http://appapi2.fanerweb.com/images/index/scrollAd-3.jpg"}],"classAd_1":[{"key":"classAd_1_1","pic":"http://appapi2.fanerweb.com/images/index/classAd_1_1.jpg"},{"key":"classAd_1_2","pic":"http://appapi2.fanerweb.com/images/index/classAd_1_2.jpg"},{"key":"classAd_1_3","pic":"http://appapi2.fanerweb.com/images/index/classAd_1_3.jpg"},{"key":"classAd_1_4","pic":"http://appapi2.fanerweb.com/images/index/classAd_1_4.jpg"}],"classAd_2":[{"key":"classAd_2_1","pic":"http://appapi2.fanerweb.com/images/index/classAd_2_1.jpg"},{"key":"classAd_2_2","pic":"http://appapi2.fanerweb.com/images/index/classAd_2_2.jpg"},{"key":"classAd_2_3","pic":"http://appapi2.fanerweb.com/images/index/classAd_2_3.jpg"},{"key":"classAd_2_4","pic":"http://appapi2.fanerweb.com/images/index/classAd_2_4.jpg"}]}
     */

    private String response;
    private int error;
    private String message;
    private DataBean data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<Pictures> scrollAd;
        private List<Pictures> classAd_1;
        private List<Pictures> classAd_2;

        public List<Pictures> getScrollAd() {
            return scrollAd;
        }

        public void setScrollAd(List<Pictures> scrollAd) {
            this.scrollAd = scrollAd;
        }

        public List<Pictures> getClassAd_1() {
            return classAd_1;
        }

        public void setClassAd_1(List<Pictures> classAd_1) {
            this.classAd_1 = classAd_1;
        }

        public List<Pictures> getClassAd_2() {
            return classAd_2;
        }

        public void setClassAd_2(List<Pictures> classAd_2) {
            this.classAd_2 = classAd_2;
        }






    }
}
