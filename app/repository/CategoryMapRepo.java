package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.entities.KbjCategory;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;

/**
 * This repository  created to handle category entity
 *  @author yue-yao
 *  @date 2017/11/24
 */
public class CategoryMapRepo {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public CategoryMapRepo(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public List<KbjCategory> findRootCates() {
        return ebeanServer.find(KbjCategory.class).where()
            .eq("parentId", 0)
            .eq("isCrawleTarget", 1)
            .eq("valid", 1).findList();
    }

    public List<KbjCategory> findLeafCates(String cateFatherId) {
        return ebeanServer.find(KbjCategory.class).where()
            .eq("parentId", cateFatherId)
            .eq("isCrawleTarget", 1)
            .eq("valid", 1).findList();
    }
}
