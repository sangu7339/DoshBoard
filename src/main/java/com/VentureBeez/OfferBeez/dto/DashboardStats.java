package com.VentureBeez.OfferBeez.dto;

public class DashboardStats {
    private long inDev;
    private long inQA;
    private long resolved;
    private long totalLive; // inDev + inQA

    public DashboardStats(long inDev, long inQA, long resolved) {
        this.inDev = inDev;
        this.inQA = inQA;
        this.resolved = resolved;
        this.totalLive = inDev + inQA;
    }

    public long getInDev() { return inDev; }
    public long getInQA() { return inQA; }
    public long getResolved() { return resolved; }
    public long getTotalLive() { return totalLive; }
}