package gildedrose;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhuguowei on 2020/1/11.
 */
public class MyGildedRoseTest {

    @Test
    public void updateSellIn() {
        Item item1 = new Item("name1", 10, 0);
        Item item2 = new Item("name2", 0, 0);
        Item item3 = new Item("name3", -1, 0);
        Item item4 = new Item("Sulfuras, Hand of Ragnaros", 1, 0);
        Item item5 = new Item("Sulfuras, Hand of Ragnaros", 0, 0);
        Item item6 = new Item("Sulfuras, Hand of Ragnaros", -1, 0);
        Item[] items = new Item[]{item1, item2, item3, item4, item5, item6};
        MyGildedRose app = new MyGildedRose(items);

        app.updateSellIn();

        assertEquals(9, app.items[0].sellIn);
        assertEquals(-1, app.items[1].sellIn);
        assertEquals(-2, app.items[2].sellIn);
        assertEquals(1, app.items[3].sellIn);
        assertEquals(0, app.items[4].sellIn);
        assertEquals(-1, app.items[5].sellIn);
    }
    @Test
    public void getReductionFromConfig_normal_item() {
        MyGildedRose app = new MyGildedRose(null);
        // 普通商品
        Item item1 = new Item("name1", 10, 9);
        int reduction = app.getReductionFromConfig(item1);
        assertEquals(1, reduction);
        Item item2 = new Item("name1", 0, 9);
        reduction = app.getReductionFromConfig(item2);
        assertEquals(2, reduction);
        Item item3 = new Item("name1", -1, 9);
        reduction = app.getReductionFromConfig(item3);
        assertEquals(2, reduction);
    }
    @Test
    public void getReductionFromConfig_aged_brie() {
        MyGildedRose app = new MyGildedRose(null);
        // “陈年干酪”（Aged Brie）的品质值随着时间的推移，不减反增
        Item item1 = new Item("Aged Brie", 10, 9);
        int reduction = app.getReductionFromConfig(item1);
        assertEquals(-1, reduction);
        Item item2 = new Item("Aged Brie", 0, 9);
        reduction = app.getReductionFromConfig(item2);
        assertEquals(-1, reduction);
        Item item3 = new Item("Aged Brie", -1, 9);
        reduction = app.getReductionFromConfig(item3);
        assertEquals(-1, reduction);
    }
    @Test
    public void getReductionFromConfig_sulfuras() {
        MyGildedRose app = new MyGildedRose(null);
        // 魔法锤（Sulfuras）是一个传奇商品，其销售剩余天数和品质值都不会变化
        Item item1 = new Item("Sulfuras, Hand of Ragnaros", 10, 9);
        int reduction = app.getReductionFromConfig(item1);
        assertEquals(0, reduction);
        Item item2 = new Item("Sulfuras, Hand of Ragnaros", 0, 9);
        reduction = app.getReductionFromConfig(item2);
        assertEquals(0, reduction);
        Item item3 = new Item("Sulfuras, Hand of Ragnaros", -1, 9);
        reduction = app.getReductionFromConfig(item3);
        assertEquals(0, reduction);
    }
    @Test
    public void getReductionFromConfig_conjured() {
        MyGildedRose app = new MyGildedRose(null);
        // “魔法”（Conjured）商品每日品质下降速度是正常商品的2倍
        Item item1 = new Item("Conjured Mana Cake", 10, 9);
        int reduction = app.getReductionFromConfig(item1);
        assertEquals(2, reduction);
        Item item2 = new Item("Conjured Mana Cake", 0, 9);
        reduction = app.getReductionFromConfig(item2);
        assertEquals(4, reduction);
        Item item3 = new Item("Conjured Mana Cake", -1, 9);
        reduction = app.getReductionFromConfig(item3);
        assertEquals(4, reduction);
    }
    @Test
    public void getReductionFromConfig_backstage_passes() {
        MyGildedRose app = new MyGildedRose(null);
        /**
         * “剧院后台通行证”（Backstage passes），就像陈年干酪一样，其品质值会随着SellIn值的减少而提高
         * — 当离演出开始不足10天时，品质值每日提高2；
         * - 当不足5天时，品质值每日提高3；
         * - 当演出结束后，品质值下降到0
         */
        Item item1 = new Item("Backstage passes to a TAFKAL80ETC concert", 20, 9);
        int reduction = app.getReductionFromConfig(item1);
        assertEquals(-1, reduction);
        Item item2 = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 9);
        reduction = app.getReductionFromConfig(item2);
        assertEquals(-2, reduction);
        Item item3 = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 9);
        reduction = app.getReductionFromConfig(item3);
        assertEquals(-3, reduction);
        Item item4 = new Item("Backstage passes to a TAFKAL80ETC concert", 0, 9);
        reduction = app.getReductionFromConfig(item4);
        assertEquals(50, reduction);
        Item item5 = new Item("Backstage passes to a TAFKAL80ETC concert", 11, 9);
        reduction = app.getReductionFromConfig(item5);
        assertEquals(-1, reduction);
        Item item6 = new Item("Backstage passes to a TAFKAL80ETC concert", 9, 9);
        reduction = app.getReductionFromConfig(item6);
        assertEquals(-2, reduction);
        Item item7 = new Item("Backstage passes to a TAFKAL80ETC concert", 1, 9);
        reduction = app.getReductionFromConfig(item7);
        assertEquals(-3, reduction);
        Item item8 = new Item("Backstage passes to a TAFKAL80ETC concert", -1, 9);
        reduction = app.getReductionFromConfig(item8);
        assertEquals(50, reduction);
    }
    @Test
    public void updateQuality_normal() {
        // 普通商品
        Item item1 = new Item("name1", 10, 9);
        Item item2 = new Item("name2", 0, 8);
        Item item3 = new Item("name3", -1, 1);
        Item[] items = new Item[]{item1, item2, item3};
        MyGildedRose app = new MyGildedRose(items);
        app.updateQuality();
        assertEquals(8, item1.quality);
        assertEquals(6, item2.quality);
        assertEquals(0, item3.quality);
    }
    @Test
    public void updateQuality_Aged_Brie() {
        // “陈年干酪”（Aged Brie）的品质值随着时间的推移，不减反增
        Item item1 = new Item("Aged Brie", 10, 10);
        Item item2 = new Item("Aged Brie", 0, 20);
        Item item3 = new Item("Aged Brie", -1, 50);
        Item[] items = new Item[]{item1, item2, item3};
        MyGildedRose app = new MyGildedRose(items);
        app.updateQuality();
        assertEquals(11, item1.quality);
        assertEquals(21, item2.quality);
        assertEquals(50, item3.quality);
    }
    @Test
    public void updateQuality_Conjured() {
        // “魔法”（Conjured）商品每日品质下降速度是正常商品的2倍
        Item item1 = new Item("Conjured Mana Cake", 10, 10);
        Item item2 = new Item("Conjured Mana Cake", 0, 20);
        Item item3 = new Item("Conjured Mana Cake", -1, 50);
        Item[] items = new Item[]{item1, item2, item3};
        MyGildedRose app = new MyGildedRose(items);
        app.updateQuality();
        assertEquals(8, item1.quality);
        assertEquals(16, item2.quality);
        assertEquals(46, item3.quality);
    }
    @Test
    public void updateQuality_Sulfuras() {
        // 魔法锤（Sulfuras）是一个传奇商品，其销售剩余天数和品质值都不会变化
        Item item1 = new Item("Sulfuras, Hand of Ragnaros", 10, 10);
        Item item2 = new Item("Sulfuras, Hand of Ragnaros", 0, 20);
        Item item3 = new Item("Sulfuras, Hand of Ragnaros", -1, 50);
        Item[] items = new Item[]{item1, item2, item3};
        MyGildedRose app = new MyGildedRose(items);
        app.updateQuality();
        assertEquals(10, item1.quality);
        assertEquals(20, item2.quality);
        assertEquals(50, item3.quality);
    }
    @Test
    public void updateQuality_Backstage_passes() {
        /**
         * “剧院后台通行证”（Backstage passes），就像陈年干酪一样，其品质值会随着SellIn值的减少而提高
         * —当离演出开始不足10天时，品质值每日提高2；
         * - 当不足5天时，品质值每日提高3；
         * - 当演出结束后，品质值下降到0
         */
        Item item1 = new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10);
        Item item2 = new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20);
        Item item3 = new Item("Backstage passes to a TAFKAL80ETC concert", 6, 30);
        Item item4 = new Item("Backstage passes to a TAFKAL80ETC concert", 5, 40);
        Item item5 = new Item("Backstage passes to a TAFKAL80ETC concert", 1, 50);
        Item item6 = new Item("Backstage passes to a TAFKAL80ETC concert", 0, 50);
        Item item7 = new Item("Backstage passes to a TAFKAL80ETC concert", -1, 0);
        Item[] items = new Item[]{item1, item2, item3, item4, item5, item6, item7};
        MyGildedRose app = new MyGildedRose(items);
        app.updateQuality();
        assertEquals(11, item1.quality);
        assertEquals(22, item2.quality);
        assertEquals(32, item3.quality);
        assertEquals(43, item4.quality);
        assertEquals(50, item5.quality);
        assertEquals(0, item6.quality);
        assertEquals(0, item7.quality);
    }
}