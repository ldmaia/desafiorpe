package br.com.rpe.card;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableFeignClients(basePackages = "br.com.rpe.card.adapters.out.http")
public class CardServiceApplicationConfig {
}
