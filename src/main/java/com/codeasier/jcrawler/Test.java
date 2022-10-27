package com.codeasier.jcrawler;

import com.codeasier.jcrawler.defaults.DefaultJsonRequestHandler;
import com.codeasier.jcrawler.defaults.DefaultSyncMultipleRequestCrawler;
import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Method;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.request.component.ParamModifier;

import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception{
        String base_url = "http://172.19.240.224:9001/polls/getSimpleData";
        String token = "ZXlKMGVYQWlPaUpLVjFBaUxDSmhiR2NpT2lKa1pXWmhkV3gwSW4wOjFvbnpJYTp6N0RfNWF4eVQ1bGQtM3VWaDZ4M2hDamlSYzhSTkREcnBlWWpVb2Y5bXc0.ZXlKMWMyVnlibUZ0WlNJNklqRXlNemt3TnpFeU5EbEFjWEV1WTI5dElpd2lhV0YwSWpveE5qWTJPRFl6TXpRd0xqSTBPVFEzTURkOToxb256SWE6NGpTaVFxbWRVZzFDR3hGLW0xNHdfUXN1VzM4XzVZR3FZNUhfY0VWbTdTcw.9d6140b0ca177a9e57b69fc3020ce1c6";

        Header real_header = new Header();
        real_header.putHeader("Authorization",token);

        List<ParamModifier> modifiers = new LinkedList<>();
        for(int i=0;i<5;i++){
            final int page = i+1;
            modifiers.add((header, param) -> param.addUrlParam("page",page+""));
        }


        DefaultSyncMultipleRequestCrawler crawler = new DefaultSyncMultipleRequestCrawler();
        crawler.build(base_url,Method.GET,real_header,new Param())
                .registerDefaultIterator(modifiers);

        DefaultJsonRequestHandler handler = (DefaultJsonRequestHandler) crawler.build(base_url, Method.GET,real_header,new Param()).request();

        System.out.println(handler);
    }
}
