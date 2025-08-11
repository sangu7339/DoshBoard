package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppServices {

    @Autowired
    private AppRepository appRepository;

    public List<OfferBeezDoshBoard> getAllItems() {
        return appRepository.findAll();
    }

    public OfferBeezDoshBoard saveItem(OfferBeezDoshBoard item) {
        return appRepository.save(item);
    }

    public OfferBeezDoshBoard getItemById(int id) {
        Optional<OfferBeezDoshBoard> item = appRepository.findById(id);
        return item.orElse(null);
    }

    public OfferBeezDoshBoard updateItem(OfferBeezDoshBoard item) {
        return appRepository.save(item);
    }

    public void deleteItem(int id) {
        appRepository.deleteById(id);
    }

    public Map<String, Long> getDevStats() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("open", items.stream().filter(i -> "open".equals(i.getInDev())).count());
        stats.put("pass", items.stream().filter(i -> "pass".equals(i.getInDev())).count());
        stats.put("fail", items.stream().filter(i -> "fail".equals(i.getInDev())).count());
        return stats;
    }

    public Map<String, Long> getQAStats() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("open", items.stream().filter(i -> "open".equals(i.getInQA())).count());
        stats.put("pass", items.stream().filter(i -> "pass".equals(i.getInQA())).count());
        stats.put("fail", items.stream().filter(i -> "fail".equals(i.getInQA())).count());
        return stats;
    }

    public Map<String, Long> getLiveStats() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("live", items.stream().filter(OfferBeezDoshBoard::isLive).count());
        stats.put("previous", items.stream().filter(i -> !i.isLive()).count());
        return stats;
    }

    public Map<String, Long> getSeverityStats() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("S1", items.stream().filter(i -> "S1".equals(i.getSeverity())).count());
        stats.put("S2", items.stream().filter(i -> "S2".equals(i.getSeverity())).count());
        stats.put("S3", items.stream().filter(i -> "S3".equals(i.getSeverity())).count());
        return stats;
    }

    public Map<String, Long> getPriorityStats() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        Map<String, Long> stats = new HashMap<>();
        stats.put("P1", items.stream().filter(i -> "P1".equals(i.getPriority())).count());
        stats.put("P2", items.stream().filter(i -> "P2".equals(i.getPriority())).count());
        stats.put("P3", items.stream().filter(i -> "P3".equals(i.getPriority())).count());
        return stats;
    }

    public ChartData getChartData() {
        ChartData data = new ChartData();
        data.setDevStats(getDevStats());
        data.setQaStats(getQAStats());
        data.setLiveStats(getLiveStats());
        data.setSeverityStats(getSeverityStats());
        data.setPriorityStats(getPriorityStats());
        return data;
    }

    public String getOverallStatus() {
        List<OfferBeezDoshBoard> items = appRepository.findAll();
        boolean hasNegative = items.stream().anyMatch(item -> 
            "fail".equals(item.getInDev()) || 
            "fail".equals(item.getInQA()) || 
            "fail".equals(item.getVerifyAndClosed()) ||
            "yes".equals(item.getReopen())
        );
        return hasNegative ? "NEGATIVE" : "POSITIVE";
    }
}
