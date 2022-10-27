package com.codeasier.jcrawler.defaults;

import com.codeasier.jcrawler.request.component.Header;
import com.codeasier.jcrawler.request.component.Param;
import com.codeasier.jcrawler.request.component.ParamModifier;
import com.codeasier.jcrawler.request.multiple.UrlIterator;

import java.util.Iterator;
import java.util.List;

public class DefaultUrlIterator extends DefaultUrlBuilder implements UrlIterator {
    private Iterator<ParamModifier> modifierIterator;

    private String base_url;
    private Header header;
    private Param param;

    private boolean same_flag = false;
    private int same_count = 1;

    public DefaultUrlIterator(List<ParamModifier> modifiers) {
        if (modifiers != null) this.modifierIterator = modifiers.listIterator();
        else {
            this.same_flag = true;
            this.same_count = 1;
        }
    }

    @Override
    public String build(String base_url, Header header, Param param) {
        return super.build(base_url, header, param);
    }

    @Override
    public void register(String base_url, Header header, Param param) {
        this.base_url = base_url;
        this.header = header;
        this.param = param;
    }

    @Override
    public boolean hasNext() {
        if (same_flag) return same_count > 0;
        else return modifierIterator != null && modifierIterator.hasNext();
    }

    @Override
    public String next() {
        if (same_flag) same_count--;
        else {
            if (modifierIterator != null && modifierIterator.hasNext()) modifierIterator.next().modify(header, param);
        }
        return build(this.base_url, this.header, this.param);
    }
}
