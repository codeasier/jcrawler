package com.codeasier.jcrawler.wrapper;

public interface FileWrapper {
    FileWrapper registerFileWrapProcessor(FileWrapProcessor processor);
    void setEncoding(String encoding);
    void setTargetFilePath(String targetFilePath);
}
