package br.com.rpe.holder;

import br.com.rpe.holder.security.InMemoryUserProperties;
import br.com.rpe.holder.security.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Configuration
@EnableJpaAuditing
@EnableFeignClients(basePackages = "br.com.rpe.holder.adapters.out.http")
@EnableConfigurationProperties({JwtProperties.class, InMemoryUserProperties.class})
public class HolderServiceApplicationConfig {
}
