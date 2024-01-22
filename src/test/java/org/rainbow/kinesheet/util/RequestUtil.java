package org.rainbow.kinesheet.util;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class RequestUtil {

    private RequestUtil() {
    }

    public static MockHttpServletRequestBuilder setBearerHeader(MockHttpServletRequestBuilder builder, String token) {
        return builder.header("Authorization", "Bearer " + token);
    }

}
