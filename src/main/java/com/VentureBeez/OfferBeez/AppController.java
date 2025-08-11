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
                           ("pass".equals(item.getInDev()) && !"pass".equals(item.getInQA())))
            .toList();
            
        List<OfferBeezDoshBoard> qaItems = allItems.stream()
            .filter(item -> "In QA".equals(item.getStatus()) || 
                           "open".equals(item.getInQA()) || 
                           "fail".equals(item.getInQA()) || 
                           ("pass".equals(item.getInDev()) && "open".equals(item.getInQA())))
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
        // Set new item as LIVE by default
        item.setLive(true);
        item.setStatus("Completed"); // Mark as completed since it's going live
        
        // Set default values for new items
        if (item.getInDev() == null || item.getInDev().isEmpty()) {
            item.setInDev("pass");
        }
        if (item.getInQA() == null || item.getInQA().isEmpty()) {
            item.setInQA("pass");
        }
        if (item.getVerifyAndClosed() == null || item.getVerifyAndClosed().isEmpty()) {
            item.setVerifyAndClosed("pass");
        }
        if (item.getReopen() == null || item.getReopen().isEmpty()) {
            item.setReopen("no");
        }
        
        appServices.saveItem(item);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        OfferBeezDoshBoard item = appServices.getItemById(id);
        if (item == null) {
            return "redirect:/";
        }
        model.addAttribute("item", item);
        return "update";
    }

    @PostMapping("/update")
    public String updateItem(@ModelAttribute OfferBeezDoshBoard item) {
        // Get the original item to create previous version
        OfferBeezDoshBoard originalItem = appServices.getItemById(item.getId());
        
        if (originalItem != null) {
            // Create a copy of the original item and mark it as PREVIOUS
            OfferBeezDoshBoard previousVersion = new OfferBeezDoshBoard();
            previousVersion.setProject(originalItem.getProject() + " (Previous)");
            previousVersion.setBugNumber(originalItem.getBugNumber() + "-OLD");
            previousVersion.setIssue(originalItem.getIssue());
            previousVersion.setSeverity(originalItem.getSeverity());
            previousVersion.setPriority(originalItem.getPriority());
            previousVersion.setNewUser(originalItem.getNewUser());
            previousVersion.setPartner(originalItem.getPartner());
            previousVersion.setAdmin(originalItem.getAdmin());
            previousVersion.setSales(originalItem.getSales());
            previousVersion.setStatus(originalItem.getStatus());
            previousVersion.setLive(false); // Mark as PREVIOUS
            previousVersion.setInDev(originalItem.getInDev());
            previousVersion.setInQA(originalItem.getInQA());
            previousVersion.setReopen(originalItem.getReopen());
            previousVersion.setVerifyAndClosed(originalItem.getVerifyAndClosed());
            previousVersion.setRemarks("Previous version of Bug#" + originalItem.getBugNumber());
            
            // Save the previous version
            appServices.saveItem(previousVersion);
        }
        
        // Update the current item and mark it as LIVE
        item.setLive(true);
        item.setStatus("Completed"); // Mark as completed since it's going live
        
        // Ensure updated item has proper status
        if (item.getInDev() == null || item.getInDev().isEmpty()) {
            item.setInDev("pass");
        }
        if (item.getInQA() == null || item.getInQA().isEmpty()) {
            item.setInQA("pass");
        }
        if (item.getVerifyAndClosed() == null || item.getVerifyAndClosed().isEmpty()) {
            item.setVerifyAndClosed("pass");
        }
        
        appServices.updateItem(item);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable int id) {
        appServices.deleteItem(id);
        return "redirect:/";
    }
}
