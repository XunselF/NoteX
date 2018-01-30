package com.xunself.notex;

import java.util.Random;

/**
 * Created by XunselF on 2018/1/15.
 */

public class DataTest {
    private static String[] titles = {"中共中央政治局常务委员会召开会议","“三九”时节探供暖：既要暖房子 更要暖民心","工作任务部署比五年前翻一番!中纪委今年反腐要这么干"};
    private static String[] contents = {"央视网消息（新闻联播）：中共中央政治局常务委员会1月15日全天召开会议，听取全国人大常委会、国务院、全国政协、最高人民法院、最高人民检察院党组工作汇报，听取中央书记处工作报告。中共中央总书记习近平主持会议并发表重要讲话。",
    "经济日报讯（记者 倪伟龄）时值数九寒天，哈尔滨市民家里却是暖意融融。入冬以来，哈尔滨市委、市政府把让百姓住上暖屋子作为当前该市最大的民生工程，要求供热企业强化社会责任，开足马力，确保居民家中温度达标。",
            "　1月13日，十九届中央纪委二次全会公报公布。从这份2800多字的公报中，能读出哪些信息？这份公报与以往的中纪委全会公报有何不同？为了进行比较全面的比较，我们找出了十八届中央纪委历次全会的公报和十九届中央纪委一次全会的公报，进行了一个系统的比较，列出了一张表格："};
    private static int[] years = {2016,2017,2018,2019};
    public static void getData(){
        Random random = new Random();
        for (int i = 0 ; i < 20; i++){
            Note note = new Note(titles[random.nextInt(2)],contents[random.nextInt(2)],years[random.nextInt(3)],random.nextInt(10) + 1,random.nextInt(20) + 1);
            note.save();
        }
    }
}
