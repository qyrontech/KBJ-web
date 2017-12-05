package solr;

import com.google.inject.ImplementedBy;
import models.Product;
import play.libs.F;
import solr.params.QueryFilter;
import solr.params.QuerySorter;

import java.util.List;

@ImplementedBy(SolrImpl.class)
public interface SolrI {

    /**
     * find from solr by keyword with the consider of pagination and sort, filter.
     * @param keyword
     * @param start
     * @param rows
     * @param sorter eg: sort=price desc,score asc
     * @param fq  eg: fq=price:[100 To *] fq=section:0
     * @return
     */
    F.Tuple<List<Product>, Long> query(String keyword, int start, int rows, String sorter, String fq);

    /**
     * find from solr by keyword with the consider of pagination and sort, filter.
     * @param keyword
     * @param start
     * @param rows
     * @param sorters eg: [("price", asc), ("score", desc)]
     * @param fqs  eg: [("+", "price", "10", "*"), ("-", "score", "4", "")]
     * @return
     */
    F.Tuple<List<Product>, Long> query(String keyword, int start, int rows,
                                       List<QuerySorter> sorters, List<QueryFilter> fqs);

    /**
     * find from solr with mall specified.
     * @param keyword
     * @param mall
     * @param cate
     * @param start
     * @param rows
     * @param sort
     * @param fq
     * @return
     */
    F.Tuple<List<Product>, Long> query(String keyword, String mall, String cate,
                                       int start, int rows, String sort, String fq);

    /**
     * find from solr by the product skuid and mall.
     * can be used in the hottest items & bargain items area in the top page.
     * for both of which need to query product from solr by id.
     * @param mall
     * @param skuid
     * @return
     */
    Product query(String mall, String skuid);

    /**
     * find by the item url.
     * @param url
     * @return
     */
    Product queryByUrl(String url);

    /**
     * find from solr by the product name.
     * @param name
     * @param start
     * @param rows
     * @param sort
     * @return
     */
    List<Product> queryByName(String name, int start, int rows, String sort);

}
