package org.sqlite.customsqlitetest.benchmark;

import java.util.Random;

public class StringUtil {

    private static final String TABLE = "BEIJING, March 30 (Xinhua) -- The Political Bureau of the Communist Party of China (CPC) Central Committee held a meeting on Tuesday to review a guideline on promoting high-quality development of the country's central region in the new era.\n" +
            "\n" +
            "The meeting was chaired by Xi Jinping, general secretary of the CPC Central Committee. Enditem" +
            "lkjdfgsf来的客服经理sdf恶恶然后hkhlbbaoew删了可地方尽快拿出，" +
            "打发莫妮卡的好看迪卡侬，模拟参加考试的话付款计划客家话" +
            "的旧方法轮廓设计符合ioef，不能每次v了很快‘；了对方考虑【乐然里5" +
            "546654576u地方vu分别走访了asdkfghdugknmbcvkesrt" +
            "可能吧看龙剑打飞机了Kerr托v家0580【中长距离课程bfhgsir" +
            "dgklajrlt风格化反馈嘎哈ui人会认为分库分那边v看fgklsjeto" +
            "地方v奶茶铺i皮特人品trot软件哦然后刚好，从v你，bnhghjllks" +
            "开发的感觉到了法国尽量让他赶紧离开雷锋ljiowe"
            + "This marked the second batch of Zijin Friendship Ambassadors named after the city’s Zijin Mountain, or Purple Mountain. The event also included tree planting at the Nanjing International Friendship Park.\n" +
            "\n" +
            "The new winners included Miro Kolesar, Vice President of the Bank of Nanjing, and Richard Antonie van Ostende, head of Nanjing Office of the Netherlands Center for Trade Promotion. Participants to the event also visited the International Friendship City Exhibition Hall.\n" +
            "\n" +
            "The city started the Zijin Friendship Ambassadors program last year in order to deepen foreigners’ understanding of Chinese culture and the city’s economic and social development. A total of 24 foreigners have been recognized in the program.\n" +
            "\n" +
            "Nanjing has strengthened efforts to turn it into a global city in recent years. It has signed the Memorandum of Understanding on University-Local Cooperation with 16 universities, established 29 overseas collaborative innovation centers with 23 countries, and also kept improving the international talent service.";

//    private static final String TABLE = "abcdefghijkl!mnopqrstuvwxyz;";
    private static final int LEN = TABLE.length();

    private static Random random = new Random();

    public static String randomString() {
        int scope = random.nextInt(200);
        if (scope == 0) {
            return randomString();
        }
        StringBuilder sb = new StringBuilder(scope);
        for (int i = 0; i < scope; i++) {
            int index = random.nextInt(200);
            sb.append(TABLE.charAt(index));
        }
        return sb.toString();
    }
}
