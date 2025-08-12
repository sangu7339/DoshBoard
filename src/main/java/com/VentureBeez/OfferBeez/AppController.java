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

        // Pass normalized data for charts
        List<OfferBeezDoshBoard> normalizedItems = normalizeDataForCharts(allItems);

        model.addAttribute("devItems", normalizedItems);
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
                if (severity.contains("critical") || (severity.contains("high") && severity.contains("1")) || severity.equals("s1")) {
                    severity = "s1";
                } else if (severity.contains("high") || (severity.contains("medium") && severity.contains("2")) || severity.equals("s2")) {
                    severity = "s2";
                } else if (severity.contains("medium") || severity.contains("low") || severity.contains("normal") || severity.equals("s3")) {
                    severity = "s3";
                }
            }
            normalized.setSeverity(severity);
            
            // Normalize priority to lowercase (p1, p2, p3)
            String priority = item.getPriority();
            if (priority != null) {
                priority = priority.toLowerCase().trim();
                if (priority.contains("urgent") || priority.contains("critical") || priority.contains("1") || priority.equals("p1")) {
                    priority = "p1";
                } else if (priority.contains("high") || priority.contains("2") || priority.equals("p2")) {
                    priority = "p2";
                } else if (priority.contains("normal") || priority.contains("low") || priority.contains("medium") || priority.contains("3") || priority.equals("p3")) {
                    priority = "p3";
                }
            }
            normalized.setPriority(priority);
            
            // Convert boolean/other values to yes/no format for charts
            normalized.setNewUser(convertToYesNo(item.getNewUser()));
            normalized.setPartner(convertToYesNo(item.getPartner()));
            normalized.setAdmin(convertToYesNo(item.getAdmin()));
            normalized.setSales(convertToYesNo(item.getSales()));
            
            normalized.setStatus(item.getStatus());
            normalized.setLive(item.isLive());
            
            // Normalize status values to lowercase
            normalized.setInDev(item.getInDev() != null ? item.getInDev().toLowerCase() : "open");
            normalized.setInQA(item.getInQA() != null ? item.getInQA().toLowerCase() : "open");
            normalized.setReopen(item.getReopen() != null ? item.getReopen().toLowerCase() : "no");
            normalized.setVerifyAndClosed(item.getVerifyAndClosed() != null ? item.getVerifyAndClosed().toLowerCase() : "pending");
            normalized.setRemarks(item.getRemarks());
            
            return normalized;
        }).toList();
    }

    /**
     * Convert various boolean representations to yes/no for chart processing
     */
    private String convertToYesNo(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "no";
        }
        
        String lowerValue = value.toLowerCase().trim();
        return ("true".equals(lowerValue) || "1".equals(lowerValue) || "yes".equals(lowerValue) || 
                "y".equals(lowerValue) || "applicable".equals(lowerValue)) 
                ? "yes" : "no";
    }

    /**
     * Convert form values to proper storage format
     */
    private String convertToStorageFormat(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Not Applicable";
        }
        
        String lowerValue = value.toLowerCase().trim();
        if ("yes".equals(lowerValue) || "true".equals(lowerValue) || "1".equals(lowerValue) || "applicable".equals(lowerValue)) {
            return "Applicable";
        } else {
            return "Not Applicable";
        }
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        OfferBeezDoshBoard item = new OfferBeezDoshBoard();
        // Set default values to prevent null issues
        item.setNewUser("no");
        item.setPartner("no");
        item.setAdmin("no");
        item.setSales("no");
        item.setInDev("open");
        item.setInQA("open");
        item.setReopen("no");
        item.setVerifyAndClosed("pending");
        item.setStatus("In Dev");
        item.setLive(true);
        
        model.addAttribute("item", item);
        return "add";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute OfferBeezDoshBoard item) {
        // Ensure new item goes LIVE
        item.setLive(true);
        item.setStatus("Completed");
        
        // Set default values for development status fields
        if (item.getInDev() == null || item.getInDev().isBlank()) item.setInDev("pass");
        if (item.getInQA() == null || item.getInQA().isBlank()) item.setInQA("pass");
        if (item.getVerifyAndClosed() == null || item.getVerifyAndClosed().isBlank()) item.setVerifyAndClosed("pass");
        if (item.getReopen() == null || item.getReopen().isBlank()) item.setReopen("no");

        // Handle category fields - convert form values to storage format
        item.setNewUser(convertToStorageFormat(item.getNewUser()));
        item.setPartner(convertToStorageFormat(item.getPartner()));
        item.setAdmin(convertToStorageFormat(item.getAdmin()));
        item.setSales(convertToStorageFormat(item.getSales()));

        // Ensure required fields are not null
        if (item.getProject() == null || item.getProject().trim().isEmpty()) {
            item.setProject("Unknown Project");
        }
        if (item.getBugNumber() == null || item.getBugNumber().trim().isEmpty()) {
            item.setBugNumber("AUTO-" + System.currentTimeMillis());
        }
        if (item.getIssue() == null || item.getIssue().trim().isEmpty()) {
            item.setIssue("No description provided");
        }

        appServices.saveItem(item);
        return "redirect:/?action=added";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        OfferBeezDoshBoard item = appServices.getItemById(id);
        if (item == null) return "redirect:/";
        
        // Convert storage format back to form format for editing
        if ("Applicable".equals(item.getNewUser())) item.setNewUser("yes");
        else item.setNewUser("no");
        
        if ("Applicable".equals(item.getPartner())) item.setPartner("yes");
        else item.setPartner("no");
        
        if ("Applicable".equals(item.getAdmin())) item.setAdmin("yes");
        else item.setAdmin("no");
        
        if ("Applicable".equals(item.getSales())) item.setSales("yes");
        else item.setSales("no");
        
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

        // Handle category fields - convert form values to storage format
        item.setNewUser(convertToStorageFormat(item.getNewUser()));
        item.setPartner(convertToStorageFormat(item.getPartner()));
        item.setAdmin(convertToStorageFormat(item.getAdmin()));
        item.setSales(convertToStorageFormat(item.getSales()));

        appServices.updateItem(item);
        return "redirect:/?action=updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable int id) {
        appServices.deleteItem(id);
        return "redirect:/";
    }
}
