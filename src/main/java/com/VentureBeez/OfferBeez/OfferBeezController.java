package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OfferBeezController {

    @Autowired
    private OfferBeezService offerBeezService;

    @GetMapping("/")
    public String home(Model model) {
        List<OfferBeezDoshBoard> statuses = offerBeezService.getAllStatuses();
        int total = statuses.size();

        double newUserPercent = total > 0 ? (statuses.stream().filter(s -> "Applicable".equalsIgnoreCase(s.getNewUser())).count() * 100.0 / total) : 0;
        double partnerPercent = total > 0 ? (statuses.stream().filter(s -> "Applicable".equalsIgnoreCase(s.getPartner())).count() * 100.0 / total) : 0;
        double adminPercent = total > 0 ? (statuses.stream().filter(s -> "Applicable".equalsIgnoreCase(s.getAdmin())).count() * 100.0 / total) : 0;
        double salesPercent = total > 0 ? (statuses.stream().filter(s -> "Applicable".equalsIgnoreCase(s.getSales())).count() * 100.0 / total) : 0;

        model.addAttribute("statuses", statuses);
        model.addAttribute("newUserPercent", newUserPercent);
        model.addAttribute("partnerPercent", partnerPercent);
        model.addAttribute("adminPercent", adminPercent);
        model.addAttribute("salesPercent", salesPercent);

        return "home";
    }

    @GetMapping("/add")
    public String addStatusForm(Model model) {
        model.addAttribute("status", new OfferBeezDoshBoard());
        return "add-status";
    }

    @PostMapping("/add")
    public String saveStatus(@ModelAttribute OfferBeezDoshBoard status) {
        offerBeezService.saveStatus(status);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editStatusForm(@PathVariable int id, Model model) {
        OfferBeezDoshBoard status = offerBeezService.getStatusById(id);
        model.addAttribute("status", status);
        return "add-status";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable int id) {
        offerBeezService.deleteStatus(id);
        return "redirect:/";
    }
}
