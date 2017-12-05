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

    /**
     * TODO
     * to be remove
     * just for test
     *
     * @return
     */
    public Result index() {

        String keyword = "小米（MI）小 米　家";

        List<F.Tuple<String, String>> sorters = new ArrayList<>();
        sorters.add(new F.Tuple("price", 1));

        List<F.Tuple4<String, String, String, String>> filters = new ArrayList<>();
        filters.add(new F.Tuple4("", "price", "10", ""));

        F.Tuple<List<Product>, Long> products = searcher.query(keyword, 0, 10, sorters, filters);

        Logger.debug("-----------solr: " + products._1.size());
        for (Product product : products._1) {
            Logger.debug(product.getSkuid() + " : " + product.getName() + " : " + product.getPrice());
        }

        return ok(Json.toJson(products));
    }

    public Result generalSearch(String keyword, int start, int rows) {

        // todo
        // for test
        keyword = "小米（MI）小 米　家";
        start = 0;
        rows = 10;

        List<F.Tuple<String, String>> sorters = new ArrayList<>();
        sorters.add(new F.Tuple("price", 1));

        List<F.Tuple4<String, String, String, String>> filters = new ArrayList<>();
        filters.add(new F.Tuple4("", "price", "10", ""));

        F.Tuple<List<Product>, Long> products = searcher.query(keyword, start, rows, sorters, filters);

        Logger.debug("-----------solr: " + products._1.size());
        for (Product product : products._1) {
            Logger.debug(product.getSkuid() + " : " + product.getName() + " : " + product.getPrice());
        }

        return ok(Json.toJson(products));
    }

    public Result generalSearch(String keyword, String start, String rows, String sorter, String filter) {

        F.Tuple<List<Product>, Long> products = searcher.query(keyword, Integer.valueOf(start),
                Integer.valueOf(rows), sorter, filter);
        Logger.debug("-----------solr: " + products._1.size());
        for (Product product : products._1) {
            Logger.debug(product.getSkuid() + " : " + product.getName() + " : " + product.getPrice());
        }

        return ok(Json.toJson(products));
    }

}
