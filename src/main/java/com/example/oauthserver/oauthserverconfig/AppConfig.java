package com.example.oauthserver.oauthserverconfig;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.oauthserver.properties.OAuthServerProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yls
 * @date 2019/4/15 17:18
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private OAuthServerProperties oAuthServerProperties;

	/**
	 * 分页插件，自动识别数据库类型 多租户，请参考官网【插件扩展】
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		// 攻击 SQL 阻断解析器、加入解析链
		sqlParserList.add(new BlockAttackSqlParser());
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}

	/**
	 * 打印sql 上线关闭
	 */
	@Bean
	@Profile({"dev"})
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}


	
	/*
	 * @Bean
	 * 
	 * @Profile({"prod", "test"}) public SqlSessionFactory
	 * sqlSessionFactory(DataSource dataSource) throws Exception {
	 * MybatisSqlSessionFactoryBean factoryBean = new
	 * MybatisSqlSessionFactoryBean(); factoryBean.setDataSource(dataSource); return
	 * factoryBean.getObject(); }
	 */

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
				.maxAge(3600);
	}

}
