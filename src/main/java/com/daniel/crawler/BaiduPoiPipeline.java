package com.daniel.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daniel.common.CrawlerConstant;
import com.daniel.common.CrawlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 百度地图POI数据持久化
 *
 * @author lingengxiang
 * @date 2018/12/11 10:22
 */
public class BaiduPoiPipeline implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(BaiduPoiPipeline.class);

    /**
     * 一级分类
     */
    private String topCategory;

    /**
     * 二级分类
     */
    private String subCategory;

    /**
     * 文件存储路径
     */
    private String path;

    /**
     * 目标区域
     */
    private String region;

    public BaiduPoiPipeline() {
    }

    public BaiduPoiPipeline(String topCategory, String subCategory, String path, String region) {
        this.topCategory = topCategory;
        this.subCategory = subCategory;
        this.path = path;
        this.region = region;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    /**
     * 负责处理数据JSON数据，并写入文件之中
     *
     * @param resultItems 结果集
     * @param task        爬虫任务
     */
    @Override
    public void process(ResultItems resultItems, Task task) {

        // 开始执行写入任务
        logger.info("get page from:{}", resultItems.getRequest().getUrl());
        File file = new File(path);
        // 如果文件不存在，则先创建新文件
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            JSONArray results = JSON.parseArray(resultItems.get(CrawlerConstant.RESULTS));
            for (int i = 0; i < results.size(); i++) {
                JSONObject result = results.getJSONObject(i);
                printWriter.println(CrawlerUtil.parseData(this.topCategory, this.subCategory, result));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
