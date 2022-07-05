package com.tretton37.controller;

import com.tretton37.response.CrawlerResponse;
import com.tretton37.service.CrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping()
    public ResponseEntity<CrawlerResponse> crawl() {
        CrawlerResponse crawlerResponse = crawlerService.crawl();
        return ResponseEntity.ok().body(crawlerResponse);
    }
}