package com.example.demo.cosmos.primary;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;

import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ReactiveCosmosTemplate;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.core.convert.MappingCosmosConverter;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import com.example.demo.cosmos.CosmosProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;


@Configuration
public class PrimaryDataSourceConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("primaryDataSourcePropertiesBean")
    CosmosProperties primaryDataSourceProperties;

    @Bean("primaryDataSourcePropertiesBean")
    @ConfigurationProperties(prefix = "azure.cosmos.primary")
    public CosmosProperties primary() {
        return new CosmosProperties();
    }

    @Bean
    public CosmosClientBuilder primaryClientBuilder(@Qualifier("primaryDataSourcePropertiesBean") CosmosProperties primaryDataSourceProperties) {
        return new CosmosClientBuilder()
                .key(primaryDataSourceProperties.getKey())
                .endpoint(primaryDataSourceProperties.getUri());
    }

    @EnableCosmosRepositories(basePackages = "com.example.demo.cosmos.primary")
    public class DataBaseConfiguration extends AbstractCosmosConfiguration {

        @Override
        protected String getDatabaseName() {
            return primaryDataSourceProperties.getDatabase();
        }

        @Override
        public CosmosConfig cosmosConfig() {
            return CosmosConfig.builder()
                               .enableQueryMetrics(primaryDataSourceProperties.isPopulateQueryMetrics())
                               .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                               .build();
        }
   }

    private class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }

}
