package com.codeasier.jcrawler.crawler.result;

import com.codeasier.jcrawler.response.holder.ResponseHolder;

public interface SingleCrawlerResultHandler<T extends ResponseHolder> {
    SingleCrawlerResultHandler registerResult(T holder)throws Exception;
    Object handleResultHolder(T holder)throws Exception;
}
