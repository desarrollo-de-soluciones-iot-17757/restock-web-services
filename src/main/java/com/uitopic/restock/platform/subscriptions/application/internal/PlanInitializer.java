package com.uitopic.restock.platform.subscriptions.application.internal;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.domain.repositories.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Slf4j
@Component
public class PlanInitializer {

    private final PlanRepository planRepository;

    public PlanInitializer(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedPlans() {
        if (planRepository.findAll().isEmpty()) {
            log.info("Seeding subscription plans...");

            Plan starter = Plan.builder()
                    .id("plan_starter")
                    .name("Starter")
                    .description("Ideal for small teams getting started with automated supply restocking.")
                    .price(new BigDecimal("19.00"))
                    .currency("usd")
                    .billingInterval("month")
                    .stripePriceId("price_1StarterPricePlaceholderId")
                    .maxRecipes(10)
                    .maxKits(5)
                    .maxDevices(2)
                    .build();

            Plan pro = Plan.builder()
                    .id("plan_pro")
                    .name("Pro")
                    .description("Designed for growing businesses managing active inventory and devices.")
                    .price(new BigDecimal("49.00"))
                    .currency("usd")
                    .billingInterval("month")
                    .stripePriceId("price_1ProPricePlaceholderId")
                    .maxRecipes(50)
                    .maxKits(25)
                    .maxDevices(5)
                    .build();

            Plan enterprise = Plan.builder()
                    .id("plan_enterprise")
                    .name("Enterprise")
                    .description("Full access and unlimited capabilities for large operations.")
                    .price(new BigDecimal("99.00"))
                    .currency("usd")
                    .billingInterval("month")
                    .stripePriceId("price_1EnterprisePricePlaceholderId")
                    .maxRecipes(-1)
                    .maxKits(-1)
                    .maxDevices(-1)
                    .build();

            planRepository.save(starter);
            planRepository.save(pro);
            planRepository.save(enterprise);
            log.info("Plans seeded successfully.");
        } else {
            log.info("Plans already seeded. Skipping initialization.");
        }
    }
}
