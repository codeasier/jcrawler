package com.codeasier.jcrawler.crawler.result;

import com.codeasier.jcrawler.response.holder.ResponseHolder;

import java.util.List;

public interface MultipleCrawlerResultHandler<T extends ResponseHolder> {
    MultipleCrawlerResultHandler<T> registerHolders(List<T> holders)throws Exception;
    Object handleHolderList(List<T> holders)throws Exception;
}