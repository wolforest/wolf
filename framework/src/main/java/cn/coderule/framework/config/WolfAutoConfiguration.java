package cn.coderule.framework.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * com.wolf.framework.config
 *
 * @author Wingle
 * @since 2020/1/6 6:23 下午
 **/
@Configuration
@ComponentScan("com.wolf.framework")
public class WolfAutoConfiguration {

    /**
     * Fix PageHelper don't work with postgresql
     * by wingle 2024-09-19
     */
    @Bean
    public Interceptor[] plugins() {
        return new Interceptor[]{new PageInterceptor()};
    }

}
