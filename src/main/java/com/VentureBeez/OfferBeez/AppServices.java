package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VentureBeez.OfferBeez.util.Enums;

import java.util.List;

@Service
public class AppServices {

    @Autowired
    private AppRepository appRepository;

    public List<OfferBeezDashBoard> getBugsByStatus(Enums.Stage status) {
        return appRepository.findByStatus(status);
    }

    public OfferBeezDashBoard saveBug(OfferBeezDashBoard bug) {
        return appRepository.save(bug);
    }

    public List<OfferBeezDashBoard> getAllBugs() {
        return appRepository.findAll();
    }

    public OfferBeezDashBoard getBugById(int id) {
        return appRepository.findById(id).orElse(null);
    }

    public void deleteBug(int id) {
        appRepository.deleteById(id);
    }
}
