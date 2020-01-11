package gildedrose;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhuguowei
 * @date 2020/1/11
 */
public class MyGildedRose {
    Item[] items;

    /**
     * 默认减少值
     */
    final static int DEFAULT_REDUCTION = 1;
    final static String DEFAULT_REDUCTION_CONFIG = "0:2;2147483647:1";
    final static Map<String, Integer> sellIn2ReductionMap = new HashMap<>();
    final static Map<String, String> quality2ReductionConfigMap = new HashMap<>();
    static {
        /**
         * 魔法锤（Sulfuras）是一个传奇商品，其销售剩余天数和品质值都不会变化
         */
        sellIn2ReductionMap.put("Sulfuras, Hand of Ragnaros", 0);
        quality2ReductionConfigMap.put("Sulfuras, Hand of Ragnaros", "2147483647:0");
        /**
         * “陈年干酪”（Aged Brie）的品质值随着时间的推移，不减反增
         */
        quality2ReductionConfigMap.put("Aged Brie", "0:-2;2147483647:-1");
        /**
         * “魔法”（Conjured）商品每日品质下降速度是正常商品的2倍
         */
        quality2ReductionConfigMap.put("Conjured Mana Cake", "0:4;2147483647:2");
        /**
         * “剧院后台通行证”（Backstage passes），就像陈年干酪一样，其品质值会随着SellIn值的减少而提高
         * —当离演出开始不足10天时，品质值每日提高2；
         * -当不足5天时，品质值每日提高3；
         * -当演出结束后，品质值下降到0
         */
        quality2ReductionConfigMap.put("Backstage passes to a TAFKAL80ETC concert", "0:50;5:-3;10:-2;2147483647:-1");
    }

    /**
     * 商品的品质值不能为负数
     */
    final static int MIN_QUALITY_VALUE = 0;
    /**
     * 商品的品质值永远不会超过50
     */
    final static int MAX_QUALITY_VALUE = 50;

    public MyGildedRose(Item[] items) {
        this.items = items;
    }
    /**
     * 更新剩余天数
     */
    public void updateSellIn() {
        for (Item item : items) {
            Integer reduction = sellIn2ReductionMap.getOrDefault(item.name, DEFAULT_REDUCTION);
            item.sellIn -= reduction;
        }
    }

    /**
     * 更新商品品质
     */
    public void updateQuality() {
        for (Item item : items) {
            int reduction = getReductionFromConfig(item);
            item.quality -= reduction;
            if (item.quality < MIN_QUALITY_VALUE) {
                item.quality = MIN_QUALITY_VALUE;
            } else if (item.quality > MAX_QUALITY_VALUE) {
                item.quality = MAX_QUALITY_VALUE;
            }
        }
    }

    int getReductionFromConfig(Item item) {
        final String config = quality2ReductionConfigMap.getOrDefault(item.name, DEFAULT_REDUCTION_CONFIG);
        int reduction = 0;
        String[] split = config.split(";");
        // 注意： 剩余天数需要从小到大排序
        for (String each : split) {
            String[] sellInReductionPair = each.split(":");
            Integer sellIn = Integer.valueOf(sellInReductionPair[0].trim());
            if (item.sellIn <= sellIn) {
                reduction = Integer.valueOf(sellInReductionPair[1].trim());
                break;
            }
        }
        return reduction;
    }


}
