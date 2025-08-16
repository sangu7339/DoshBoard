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
        try {
            List<OfferBeezDashBoard> bugs = appRepository.findByStatus(status);
            System.out.println("Found " + bugs.size() + " bugs with status: " + status);
            
            // Debug: Print first bug if available
            if (!bugs.isEmpty()) {
                System.out.println("Sample bug: " + bugs.get(0).toString());
            }
            
            return bugs;
        } catch (Exception e) {
            System.err.println("Error fetching bugs by status " + status + ": " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    public OfferBeezDashBoard saveBug(OfferBeezDashBoard bug) {
        try {
            System.out.println("Saving bug: " + bug.toString());
            OfferBeezDashBoard savedBug = appRepository.save(bug);
            System.out.println("Bug saved successfully with ID: " + savedBug.getId());
            return savedBug;
        } catch (Exception e) {
            System.err.println("Error saving bug: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<OfferBeezDashBoard> getAllBugs() {
        try {
            List<OfferBeezDashBoard> allBugs = appRepository.findAll();
            System.out.println("Total bugs in database: " + allBugs.size());
            return allBugs;
        } catch (Exception e) {
            System.err.println("Error fetching all bugs: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public OfferBeezDashBoard getBugById(int id) {
        try {
            return appRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.err.println("Error fetching bug by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deleteBug(int id) {
        try {
            System.out.println("Deleting bug with ID: " + id);
            appRepository.deleteById(id);
            System.out.println("Bug deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting bug with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Additional helper methods for debugging
    public long countBugsBySeverity(Enums.Severity severity) {
        try {
            return appRepository.findBySeverity(severity).size();
        } catch (Exception e) {
            System.err.println("Error counting bugs by severity " + severity + ": " + e.getMessage());
            return 0;
        }
    }
    
    public long countBugsByPriority(Enums.Priority priority) {
        try {
            return appRepository.findByPriority(priority).size();
        } catch (Exception e) {
            System.err.println("Error counting bugs by priority " + priority + ": " + e.getMessage());
            return 0;
        }
    }
}
