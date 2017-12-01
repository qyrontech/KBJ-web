package services;

import models.Product;
import play.libs.F;
import solr.SolrI;
import solr.params.QueryFilter;

import javax.inject.Inject;
import java.util.ArrayList;
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
    public List<Product> query(String keyword, int start, int rows,
                               List<F.Tuple<String, Integer>> sorters,
                               List<F.Tuple4<String, String, String, String>> filters) {
        List<QueryFilter> fqs = new ArrayList<>();

        try {
            fqs = QueryFilter.convertFromTuple(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Product> products = solr.query(keyword, start, rows, sorters, fqs);
        return products;
    }

    public List<Product> query(String keyword, int start, int rows, String sorter, String filter) {
        List<Product> products = solr.query(keyword, start, rows, sorter, filter);
        return products;
    }

}
