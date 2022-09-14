package com.sigma.firebolt_api.util;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class HttpUtil {
    public static String find(String property, HttpServletRequest request) {
        try {
            InputStream inputStream = request.getPart(property).getInputStream();
            return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUsername(HttpServletRequest request) {
        return find("username", request);
    }

    public static String getPassword(HttpServletRequest request) {
        return find("password", request);
    }

}
