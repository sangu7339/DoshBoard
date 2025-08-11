package com.VentureBeez.OfferBeez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferBeezService {

    @Autowired
    private OfferBeezRepository offerBeezRepository;

    public List<OfferBeezDoshBoard> getAllStatuses() {
        return offerBeezRepository.findAll();
    }

    public OfferBeezDoshBoard getStatusById(int id) {
        return offerBeezRepository.findById(id).orElse(null);
    }

    public void saveStatus(OfferBeezDoshBoard status) {
        offerBeezRepository.save(status);
    }

    public void deleteStatus(int id) {
        offerBeezRepository.deleteById(id);
    }
}
