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
    public String homepage(Model model) {
        List<OfferBeezDoshBoard> allItems = appServices.getAllItems();
        
        // Separate items by status
        List<OfferBeezDoshBoard> devItems = allItems.stream()
            .filter(item -> "In Dev".equals(item.getStatus()) || 
                           "open".equals(item.getInDev()) || 
                           "fail".equals(item.getInDev()) || 
                           "pass".equals(item.getInDev()))
            .toList();
            
        List<OfferBeezDoshBoard> qaItems = allItems.stream()
            .filter(item -> "In QA".equals(item.getStatus()) || 
                           "open".equals(item.getInQA()) || 
                           "fail".equals(item.getInQA()) || 
                           "pass".equals(item.getInQA()))
            .toList();
            
        List<OfferBeezDoshBoard> liveItems = allItems.stream()
            .filter(item -> item.isLive())
            .toList();
            
        List<OfferBeezDoshBoard> previousItems = allItems.stream()
            .filter(item -> !item.isLive())
            .toList();

        model.addAttribute("devItems", devItems);
        model.addAttribute("qaItems", qaItems);
        model.addAttribute("liveItems", liveItems);
        model.addAttribute("previousItems", previousItems);
        model.addAttribute("overallStatus", appServices.getOverallStatus());
        model.addAttribute("newItem", new OfferBeezDoshBoard());
        
        return "homepage";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new OfferBeezDoshBoard());
        return "add";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute OfferBeezDoshBoard item) {
        appServices.saveItem(item);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        OfferBeezDoshBoard item = appServices.getItemById(id);
        model.addAttribute("item", item);
        return "update";
    }

    @PostMapping("/update")
    public String updateItem(@ModelAttribute OfferBeezDoshBoard item) {
        appServices.updateItem(item);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable int id) {
        appServices.deleteItem(id);
        return "redirect:/";
    }
}
