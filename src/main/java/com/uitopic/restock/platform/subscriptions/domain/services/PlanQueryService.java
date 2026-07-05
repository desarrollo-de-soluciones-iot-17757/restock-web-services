package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.domain.model.queries.GetPlansQuery;
import java.util.List;

public interface PlanQueryService {
    List<Plan> handle(GetPlansQuery query);
}
