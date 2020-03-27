package ru.romanov.durak.config;

import lombok.RequiredArgsConstructor;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.romanov.durak.util.ExceptionTranslator;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class DatabaseConfig {

    private final DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultDSLContext context() {
        DefaultConfiguration configuration = new DefaultConfiguration();
        configuration.set(connectionProvider());
        configuration.set(new DefaultExecuteListenerProvider(exceptionTranslator()));
        return new DefaultDSLContext(configuration);
    }

    @Bean
    public ExceptionTranslator exceptionTranslator() {
        return new ExceptionTranslator();
    }

}
