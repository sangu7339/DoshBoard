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

        model.addAttribute("stats", stats);
        model.addAttribute("inDevBugs", inDevBugs);
        model.addAttribute("inQaBugs", inQaBugs);
        model.addAttribute("resolvedBugs", resolvedBugs);
        
        // Add enums to model for form dropdowns
        model.addAttribute("severities", Enums.Severity.values());
        model.addAttribute("priorities", Enums.Priority.values());
        model.addAttribute("stages", Enums.Stage.values());

        return "homepage";
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
        bug.setLive(true);
        appServices.saveBug(bug);
        return "redirect:/";
    }

    @GetMapping("/bugs/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        OfferBeezDashBoard bug = appServices.getBugById(id);
        model.addAttribute("bug", bug);
        model.addAttribute("severities", Enums.Severity.values());
        model.addAttribute("priorities", Enums.Priority.values());
        model.addAttribute("stages", Enums.Stage.values());
        return "update";
    }

    @PostMapping("/bugs/update")
    public String updateBug(@ModelAttribute OfferBeezDashBoard bug) {
        appServices.saveBug(bug);
        return "redirect:/";
    }

    @GetMapping("/bugs/delete/{id}")
    public String deleteBug(@PathVariable("id") int id) {
        appServices.deleteBug(id);
        return "redirect:/";
    }
}
