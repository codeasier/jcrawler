package com.codeasier.jcrawler.wrapper;

import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.List;

public interface JsonWrapper {
     JsonWrapper registerProcessor(JsonWrapProcessor processor);

     default void doProcessWrapBefore(List<JsonWrapProcessor> processors, JSONObject object){
          if(processors.isEmpty())return;
          Iterator<JsonWrapProcessor> iterator = processors.listIterator();
          while (iterator.hasNext()){
               JsonWrapProcessor processor = iterator.next();
               processor.beforeWrap(object);
          }
     }

     default void doProcessWrapAfter(List<JsonWrapProcessor> processors,JSONObject object,Object wrappedObject){
          if(processors.isEmpty())return;
          Iterator<JsonWrapProcessor> iterator = processors.listIterator();
          while (iterator.hasNext()){
               JsonWrapProcessor processor = iterator.next();
               processor.afterWrap(object,wrappedObject);
          }
     }

     <T> T wrapJsonToEntity(Class<T> targetClass);
}
