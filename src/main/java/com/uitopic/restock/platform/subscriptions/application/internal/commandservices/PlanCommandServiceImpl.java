package com.uitopic.restock.platform.subscriptions.application.internal.commandservices;

import com.uitopic.restock.platform.subscriptions.domain.model.commands.SeedSubscriptionPlansCommand;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlanCatalog;
import com.uitopic.restock.platform.subscriptions.domain.repositories.PlanRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.PlanCommandService;
import com.uitopic.restock.platform.subscriptions.application.internal.outboundservices.payments.PlanPaymentReferencesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanCommandServiceImpl implements PlanCommandService {
    private final PlanRepository planRepository;
    private final PlanPaymentReferencesProvider paymentReferencesProvider;

    public PlanCommandServiceImpl(
            PlanRepository planRepository,
            PlanPaymentReferencesProvider paymentReferencesProvider
    ) {
        this.planRepository = planRepository;
        this.paymentReferencesProvider = paymentReferencesProvider;
    }

    @Override
    public void handle(SeedSubscriptionPlansCommand command) {
        log.info("Starting subscription plans seed process");
        SubscriptionPlanCatalog.predefinedPlans().stream()
                .map(paymentReferencesProvider::attachReferences)
                .forEach(this::upsert);
        log.info("Subscription plans seed process finished successfully");
    }

    private void upsert(SubscriptionPlan catalogPlan) {
        var planToSave = planRepository.findByType(catalogPlan.type())
                .map(existing -> existing.updateFromCatalog(catalogPlan))
                .orElse(catalogPlan);

        planRepository.save(planToSave);
    }
}
