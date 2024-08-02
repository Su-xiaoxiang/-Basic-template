package com.example.classmarangethouduan.fitter;


import com.alibaba.fastjson.JSONObject;
import com.example.classmarangethouduan.pojo.Result;
import com.example.classmarangethouduan.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginFitter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
        * 强制转换
        * */
         HttpServletRequest sreq =(HttpServletRequest) servletRequest;
         HttpServletResponse srep=(HttpServletResponse) servletResponse;
          /*
          * 获取请求的url
          * */
         String url=sreq.getRequestURL().toString();
         log.info("获取到的url为: "+url);
          //判断是否为登录操作
         if (url.contains("login")){
             log.info("登陆操作..成功放行");
             filterChain.doFilter(servletRequest,servletResponse);
             return;
         }
        //判断是否为注册操作
        if (url.contains("register")){
            log.info("注册操作...成功放行");
            filterChain.doFilter(servletRequest,servletResponse);
           return;
        }
        /*
        * 判断是否含有tonken或者token是否有效
        * */
         String token=sreq.getHeader("token");
         if ( !StringUtils.hasLength(token)){
             log.info("token不正确或已经过期,返回登录页面");
             Result error =Result.error("NOT_LOGIN");
             //将错误信息转换为json形式
             String notlogin= JSONObject.toJSONString(error);
             srep.getWriter().write(notlogin);
             return;
         }

         /*
         *使用try catch进行解析
         * */
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析失败");
            Result error=Result.error("NOT_LOGIN");
            String notlogin=JSONObject.toJSONString(error);
            srep.getWriter().write(notlogin);
            return;
        }
          /*
          * 解析成功后进行放行
          * */
        log.info("放行");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
