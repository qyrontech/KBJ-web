package solr.params;

import play.libs.F;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Lucene query string(s) for filtering the results without affecting scoring
 * format eg:
 *   fq=+price:[1 TO 1000]&-fq=sales:[1000 TO *]
 * @author jie-z
 * @date 2017/12/01
 */
public class QueryFilter {

    public enum DIRECTION { in, ex;
        public DIRECTION reverse() {
            return (this == in) ? ex : in;
        }
    }

    public QueryFilter(String direct, String field, String from, String to) throws Exception {
        setDirection(direct);
        setField(field);
        setFrom(from);
        setTo(to);
    }

    private DIRECTION direction = DIRECTION.in;
    private String field;
    private String from = "*";
    private String to = "*";

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public void setDirection(String direction) throws Exception {
        if (direction == null || direction.isEmpty()) {
            this.direction = DIRECTION.in;
        } else {
            if (direction.trim().toLowerCase().equals(DIRECTION.in.toString())
                    || direction.trim().equals("+")) {
                this.direction = DIRECTION.in;
            } else if (direction.toLowerCase().equals(DIRECTION.ex.toString())
                    || direction.trim().equals("-")) {
                this.direction = DIRECTION.ex;
            } else {
                throw new Exception("invalid value of QueryFilter.DIRECTION.");
            }
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) throws Exception {
        if (field == null || field.trim().isEmpty()) {
            throw new Exception("invalid value of QueryFilter.DIRECTION.");
        } else {
            this.field = field;
        }

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        if (from == null || from.trim().isEmpty()) {
            this.from = "*";
        } else {
            this.from = from;
        }
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        if (to == null || to.trim().isEmpty()) {
            this.to = "*";
        } else {
            this.to = to;
        }
    }

    @Override
    public String toString() {
        String direct = direction == DIRECTION.in ? "" : "-";
        return direct + field + ":[ " + from + " TO " + to + " ]";
    }

    public static QueryFilter convertFromTuple(F.Tuple4<String, String, String, String> fq)
            throws Exception {
        if (fq!= null) {
            return new QueryFilter(fq._1, fq._2, fq._3, fq._4);
        }
        return null;
    }

    public static List<QueryFilter> convertFromTuple(List<F.Tuple4<String, String, String, String>> filters)
            throws Exception {
        List<QueryFilter> fqs = new ArrayList<>();
        if (filters != null) {
            for (F.Tuple4<String, String, String, String> flts : filters) {
                QueryFilter fq = convertFromTuple(flts);
                if (fq != null) {
                    fqs.add(fq);
                }
            }
        }
        return fqs;
    }
}
