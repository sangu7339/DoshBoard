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
	    return appRepository.findById(id).orElse(null);
	}

	public OfferBeezDoshBoard updateItem(OfferBeezDoshBoard item) {
	    return appRepository.save(item);
	}

	public void deleteItem(int id) {
	    appRepository.deleteById(id);
	}

	public List<OfferBeezDoshBoard> getLiveItems() { return appRepository.findByLive(true); }

	public List<OfferBeezDoshBoard> getPreviousItems() { return appRepository.findByLive(false); }

	public String getOverallStatus() {
	    List<OfferBeezDoshBoard> items = appRepository.findAll();
	    boolean hasNegative = items.stream().anyMatch(i ->
	            "fail".equalsIgnoreCase(i.getInDev())
	                    || "fail".equalsIgnoreCase(i.getInQA())
	                    || "fail".equalsIgnoreCase(i.getVerifyAndClosed())
	                    || "yes".equalsIgnoreCase(i.getReopen()));
	    return hasNegative ? "NEGATIVE" : "POSITIVE";
	}

}
