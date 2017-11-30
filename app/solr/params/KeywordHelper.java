package solr.params;

import com.typesafe.config.Config;
import org.apache.solr.client.solrj.util.ClientUtils;
import play.Logger;

import javax.inject.Inject;
import java.util.List;

public class KeywordHelper {

    private static Config config;
    @Inject
    public KeywordHelper(Config config) {
        this.config = config;
    }

    /**
     * get fields which will be return as a query result from conf setting.
     * @return
     */
    public static List<String> getQueryResponseFields() {
        List<String> fields = config.getStringList("solr.query.fl");
        return fields;
    }

    /**
     * get target fields which a query will query from from conf setting.
     * @return
     */
    public static List<String> getQueryTargetFields() {
        List<String> fromFields = config.getStringList("solr.query.from.fl");
        if (fromFields.size() == 0) {
            fromFields.add("*");
        }
        return fromFields;
    }

    /**
     *
     * @param keyword
     * @return
     */
    public static String getQueryString(String keyword) {
        Logger.debug("origin keyword: " + keyword);

        List<String> fromFields = getQueryTargetFields();
        String[] kws = splitBySpaces(keyword);

        int i = 0;
        String escaped;
        StringBuilder sb = new StringBuilder();
        for (String fl : fromFields) {
            if (kws.length == 0) {
                sb.append(fl + ":*");
            } else {
                sb.append("(");
                int j = 0;
                for (String kw : kws) {
                    // todo
                    // must test for
                    // blanks in middle, head, and tail.
                    escaped = escapeSpecialChars(kw.trim());
                    Logger.debug("escaped keyword: " + escaped);
                    sb.append(fl + ":*" + escaped + "*");
                    if ( j != kws.length - 1) {
                        // todo
//                        sb.append(" AND ");
                        sb.append(" OR ");
                    }
                    j++;
                }
                sb.append(")");

                if ( i != fromFields.size() - 1) {
                    sb.append(" OR ");
                }
                i++;
            }
        }

        Logger.debug("after keyword: " + sb.toString());
        return sb.toString();
    }

    /**
     * notice:
     *   both the full-width and half-width spaces should be considered
     *   as separator.
     * @param keyword
     * @return
     */
    public static String[] splitBySpaces(String keyword) {
        return keyword.split("(　|\\s)+");
    }

    /**
     * notice:
     * the special characters in solr is
     *  + – && || ! ( ) { } [ ] ^ " ~ * ? : \ and [space].
     * should be converted in advance.
     * @param keyword
     * @return
     */
    public static String escapeSpecialChars(String keyword) {
        return ClientUtils.escapeQueryChars(keyword);
    }

}
