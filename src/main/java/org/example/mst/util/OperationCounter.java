package org.example.mst.util;


public class OperationCounter {
    private long comparisons = 0;
    private long unions = 0;
    private long other = 0;

    public void incComparisons() { comparisons++; }
    public void addComparisons(long delta) { comparisons += delta; }
    public void incUnions() { unions++; }
    public void incOther() { other++; }

    public long getComparisons() { return comparisons; }
    public long getUnions() { return unions; }
    public long getOther() { return other; }
    public long total() { return comparisons + unions + other; }
}
