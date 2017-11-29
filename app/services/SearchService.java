package services;

import models.Product;
import solr.SolrI;

import javax.inject.Inject;
import java.util.List;

public class SearchService {

    private static SolrI solr;
    @Inject
    public SearchService(SolrI solr) {
        this.solr = solr;
    }

    /**
     * TODO
     * to be remove
     * just for test
     * @return
     */
    public List<Product> query() {
        List<Product> products = solr.searchProductByName("小米*",0, "name", 1, "price:[1 TO 1000]");
        return products;
    }

    public List<Product> query(String keyword, int start, int rows, String sort, String fq) {
//        List<Product> products = solr.query(keyword, 0, 10, "", "");
        List<Product> products = solr.searchProductByName(keyword,0, "name", 1, "price:[1 TO 10000]");
        return products;
    }

}
