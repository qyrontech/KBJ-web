package controllers.gui.manage;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entities.KbjCategory;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.CategoryMapServices;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle connection on kbjCategory and mallCategory
 *  @author yue-yao
 *  @date 2017/11/24
 */
public class CategoryController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private static CategoryMapServices categoryMapServices;

    @Inject
    public CategoryController(FormFactory formFactory,
                              HttpExecutionContext httpExecutionContext,
                              CategoryMapServices categoryMapServices) {
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.categoryMapServices = categoryMapServices;
    }
    
    public CompletionStage<Result> getRootCates() {
        return categoryMapServices.getKbjRootCates().thenApplyAsync(kbjFatherCates -> {
            return ok(views.html.manage.categoryMap.render(kbjFatherCates));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> getLeafCates(String cateRootId) {
        return categoryMapServices.getKbjLeafCates(cateRootId).thenApplyAsync(kbjChildCates -> {

            ArrayNode catesJson = Json.newArray();

            for(KbjCategory kbjChildCate: kbjChildCates) {
                ObjectNode cateJson = Json.newObject();

                cateJson.put("id", kbjChildCate.id);
                cateJson.put("name", kbjChildCate.name);
                catesJson.add(cateJson);
            }

            System.out.println(catesJson);
           return ok( Json.toJson(catesJson));
        }, httpExecutionContext.current());
    }
}
