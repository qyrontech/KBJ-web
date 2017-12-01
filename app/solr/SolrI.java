package solr;

import com.google.inject.ImplementedBy;
import models.Product;
import play.libs.F;
import solr.params.QueryFilter;

import java.util.List;
import java.util.Map;

@ImplementedBy(SolrImpl.class)
public interface SolrI {

    /**
     * find from solr by keyword with the consider of pagination and sort.
     * @param keyword
     * @param start
     * @param rows
     * @param sorters eg: sort=price desc,score asc
     * @param fqs  eg: fq=price:[100 To *] fq=section:0
     * @return
     */
    List<Product> query(String keyword, int start, int rows, List<F.Tuple<String, Integer>> sorters, List<QueryFilter> fqs);

    /**
     * find from solr with shop specified.
     * @param keyword
     * @param shop
     * @param start
     * @param rows
     * @param sort
     * @param fq
     * @return
     */
    List<Product> query(String keyword, String shop, int start, int rows, String sort, String fq);

    /**
     * find from solr by the array product skuids.
     * can be used in the hottest items & bargain items area in the top page.
     * for both of which need to query product from solr by id.
     * @param mallSquidPair List<(mall, skuid)>
     * @param start
     * @param rows
     * @param sort
     * @param fq
     * @return
     */
    List<Product> query(List<F.Tuple<String, String>> mallSquidPair, int start, int rows, String sort, String fq);

    /**
     * find from solr by the product skuid.
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
