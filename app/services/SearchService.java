package services;

import models.Product;
import play.libs.F;
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
    public List<Product> query(String keyword, int start, int rows, List<F.Tuple<String, Integer>> sort, String fq) {
        List<Product> products = solr.query(keyword, start, rows, sort, fq);
        return products;
    }

}
