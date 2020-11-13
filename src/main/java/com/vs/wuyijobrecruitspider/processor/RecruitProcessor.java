package com.vs.wuyijobrecruitspider.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.pipeline.SpringDataPipeline;
import com.vs.wuyijobrecruitspider.util.SalaryConvertor;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class RecruitProcessor implements PageProcessor {
    @Autowired
    private SpringDataPipeline springDataPipeline;

    private int pageNum = 1;
    private String url = "https://search.51job.com/list/000000,000000,0100%252c7700%252c7200%252c7900%252c2700,00,9,99,+,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-");
    private String today = formatter.format(new Date());
    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(8000)
            .setRetrySleepTime(2000)
            .setRetryTimes(3);

    @Override
    public void process(Page page) {
        // 解析页面, 获取script代码中的招聘详情信息
        String dataJs = page.getHtml().css("script").regex(".*SEARCH_RESULT.*").get();
        // 判断获取到的页面是否为空
        if (!StringUtils.isEmpty(dataJs)) {
            System.out.println("开始抓取第" + pageNum + "页");
            // 如果不为空, 表示这是列表页
            // 解析拿到json字符串
            dataJs = dataJs.substring(dataJs.indexOf("{"), dataJs.lastIndexOf("}") + 1);
            // 创建json对象
            JSONObject jsonObject = (JSONObject) JSONObject.parse(dataJs);
            // 根据分析拿到放置信息的数组
            JSONArray resArr = jsonObject.getJSONArray("engine_search_result");
            // 判断数组中是否存在数据
            if (resArr.size() > 0) {
                for (int i = 0; i < resArr.size(); i++) {
                    // 获取数组中的每一个对象
                    JSONObject resObj = (JSONObject) resArr.get(i);
                    //把获取到的url地址放到任务队列中
                    page.addTargetRequest(String.valueOf(resObj.get("job_href")));
                }
                if (pageNum < 402) {
                    // 获取下一页的url
                    String bkUrl = "https://search.51job.com/list/000000,000000,0100%252c7700%252c7200%252c7900%252c2700,00,9,99,+,2," + (++pageNum) + ".html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
                    // 把url放到任务队列中
                    page.addTargetRequest(bkUrl);
                }
            } else {
                // 设置变量为初始值
                pageNum = 0;
                // 如果没有数据那么爬虫结束
                return;
            }
        } else {
            // 如果为空, 表示这是招聘详情页, 解析页面, 获取招聘详情信息, 保存数据
            saveJobInfo(page);
        }
    }

    private void saveJobInfo(Page page) {
        // System.out.println(page.getHtml().toString());
        // 创建招聘详情对象
        JobInfo jobInfo = new JobInfo();

        // 解析页面
        Html html = page.getHtml();

        // 获取数据, 封装到对象中
        // 有些为空导致空string调用了toString，报错
        // 薪水大小反了
        String descObj = html.css("p.ltype").toString();
        if (descObj == null) {
            // 有极少数非51内部网站无法找到
            return;
        }
        String desc = Jsoup.parse(descObj).text();

        desc = desc.substring(0, desc.lastIndexOf("发布"));
        // 设置 jobInfo 对象
        jobInfo.setCompanyName(html.css("div.cn p.cname a", "text").toString().trim());
        jobInfo.setCompanyAddr(desc.substring(0, desc.indexOf("|")).trim());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text().trim());
        jobInfo.setJobName(html.css("div.cn h1", "text").toString());
        jobInfo.setJobAddr(html.css("div.bmsg>p.fp", "text").toString());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text().trim());
        jobInfo.setUrl(page.getUrl().toString());

        // 获取薪资
        // 有的没有写薪资, 先获取薪资的字符串
        String salaryText = html.css("div.cn strong", "text").toString();
        // 看看是否没有薪资这个字段
        if (!StringUtils.isEmpty(salaryText)) {
            // 使用工具类转换薪资字符串
            Integer[] salary = SalaryConvertor.getSalary(salaryText);
            jobInfo.setSalaryMax(salary[1]);
            jobInfo.setSalaryMin(salary[0]);
        } else {
            // 没有则设为零
            jobInfo.setSalaryMax(0);
            jobInfo.setSalaryMin(0);
        }
        // 获取发布时间
        String time = desc.substring(desc.lastIndexOf("|") + 3);
        jobInfo.setTime(today + time.trim());
        //System.out.println(jobInfo);
        // 把结果保存到 ResultItems 中
        page.putField("jobInfo", jobInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }

    // 设置定时启动
    @Scheduled(initialDelay = 1000, fixedDelay = 1000000 * 1000)
    public void process() {
        Spider.create(new RecruitProcessor())
                .addUrl(url)
                // 设置Secheduler
                .setScheduler(new QueueScheduler()
                        // 设置Bloom去重
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
                .thread(100)
                // 设置自定义的Pipeline储存数据
                .addPipeline(springDataPipeline)
                .run();
    }
}
