package com.wolf.httpclient.cookie;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.common.util.collection.ListUtil;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.httpclient.HttpRequestBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CookiePolicy {
    public static List<Cookie> match(CookiePolicyEnum policy, @NonNull HttpRequestBuilder request, List<Cookie> cookies) {
        if (CookiePolicyEnum.ACCEPT_ALL.equals(policy)) {
            return cookies;
        }

        if (null == policy
                || CookiePolicyEnum.ACCEPT_NONE.equals(policy)
                || CollectionUtil.isEmpty(cookies)) {
            return ListUtil.empty();
        }

        return matchDomain(request, cookies);
    }

    public static List<Cookie> matchDomain(HttpRequestBuilder request, List<Cookie> cookies) {
        List<Cookie> result = new ArrayList<>();
        if (null == request || StringUtil.isBlank(request.getUrl())) {
            return result;
        }
        try {
            URL url = new URL(request.getUrl());
            String domain = url.getHost();
            String query = url.getQuery();

            for (Cookie cookie : cookies) {
                if (cookie.domainMatches(domain, query)) {
                    result.add(cookie);
                }
            }

        } catch (MalformedURLException e) {
            log.error("invalid request url", e);
            return result;
        }

        return result;
    }
}
