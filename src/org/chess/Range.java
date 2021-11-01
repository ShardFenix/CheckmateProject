package org.chess;

public class Range {

    public int start, end;

    public Range(int s, int e) {
        start = s;
        end = e;
    }
    
    public boolean includes(int i){
        return i>=start && i<end;
    }

    public String toString() {
        return "" + start + "-" + end;
    }
}
