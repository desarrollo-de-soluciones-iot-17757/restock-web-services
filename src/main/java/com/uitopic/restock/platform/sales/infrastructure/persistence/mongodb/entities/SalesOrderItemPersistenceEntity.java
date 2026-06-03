package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales_order_items")
public class SalesOrderItemPersistenceEntity {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;
}
