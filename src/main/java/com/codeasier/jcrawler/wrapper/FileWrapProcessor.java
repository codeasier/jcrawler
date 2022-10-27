package com.codeasier.jcrawler.wrapper;

public interface FileWrapProcessor {
    void beforeWrap(byte[] responseBytes);
    void afterWrap(byte[] responseBytes,String file_path);
}
