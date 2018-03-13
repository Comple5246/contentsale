package com.netease.comple.contentsale.exceptionhandler;

import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class SimpleExceptionHandler implements HandlerExceptionResolver{
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception exception) {
        // 判断是否ajax请求
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("errorMsg", "系统异常： "+exception.getMessage());
            exception.printStackTrace();
            return new ModelAndView("error", map);
        } else {
            try {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", 400);
                map.put("message", "系统异常： "+exception.getMessage());
                writer.write(JSONUtils.toJSONString(map));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
