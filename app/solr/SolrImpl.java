package solr;

import com.typesafe.config.Config;
import models.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import play.Logger;
import play.libs.F;
import solr.params.KeywordHelper;
import solr.params.QueryFilter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SolrImpl implements SolrI {

    // TODO
    /*
     * defType	指定用于处理查询语句(参数q的内容)的查询解析器，eg:defType=lucene
     * sort	指定响应的排序方式：升序asc或降序desc.同时需要指定按哪个字段进行排序。eg: sort=price desc,score asc
     * start	指定显示查询结果的开始位置，默认是0
     * rows	指定一次显示多少行查询结果，默认是10
     * fq	指定用于对查询结果进行过滤的过滤器(也看作是一种query) eg: fq=price:[100 To *]&fq=setction:0
     * fl	指定查询结果中返回的字段，该字段的stored=”true”或docValues=”true” ,eg:fl=id,title,product(price, popularity)
     * debug	指定查询结果中携带额外的调试信息：时间信息debug=timing，“explain”信息debug=results,所有调试信息debug=query
     * explainOther	Allows clients to specify a Lucene query to identify a set of documents. If non-blank,the explain info of each document which matches this query, relative to the main query (specified by the q parameter) will be returned along with the rest of the debugging information.
     * timeAllowed	指定查询处理的时间，单位毫秒。如果查询在指定的时间未完成，则只返回部分信息
     * segmentTerminateEarly	Indicates that, if possible, Solr should stop collecting documents from each individual (sorted) segment once it can determine that any subsequent documents in that segment will not be candidates for the rows being returned. The default is false.
     * omitHeader	当设为true时，返回结果不包含头部信息(例如请求花费的时间等信息)，默认是false
     * wt	执行响应的输出格式：xml或json等
     * logParamsList	指定哪些参数需要写入log, eg:logParamsList=q,fq
     * echoParams	指定响应头部包含哪些参数，取值为none/all/explicit(默认值)
     */

    private final static String PRODUCTS = "products";

    private static HttpSolrClient solrClient;
    private static Config config;
    private static KeywordHelper keywordHelper;

    @Inject
    public SolrImpl(Config config, KeywordHelper keywordHelper) {
        this.config = config;
        this.keywordHelper = keywordHelper;
        init();
    }

    protected void init() {
        String server = this.config.getString("solr.server");
        List<String> shards = this.config.getStringList("solr.shards");
        int connectionTimeout = this.config.getInt("solr.connection.timeout");
        int socketTimeout = this.config.getInt("solr.socket.timeout");

        this.solrClient = new HttpSolrClient.Builder(server)
                .withConnectionTimeout(connectionTimeout)
                .withSocketTimeout(socketTimeout)
                .build();
    }

    // TODO
    // should we release the solr connection?

    @Override
    public List<Product> query(String keyword, int start, int rows, String sorter, String fq) {

        SolrQuery query = new SolrQuery();

        query.setQuery(keywordHelper.getQueryString(keyword));
        query.setStart(start);
        if (rows != 0) {
            query.setRows(rows);
        }

        // todo
        Logger.debug("sorter: " + sorter);
        String[] sorters = StringUtils.split(sorter,",");
        for (String sort : sorters) {
            String[] sts = StringUtils.split(sort);
            if (sts.length == 2) {
                String field = sts[0];
                ORDER order = ORDER.asc.toString().equals(sts[1]) ? ORDER.asc : ORDER.desc;
                query.addSort(field, order);
            }
        }

        try {
            List<QueryFilter> qfs = QueryFilter.apply(fq);
            for (QueryFilter qf : qfs) {
                query.addFilterQuery(qf.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.debug(query.toQueryString());

        return doQuery(PRODUCTS, query);
    }

    @Override
    public List<Product> query(String keyword, int start, int rows, List<F.Tuple<String, Integer>> sorters, List<QueryFilter> fqs) {

        SolrQuery query = new SolrQuery();

        query.setQuery(keywordHelper.getQueryString(keyword));
        query.setStart(start);
        if (rows != 0) {
            query.setRows(rows);
        }

        for (F.Tuple<String, Integer> sorter : sorters) {
            SolrQuery.ORDER order = sorter._2 == 1 ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
            query.addSort(sorter._1, order);
        }

        for (QueryFilter fq : fqs) {
            query.addFilterQuery(fq.toString());
        }

        Logger.debug(query.toQueryString());

        return doQuery(PRODUCTS, query);
    }

    // todo
    @Override
    public List<Product> query(String keyword, String shop, int start, int rows, String sort, String fq) {
        Product product = new Product();
        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    // todo
    @Override
    public List<Product> query(List<F.Tuple<String, String>> mallSquidPair, int start, int rows, String sort, String fq) {
        Product product = new Product();
        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    @Override
    public Product query(String mall, String skuid) {
        SolrQuery query = new SolrQuery();
        query.setQuery("mall:" + mall);
        query.setQuery("skuid:" + skuid);

        // todo
        // if there is no result.....
        return doQuery(PRODUCTS, query).get(0);
    }

    // todo
    @Override
    public Product queryByUrl(String url) {
        Product product = new Product();
        return product;
    }

    // todo
    @Override
    public List<Product> queryByName(String name, int start, int rows, String sort) {
        Product product = new Product();
        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    /**
     * query from solr.
     * @param collection
     * @param query
     * @return
     */
    protected List<Product> doQuery(String collection, SolrQuery query) {

        List<Product> products = new ArrayList<>();
        try {
            // get fields which will be return as a query result from conf setting.
            List<String> fields = config.getStringList("solr.query.fl");
            for (String fl : fields) {
                query.addField(fl);
            }

            QueryResponse response = this.solrClient.query(collection, query);
            // todo
            products = response.getBeans(Product.class);

        } catch(SolrServerException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return products;
    }

}
