package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.VentureBeez.OfferBeez.util.Enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private AppServices appServices;

    @GetMapping("/")
    public String homePage(Model model) {
        List<OfferBeezDashBoard> inDevBugs = appServices.getBugsByStatus(Enums.Stage.DEV);
        List<OfferBeezDashBoard> inQaBugs = appServices.getBugsByStatus(Enums.Stage.QA);
        List<OfferBeezDashBoard> resolvedBugs = appServices.getBugsByStatus(Enums.Stage.RESOLVED);

        int totalLive = inDevBugs.size() + inQaBugs.size();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("inDev", inDevBugs.size());
        stats.put("inQA", inQaBugs.size());
        stats.put("totalLive", totalLive);
        stats.put("resolved", resolvedBugs.size());

        // Create enhanced chart data for severity and priority by user type
        Map<String, Object> chartData = createEnhancedChartData(inDevBugs, inQaBugs, resolvedBugs);
        
        // Create total chart data (all bugs combined)
        List<OfferBeezDashBoard> allBugs = appServices.getAllBugs();
        Map<String, Object> totalChartData = createTotalChartData(allBugs);
        
        // Debug logging
        System.out.println("=== ENHANCED HOMEPAGE DEBUG ===");
        System.out.println("In Dev bugs: " + inDevBugs.size());
        System.out.println("In QA bugs: " + inQaBugs.size());
        System.out.println("Resolved bugs: " + resolvedBugs.size());
        System.out.println("Total bugs: " + allBugs.size());
        System.out.println("Enhanced chart data: " + chartData);
        System.out.println("Total chart data: " + totalChartData);

        model.addAttribute("stats", stats);
        model.addAttribute("inDevBugs", inDevBugs);
        model.addAttribute("inQaBugs", inQaBugs);
        model.addAttribute("resolvedBugs", resolvedBugs);
        model.addAttribute("chartData", chartData);
        model.addAttribute("totalChartData", totalChartData);
        
        // Add enums to model for form dropdowns
        model.addAttribute("severities", Enums.Severity.values());
        model.addAttribute("priorities", Enums.Priority.values());
        model.addAttribute("stages", Enums.Stage.values());

        return "homepage";
    }

    // Debug endpoint to check enhanced data generation
    @GetMapping("/debug-chart")
    @ResponseBody
    public Map<String, Object> debugChartData() {
        List<OfferBeezDashBoard> inDevBugs = appServices.getBugsByStatus(Enums.Stage.DEV);
        List<OfferBeezDashBoard> inQaBugs = appServices.getBugsByStatus(Enums.Stage.QA);
        List<OfferBeezDashBoard> resolvedBugs = appServices.getBugsByStatus(Enums.Stage.RESOLVED);
        List<OfferBeezDashBoard> allBugs = appServices.getAllBugs();
        
        System.out.println("=== DEBUG ENHANCED CHART DATA ===");
        System.out.println("In Dev bugs: " + inDevBugs.size());
        System.out.println("In QA bugs: " + inQaBugs.size());
        System.out.println("Resolved bugs: " + resolvedBugs.size());
        System.out.println("Total bugs: " + allBugs.size());
        
        // Print detailed sample bug data if available
        if (!allBugs.isEmpty()) {
            OfferBeezDashBoard sampleBug = allBugs.get(0);
            System.out.println("Sample bug details:");
            System.out.println("  ID: " + sampleBug.getId());
            System.out.println("  Admin: " + sampleBug.getAdmin());
            System.out.println("  Severity: " + sampleBug.getSeverity()); 
            System.out.println("  Priority: " + sampleBug.getPriority());
            System.out.println("  Status: " + sampleBug.getStatus());
        }
        
        Map<String, Object> chartData = createEnhancedChartData(inDevBugs, inQaBugs, resolvedBugs);
        Map<String, Object> totalChartData = createTotalChartData(allBugs);
        
        Map<String, Object> debug = new HashMap<>();
        debug.put("inDevCount", inDevBugs.size());
        debug.put("inQACount", inQaBugs.size());
        debug.put("resolvedCount", resolvedBugs.size());
        debug.put("totalCount", allBugs.size());
        debug.put("chartData", chartData);
        debug.put("totalChartData", totalChartData);
        debug.put("sampleBugs", allBugs.subList(0, Math.min(3, allBugs.size())));
        
        return debug;
    }

    // Enhanced chart data creation with both severity and priority
    private Map<String, Object> createEnhancedChartData(List<OfferBeezDashBoard> inDevBugs, 
                                                       List<OfferBeezDashBoard> inQaBugs, 
                                                       List<OfferBeezDashBoard> resolvedBugs) {
        Map<String, Object> chartData = new HashMap<>();
        
        // Initialize counters for each stage with both severity and priority
        Map<String, Map<String, Map<String, Integer>>> stageData = new HashMap<>();
        stageData.put("inDev", initializeEnhancedUserTypeCounters());
        stageData.put("inQA", initializeEnhancedUserTypeCounters());
        stageData.put("resolved", initializeEnhancedUserTypeCounters());
        
        // Count bugs for each stage (both severity and priority)
        countBugsBySeverityAndPriority(inDevBugs, stageData.get("inDev"));
        countBugsBySeverityAndPriority(inQaBugs, stageData.get("inQA"));
        countBugsBySeverityAndPriority(resolvedBugs, stageData.get("resolved"));
        
        // Debug the counted data
        System.out.println("Enhanced InDev stage data: " + stageData.get("inDev"));
        System.out.println("Enhanced InQA stage data: " + stageData.get("inQA"));
        System.out.println("Enhanced Resolved stage data: " + stageData.get("resolved"));
        
        chartData.put("stageData", stageData);
        return chartData;
    }

    // Create total chart data for all bugs combined
    private Map<String, Object> createTotalChartData(List<OfferBeezDashBoard> allBugs) {
        Map<String, Object> totalChartData = new HashMap<>();
        Map<String, Map<String, Integer>> totalCounters = initializeEnhancedUserTypeCounters();
        
        // Count all bugs regardless of stage
        countBugsBySeverityAndPriority(allBugs, totalCounters);
        
        System.out.println("Total chart data created: " + totalCounters);
        
        totalChartData.put("total", totalCounters);
        return totalChartData;
    }
    
    // Initialize counters with both severity (S1, S2, S3) and priority (P1, P2, P3)
    private Map<String, Map<String, Integer>> initializeEnhancedUserTypeCounters() {
        Map<String, Map<String, Integer>> userTypes = new HashMap<>();
        String[] types = {"admin", "newUser", "sales", "partner"};
        String[] categories = {"S1", "S2", "S3", "P1", "P2", "P3"}; // Both severity and priority
        
        for (String type : types) {
            Map<String, Integer> categoryCount = new HashMap<>();
            for (String category : categories) {
                categoryCount.put(category, 0);
            }
            userTypes.put(type, categoryCount);
        }
        return userTypes;
    }
    
    // Count bugs by both severity and priority for each user type
    private void countBugsBySeverityAndPriority(List<OfferBeezDashBoard> bugs, 
                                              Map<String, Map<String, Integer>> counters) {
        for (OfferBeezDashBoard bug : bugs) {
            if (bug.getSeverity() == null || bug.getPriority() == null) {
                System.out.println("Warning: Bug " + bug.getId() + " has null severity or priority");
                continue;
            }
            
            String severity = bug.getSeverity().toString();
            String priority = bug.getPriority().toString();
            
            System.out.println("Processing bug " + bug.getId() + ": Severity=" + severity + ", Priority=" + priority);
            
            // Count for each user type if applicable - both severity and priority
            if ("APPLICABLE".equals(bug.getAdmin())) {
                incrementCounter(counters, "admin", severity);
                incrementCounter(counters, "admin", priority);
                System.out.println("  Added to admin: " + severity + " and " + priority);
            }
            if ("APPLICABLE".equals(bug.getNewUser())) {
                incrementCounter(counters, "newUser", severity);
                incrementCounter(counters, "newUser", priority);
                System.out.println("  Added to newUser: " + severity + " and " + priority);
            }
            if ("APPLICABLE".equals(bug.getSales())) {
                incrementCounter(counters, "sales", severity);
                incrementCounter(counters, "sales", priority);
                System.out.println("  Added to sales: " + severity + " and " + priority);
            }
            if ("APPLICABLE".equals(bug.getPartner())) {
                incrementCounter(counters, "partner", severity);
                incrementCounter(counters, "partner", priority);
                System.out.println("  Added to partner: " + severity + " and " + priority);
            }
        }
    }
    
    // Helper method to safely increment counters
    private void incrementCounter(Map<String, Map<String, Integer>> counters, String userType, String category) {
        if (counters.containsKey(userType) && counters.get(userType).containsKey(category)) {
            int currentCount = counters.get(userType).get(category);
            counters.get(userType).put(category, currentCount + 1);
            System.out.println("    Incremented " + userType + "." + category + " to " + (currentCount + 1));
        } else {
            System.out.println("    Warning: Could not increment " + userType + "." + category + " - counter not found");
        }
    }

    @GetMapping("/bugs/add")
    public String showAddForm(Model model) {
        model.addAttribute("bug", new OfferBeezDashBoard());
        model.addAttribute("severities", Enums.Severity.values());
        model.addAttribute("priorities", Enums.Priority.values());
        model.addAttribute("stages", Enums.Stage.values());
        return "add";
    }

    @PostMapping("/bugs/add")
    public String saveBug(@ModelAttribute OfferBeezDashBoard bug) {
        System.out.println("Saving new bug: " + bug.toString());
        bug.setLive(true);
        OfferBeezDashBoard savedBug = appServices.saveBug(bug);
        System.out.println("Bug saved successfully with ID: " + savedBug.getId());
        return "redirect:/";
    }

    @GetMapping("/bugs/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        OfferBeezDashBoard bug = appServices.getBugById(id);
        if (bug == null) {
            System.out.println("Bug not found with ID: " + id);
            return "redirect:/";
        }
        model.addAttribute("bug", bug);
        model.addAttribute("severities", Enums.Severity.values());
        model.addAttribute("priorities", Enums.Priority.values());
        model.addAttribute("stages", Enums.Stage.values());
        return "update";
    }

    @PostMapping("/bugs/update")
    public String updateBug(@ModelAttribute OfferBeezDashBoard bug) {
        System.out.println("Updating bug: " + bug.toString());
        OfferBeezDashBoard updatedBug = appServices.saveBug(bug);
        System.out.println("Bug updated successfully with ID: " + updatedBug.getId());
        return "redirect:/";
    }

    @GetMapping("/bugs/delete/{id}")
    public String deleteBug(@PathVariable("id") int id) {
        System.out.println("Deleting bug with ID: " + id);
        appServices.deleteBug(id);
        System.out.println("Bug deleted successfully");
        return "redirect:/";
    }

    // Additional utility endpoints for debugging
    @GetMapping("/debug/counts")
    @ResponseBody
    public Map<String, Object> getDebugCounts() {
        Map<String, Object> counts = new HashMap<>();
        
        // Count by severity
        for (Enums.Severity severity : Enums.Severity.values()) {
            long count = appServices.countBugsBySeverity(severity);
            counts.put("severity_" + severity.toString(), count);
        }
        
        // Count by priority
        for (Enums.Priority priority : Enums.Priority.values()) {
            long count = appServices.countBugsByPriority(priority);
            counts.put("priority_" + priority.toString(), count);
        }
        
        // Count by status
        for (Enums.Stage stage : Enums.Stage.values()) {
            List<OfferBeezDashBoard> bugs = appServices.getBugsByStatus(stage);
            counts.put("status_" + stage.toString(), bugs.size());
        }
        
        counts.put("totalBugs", appServices.getAllBugs().size());
        
        return counts;
    }
}
