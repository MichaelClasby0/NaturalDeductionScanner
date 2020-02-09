package com.hack.naturaldeductionscanner.entities;

import java.util.List;

public class Proof {
    private List<Line> lines;

    public Proof(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

}
