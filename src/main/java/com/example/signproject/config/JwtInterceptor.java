package com.example.signproject.config;

import com.example.signproject.Utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true; // 通过所有OPTION请求
        } else {
            String token = request.getHeader("accessToken"); // 获取请求头中的token
            Map<String, Object> map = new HashMap<>();
            try {
                boolean verify = !JwtUtil.getSubject(token).isEmpty();
                // 未通过验证
                return verify; // 通过验证
            } catch (Exception e) {
                e.printStackTrace();
                map.put("msg", "token无效");
            }
            map.put("state", false);
            // 将map转为json
            String json = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
    }
}