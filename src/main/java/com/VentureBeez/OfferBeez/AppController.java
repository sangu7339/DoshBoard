package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private AppServices appServices;

    @GetMapping("/")
    public String homepage(Model model, @RequestParam(value = "action", required = false) String action) {
        List<OfferBeezDoshBoard> allItems = appServices.getAllItems();

        // Filter items for different categories
        List<OfferBeezDoshBoard> devItems = allItems.stream()
                .filter(item -> "In Dev".equals(item.getStatus())
                        || "open".equalsIgnoreCase(item.getInDev())
                        || "fail".equalsIgnoreCase(item.getInDev())
                        || "pass".equalsIgnoreCase(item.getInDev())
                        || "pending".equalsIgnoreCase(item.getInDev()))
                .toList();

        List<OfferBeezDoshBoard> qaItems = allItems.stream()
                .filter(item -> "In QA".equals(item.getStatus())
                        || "open".equalsIgnoreCase(item.getInQA())
                        || "fail".equalsIgnoreCase(item.getInQA())
                        || "pass".equalsIgnoreCase(item.getInQA())
                        || "pending".equalsIgnoreCase(item.getInQA()))
                .toList();

        List<OfferBeezDoshBoard> liveItems = allItems.stream().filter(OfferBeezDoshBoard::isLive).toList();
        List<OfferBeezDoshBoard> previousItems = allItems.stream().filter(i -> !i.isLive()).toList();

        // **KEY FIX**: Pass ALL items to the dashboard, not just filtered ones
        // The JavaScript needs to see all data to count properly by categories
        List<OfferBeezDoshBoard> normalizedItems = normalizeDataForCharts(allItems);

        model.addAttribute("devItems", normalizedItems); // Changed from devItems to allItems
        model.addAttribute("qaItems", qaItems);
        model.addAttribute("liveItems", liveItems);
        model.addAttribute("previousItems", previousItems);
        model.addAttribute("overallStatus", appServices.getOverallStatus());
        model.addAttribute("action", action == null ? "" : action);

        // Add separate counts for display
        model.addAttribute("devCount", devItems.size());
        model.addAttribute("qaCount", qaItems.size());
        model.addAttribute("liveCount", liveItems.size());
        model.addAttribute("totalCount", allItems.size());

        return "homepage";
    }

    /**
     * Normalize data to ensure consistent format for JavaScript chart processing
     */
    private List<OfferBeezDoshBoard> normalizeDataForCharts(List<OfferBeezDoshBoard> items) {
        return items.stream().map(item -> {
            // Create a copy to avoid modifying original data
            OfferBeezDoshBoard normalized = new OfferBeezDoshBoard();
            normalized.setId(item.getId());
            normalized.setProject(item.getProject());
            normalized.setBugNumber(item.getBugNumber());
            normalized.setIssue(item.getIssue());
            
            // Normalize severity to lowercase (s1, s2, s3)
            String severity = item.getSeverity();
            if (severity != null) {
                severity = severity.toLowerCase().trim();
                if (severity.contains("critical") || severity.contains("high") && severity.contains("1")) {
                    severity = "s1";
                } else if (severity.contains("high") || severity.contains("medium") && severity.contains("2")) {
                    severity = "s2";
                } else if (severity.contains("medium") || severity.contains("low") || severity.contains("normal")) {
                    severity = "s3";
                }
            }
            normalized.setSeverity(severity);
            
            // Normalize priority to lowercase (p1, p2, p3)
            String priority = item.getPriority();
            if (priority != null) {
                priority = priority.toLowerCase().trim();
                if (priority.contains("urgent") || priority.contains("critical") || priority.contains("1")) {
                    priority = "p1";
                } else if (priority.contains("high") || priority.contains("2")) {
                    priority = "p2";
                } else if (priority.contains("normal") || priority.contains("low") || priority.contains("medium") || priority.contains("3")) {
                    priority = "p3";
                }
            }
            normalized.setPriority(priority);
            
            // Convert boolean/other values to yes/no format
            normalized.setNewUser(convertToYesNo(item.getNewUser()));
            normalized.setPartner(convertToYesNo(item.getPartner()));
            normalized.setAdmin(convertToYesNo(item.getAdmin()));
            normalized.setSales(convertToYesNo(item.getSales()));
            
            normalized.setStatus(item.getStatus());
            normalized.setLive(item.isLive());
            
            // Normalize status values to lowercase
            normalized.setInDev(item.getInDev() != null ? item.getInDev().toLowerCase() : "");
            normalized.setInQA(item.getInQA() != null ? item.getInQA().toLowerCase() : "");
            normalized.setReopen(item.getReopen());
            normalized.setVerifyAndClosed(item.getVerifyAndClosed());
            normalized.setRemarks(item.getRemarks());
            
            return normalized;
        }).toList();
    }

    /**
     * Convert various boolean representations to yes/no
     */
    private String convertToYesNo(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "no";
        }
        
        String lowerValue = value.toLowerCase().trim();
        return ("true".equals(lowerValue) || "1".equals(lowerValue) || "yes".equals(lowerValue) || "y".equals(lowerValue)) 
                ? "yes" : "no";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new OfferBeezDoshBoard());
        return "add";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute OfferBeezDoshBoard item) {
        // Ensure new item goes LIVE
        item.setLive(true);
        item.setStatus("Completed");
        if (item.getInDev() == null || item.getInDev().isBlank()) item.setInDev("pass");
        if (item.getInQA() == null || item.getInQA().isBlank()) item.setInQA("pass");
        if (item.getVerifyAndClosed() == null || item.getVerifyAndClosed().isBlank()) item.setVerifyAndClosed("pass");
        if (item.getReopen() == null || item.getReopen().isBlank()) item.setReopen("no");

        // Ensure category fields are set properly
        if (item.getNewUser() == null || item.getNewUser().isBlank()) item.setNewUser("no");
        if (item.getPartner() == null || item.getPartner().isBlank()) item.setPartner("no");
        if (item.getAdmin() == null || item.getAdmin().isBlank()) item.setAdmin("no");
        if (item.getSales() == null || item.getSales().isBlank()) item.setSales("no");

        appServices.saveItem(item);
        return "redirect:/?action=added";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        OfferBeezDoshBoard item = appServices.getItemById(id);
        if (item == null) return "redirect:/";
        model.addAttribute("item", item);
        return "update";
    }

    @PostMapping("/update")
    public String updateItem(@ModelAttribute OfferBeezDoshBoard item) {
        OfferBeezDoshBoard original = appServices.getItemById(item.getId());
        if (original != null) {
            // Save previous version
            OfferBeezDoshBoard prev = new OfferBeezDoshBoard();
            prev.setProject(original.getProject() + " (Previous)");
            prev.setBugNumber(original.getBugNumber() + "-OLD");
            prev.setIssue(original.getIssue());
            prev.setSeverity(original.getSeverity());
            prev.setPriority(original.getPriority());
            prev.setNewUser(original.getNewUser());
            prev.setPartner(original.getPartner());
            prev.setAdmin(original.getAdmin());
            prev.setSales(original.getSales());
            prev.setStatus(original.getStatus());
            prev.setLive(false);
            prev.setInDev(original.getInDev());
            prev.setInQA(original.getInQA());
            prev.setReopen(original.getReopen());
            prev.setVerifyAndClosed(original.getVerifyAndClosed());
            prev.setRemarks("Previous version of Bug#" + original.getBugNumber());
            appServices.saveItem(prev);
        }

        // Save updated as LIVE
        item.setLive(true);
        item.setStatus("Completed");
        if (item.getInDev() == null || item.getInDev().isBlank()) item.setInDev("pass");
        if (item.getInQA() == null || item.getInQA().isBlank()) item.setInQA("pass");
        if (item.getVerifyAndClosed() == null || item.getVerifyAndClosed().isBlank()) item.setVerifyAndClosed("pass");

        // Ensure category fields are set properly
        if (item.getNewUser() == null || item.getNewUser().isBlank()) item.setNewUser("no");
        if (item.getPartner() == null || item.getPartner().isBlank()) item.setPartner("no");
        if (item.getAdmin() == null || item.getAdmin().isBlank()) item.setAdmin("no");
        if (item.getSales() == null || item.getSales().isBlank()) item.setSales("no");

        appServices.updateItem(item);
        return "redirect:/?action=updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable int id) {
        appServices.deleteItem(id);
        return "redirect:/";
    }
}
