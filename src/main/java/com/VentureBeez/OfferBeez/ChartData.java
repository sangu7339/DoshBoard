package com.VentureBeez.OfferBeez;

import lombok.Data;
import java.util.Map;

@Data
public class ChartData {
    private Map<String, Long> devStats;
    private Map<String, Long> qaStats;
    private Map<String, Long> liveStats;
    private Map<String, Long> severityStats;
    private Map<String, Long> priorityStats;
}
