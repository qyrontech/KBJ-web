package models.entities;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 *  @author yue-yao
 *  @date 2017/11/28
 */
@Entity
public class CategoryMapping extends BaseModel {

    @Id
    public Long id;

    @Constraints.Required
    public Long kbjCateId;

    @Constraints.Required
    public Long mallCateId;

}
