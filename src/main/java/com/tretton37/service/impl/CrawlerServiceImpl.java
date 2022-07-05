package com.tretton37.service.impl;

import com.tretton37.config.UrlProperties;
import com.tretton37.response.CrawlResponse;
import com.tretton37.service.CrawlerService;
import com.tretton37.utils.CrawlingUtils;
import org.springframework.stereotype.Service;

@Service
public class CrawlerServiceImpl implements CrawlerService {
    private final CrawlingUtils crawlingUtils;
    private final UrlProperties urlProperties;

    public CrawlerServiceImpl(CrawlingUtils crawlingUtils, UrlProperties urlProperties) {
        this.crawlingUtils = crawlingUtils;
        this.urlProperties = urlProperties;
    }

    @Override
    public CrawlResponse crawl() {
        return crawlingUtils.crawlResource(urlProperties.getUrl());
    }
}