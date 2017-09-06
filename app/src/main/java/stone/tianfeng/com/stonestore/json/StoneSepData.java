package stone.tianfeng.com.stonestore.json;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class StoneSepData {

    /**
     * ModeSeqno : 1
     * maxweight :
     * minweight :
     * stone : {"colorTitle":"","number":1,"purityTitle":"","shapeId":1,"shapeTitle":"圆形","specId":"1","specSelectTitle":"请参考输入0到0.5之间的规格","specTitle":"0.5","typeId":1,"typeTitle":"钻石"}
     * stoneA : {"colorTitle":"","number":32,"purityTitle":"","shapeId":1,"shapeTitle":"圆形","specId":"14","specSelectTitle":"请参考输入0到0.5之间的规格","specTitle":"0.108","stoneOut":0,"typeId":2,"typeTitle":"碎钻"}
     * stoneB : {"colorTitle":"","purityTitle":"","specId":0,"specSelectTitle":"请输入的规格","specTitle":"","stoneOut":0}
     * stoneC : {"colorTitle":"","purityTitle":"","specId":0,"specSelectTitle":"请输入的规格","specTitle":"","stoneOut":0}
     * title : 50'
     */

    private int ModeSeqno;
    private String maxweight;
    private String minweight;
    private StoneEntity stone;
    private ModelDetailResult.DataEntity.ModelEntity.StoneAEntity stoneA;
    private ModelDetailResult.DataEntity.ModelEntity.StoneBEntity stoneB;
    private ModelDetailResult.DataEntity.ModelEntity.StoneCEntity stoneC;
    private String title;

    public int getModeSeqno() {
        return ModeSeqno;
    }

    public void setModeSeqno(int ModeSeqno) {
        this.ModeSeqno = ModeSeqno;
    }

    public String getMaxweight() {
        return maxweight;
    }

    public void setMaxweight(String maxweight) {
        this.maxweight = maxweight;
    }

    public String getMinweight() {
        return minweight;
    }

    public void setMinweight(String minweight) {
        this.minweight = minweight;
    }

    public StoneEntity getStone() {
        return stone;
    }

    public void setStone(StoneEntity stone) {
        this.stone = stone;
    }

    public ModelDetailResult.DataEntity.ModelEntity.StoneAEntity  getStoneA() {
        return stoneA;
    }

    public void setStoneA(ModelDetailResult.DataEntity.ModelEntity.StoneAEntity  stoneA) {
        this.stoneA = stoneA;
    }

    public ModelDetailResult.DataEntity.ModelEntity.StoneBEntity getStoneB() {
        return stoneB;
    }

    public void setStoneB(ModelDetailResult.DataEntity.ModelEntity.StoneBEntity stoneB) {
        this.stoneB = stoneB;
    }

    public ModelDetailResult.DataEntity.ModelEntity.StoneCEntity getStoneC() {
        return stoneC;
    }

    public void setStoneC(ModelDetailResult.DataEntity.ModelEntity.StoneCEntity stoneC) {
        this.stoneC = stoneC;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
