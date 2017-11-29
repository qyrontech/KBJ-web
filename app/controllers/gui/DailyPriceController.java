package controllers.gui;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entities.DailyPrice;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repository.DailyPriceRepo;
import services.PriceService;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class DailyPriceController extends Controller {

    private final DailyPriceRepo dailyPriceRepo;
    private final HttpExecutionContext httpExecutionContext;
    private final PriceService priceService;

    @Inject
    public DailyPriceController(
            DailyPriceRepo dailyPriceRepo
            ,HttpExecutionContext httpExecutionContext
            ,PriceService priceService) {
        this.dailyPriceRepo = dailyPriceRepo;
        this.httpExecutionContext = httpExecutionContext;
        this.priceService = priceService;
    }

    public CompletionStage<Result> priceTrend(String mall, String skuid) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        Date  formNow3Month = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = simpleDateFormat.format(formNow3Month);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = sdf.format(date);

        return priceService.goodsPriceByDate("jd", skuid, "2017-08-01", endDate).thenApplyAsync(goods -> {
            ObjectNode json = Json.newObject();
//            ArrayNode arr = json.arrayNode();
            ArrayNode arrPrice = json.arrayNode();
            ArrayNode arrDate = json.arrayNode();
//            List<DailyPrice> goodsPrice = removeDuplicatePrice(goods);

//            List<DailyPrice> goodsPrice = new ArrayList<>();

//            for (int i = 0; i < goods.size(); i++) {
//                if (goodsPrice.isEmpty()) {
//                    goodsPrice.add(goods.get(0));
//                } else if (!goods.get(i).price.equals(goods.get(i-1).price))  {
//                    goodsPrice.add(goods.get(i));
//                }
//            }

            if (goods.isEmpty()) {
                json.put("results", "null");
            } else {
                for (DailyPrice row: goods) {
                    SimpleDateFormat simpleDate = new SimpleDateFormat("MM-dd");
                    String priceDate = simpleDate.format(row.date);
                    arrDate.add(priceDate);
                    arrPrice.add(row.price);
                }
                json.put("results", "ok");
                json.put("skuid", skuid);
                System.out.println("skuid:" + skuid);
                json.putArray("price").addAll(arrPrice);
                json.putArray("date").addAll(arrDate);
                System.out.println("json:" + json);
            }
            return ok(Json.toJson(json));
        }, httpExecutionContext.current());
    }

//    public List<DailyPrice> removeDuplicatePrice( List<DailyPrice> goods) {
//        Set<DailyPrice> set = new TreeSet<DailyPrice>(new Comparator<DailyPrice>() {
//            @Override
//            public int compare(DailyPrice o1, DailyPrice o2) {
//                return o1.price.compareTo(o2.price);
//            }
//        });
//        set.addAll(goods);
//        return new ArrayList<DailyPrice>(set);
//    }
}