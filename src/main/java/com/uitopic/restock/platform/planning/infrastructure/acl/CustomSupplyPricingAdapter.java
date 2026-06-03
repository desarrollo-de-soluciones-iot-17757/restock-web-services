package com.uitopic.restock.platform.planning.infrastructure.acl;

import com.uitopic.restock.platform.planning.domain.services.CustomSupplyPricingPort;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Anti-Corruption Layer (ACL) adapter implementing {@link CustomSupplyPricingPort}.
 *
 * <p>Bridges the {@code planning} bounded context with the {@code resources} bounded
 * context via an in-process call to {@link CustomSupplyQueryService} — the public
 * application-service boundary exposed by {@code resources}.</p>
 *
 * <h3>Corrections vs previous version</h3>
 * <ol>
 *   <li><strong>Dependency changed</strong>: injects {@link CustomSupplyQueryService}
 *       instead of the internal {@code CustomSupplyRepository}. The repository is an
 *       infrastructure detail of {@code resources}; importing it from another BC breaks
 *       the bounded-context boundary and causes compilation errors because the real class
 *       lives at {@code …resources.infrastructure.persistence.mongodb.repositories},
 *       not at {@code …resources.domain.repositories}.</li>
 *   <li><strong>Money accessor</strong>: {@code Money} is a Java record that explicitly
 *       declares {@code getAmount()} — so both {@code .getAmount()} and the record
 *       accessor {@code .amount()} work. We use {@code .getAmount()} to match the style
 *       used throughout the rest of the codebase.</li>
 *   <li><strong>Query object</strong>: uses {@link GetCustomSupplyByIdQuery} which
 *       validates the ID in its compact constructor (throws if blank).</li>
 * </ol>
 */
@Slf4j
@Component
public class CustomSupplyPricingAdapter implements CustomSupplyPricingPort {

    private final CustomSupplyQueryService customSupplyQueryService;

    public CustomSupplyPricingAdapter(CustomSupplyQueryService customSupplyQueryService) {
        this.customSupplyQueryService = customSupplyQueryService;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delegates to {@link CustomSupplyQueryService#handle(GetCustomSupplyByIdQuery)}
     * and extracts the {@link BigDecimal} amount from the
     * {@link com.uitopic.restock.platform.shared.domain.model.valueobjects.Money} record
     * via its explicit {@code getAmount()} method.</p>
     *
     * @param customSupplyId the ID of the custom supply in the {@code resources} BC
     * @return the unit price amount, or {@link Optional#empty()} if the supply does not
     *         exist or has no unit price configured
     */
    @Override
    public Optional<BigDecimal> getUnitPrice(String customSupplyId) {
        GetCustomSupplyByIdQuery query = new GetCustomSupplyByIdQuery(customSupplyId);

        return customSupplyQueryService.handle(query)
                .map(cs -> {
                    if (cs.getUnitPrice() == null) {
                        log.warn("CustomSupply '{}' exists but has no unit price configured.",
                                customSupplyId);
                        return null;
                    }
                    return cs.getUnitPrice().getAmount();
                });
    }
}
