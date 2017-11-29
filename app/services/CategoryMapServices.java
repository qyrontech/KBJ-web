package services;

import models.entities.KbjCategory;
import play.data.FormFactory;
import repository.CategoryMapRepo;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This service created to get datas from entity
 *  @author yue-yao
 *  @date 2017/11/24
 */
public class CategoryMapServices {

    private final CategoryMapRepo categoryMapRepo;
    private final FormFactory formFactory;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public CategoryMapServices(FormFactory formFactory,
                              CategoryMapRepo categoryMapRepo,
                              DatabaseExecutionContext executionContext) {
        this.formFactory = formFactory;
        this.categoryMapRepo = categoryMapRepo;
        this.executionContext = executionContext;

    }

    /**
     * get root category datas
     * @return
     */
    public CompletionStage<List<KbjCategory>> getKbjRootCates() {
        return supplyAsync(() -> {
            return categoryMapRepo.findRootCates();
        }, executionContext);
    }

    /**
     * get leaf category datas
     * @param cateRootId
     * @return
     */
    public CompletionStage<List<KbjCategory>> getKbjLeafCates(String cateRootId) {
        return supplyAsync(() -> {
            return categoryMapRepo.findLeafCates(cateRootId);
        }, executionContext);
    }
}
