package com.shiyouao.ansy.service;

import com.shiyouao.ansy.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @Author sya
 * @Date 2019/1/22
 */
@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * 通过，RestTemplate去请求，另外加上类@Async 表明是一个异步任务。
     * 分析：可以卡的前面2个方法分别在GithubLookup-1 和GithubLookup-2执行，第三个在GithubLookup-2执行，
     * 注意因为在配置线程池的时候最大线程为2.如果你把线程池的个数为3的时候，耗时减少。
     * 如果去掉@Async，你会发现，执行这三个方法都在main线程中执行，耗时增加。
     * @param user
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up -->" + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        Thread.sleep(1000L);
        return new AsyncResult<>(results);
    }
}
