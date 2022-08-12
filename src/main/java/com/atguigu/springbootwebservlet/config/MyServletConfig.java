package com.atguigu.springbootwebservlet.config;

import com.atguigu.springbootwebservlet.filter.MyFilter;
import com.atguigu.springbootwebservlet.listener.MyListener;
import com.atguigu.springbootwebservlet.servlet.MyServlet;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MyServletConfig {
    //注册Servlet三大组件 在springBoot中，自动注册了springMVC所需要的DisPatchServlet,拦截路径为“/”;
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean<MyServlet> servletRegistrationBean = new ServletRegistrationBean<> (new MyServlet(), "/myServlet");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean<MyFilter> myFilterFilterRegistrationBean = new FilterRegistrationBean<>(new MyFilter());
        myFilterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/hello"));//默认过滤全局路径，指定则用指定的
        return myFilterFilterRegistrationBean;
    }
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean<MyListener> myListenerServletListenerRegistrationBean = new ServletListenerRegistrationBean<>(new MyListener());
        return myListenerServletListenerRegistrationBean;
    }





    //配置嵌入式的Servlet容器，内置的tomcat，可以在这里设置
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
                    @Override
                    public void customize(Connector connector) {
                        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                        // protocol.setMaxConnections(200);
                        // protocol.setMaxThreads(200);
                        // protocol.setSelectorTimeout(3000);
                        protocol.setSessionTimeout(60000);
                        // protocol.setConnectionTimeout(3000);
                    }
                });
            }
        };

    }
}
