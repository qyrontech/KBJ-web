package models.entities.form;

/**
 *  @author yue-yao
 *  @date 2017/11/29
 */
public class BindCategory {
    //映射表id
    public Long mapId;

    //商品品类id
    public Long mallCateId;

    //kbj品类父类
    public Long rootCate;

    //kbj品类子类
    public Long leafCate;

    //商城
    public String mall;

    //商城分类名
    public String name;

    //分类url
    public String link;

    //tag
    public String tag;

    //搜索关键字
    public String keyWord;

    //搜索条件 是否绑定
    public String isBind;

    //更新 是否绑定
    public String bindFlg;

    //是否更新判断
    public String chgFlg;
}
