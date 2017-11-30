package controllers.api;

import models.Product;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import services.SearchService;

import javax.inject.Inject;
import java.util.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private static SearchService searcher;
    @Inject
    public HomeController(SearchService searcher) {
        this.searcher = searcher;
    }

    public Result index() {

        // TODO
        // to be remove
        // just for test
//        List<Product> products = searcher.query();
//        List<Product> products = searcher.query("罗技", 0, 10, "", "");
//        List<Product> products = searcher.query("小 (米*", 0, 10, "", "");
//        List<Product> products = searcher.query("小 米", 0, 10, "", "");

        List<F.Tuple<String, Integer>> sorters = new ArrayList<>();
        sorters.add(new F.Tuple("price", 1));
        List<Product> products = searcher.query("小米（MI）小 米　家", 0, 10, sorters, "");
        Logger.debug("-----------solr: " + products.size());
        for (Product product : products) {
            Logger.debug(product.getSkuid() + " : " + product.getName() + " : " + product.getPrice());
        }

        return ok(Json.toJson(products));
    }

}
