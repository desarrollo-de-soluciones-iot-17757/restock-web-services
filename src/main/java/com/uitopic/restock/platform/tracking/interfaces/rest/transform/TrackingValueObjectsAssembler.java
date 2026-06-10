package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.HumidityRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TemperatureRecord;

/**
 * Assembler class responsible for converting raw data from the request into value objects used in the tracking domain.
 */
public final class TrackingValueObjectsAssembler {

    /**
     * Converts a Double value to a StockRecord value object.
     *
     * @param stockValue the stock value to convert, provided by the request
     * @return a StockRecord value object representing the stock value
     */
    public static StockRecord toStockRecordFromNumber(Double stockValue) {
        if (stockValue == null) {
            throw new IllegalArgumentException("Stock value cannot be null");
        }
        if (stockValue < 0) {
            throw new IllegalArgumentException("Stock value cannot be negative");
        }
        return new StockRecord(stockValue);
    }

    /**
     * Converts a Double value to a TemperatureRecord value object.
     *
     * @param temperatureValue the temperature value to convert, provided by the request
     * @return a TemperatureRecord value object representing the temperature value
     */
    public static TemperatureRecord toTemperatureRecordFromNumber(Double temperatureValue) {
        if (temperatureValue == null) {
            throw new IllegalArgumentException("Temperature value cannot be null");
        }
        return new TemperatureRecord(temperatureValue);
    }

    /**
     * Converts a Double value to a HumidityRecord value object.
     *
     * @param humidityValue the humidity value to convert, provided by the request
     * @return a HumidityRecord value object representing the humidity value
     */
    public static HumidityRecord toHumidityRecordFromNumber(Double humidityValue) {
        if (humidityValue == null) {
            throw new IllegalArgumentException("Humidity value cannot be null");
        }
        return new HumidityRecord(humidityValue);
    }
}
