package com.hack.naturaldeductionscanner.proofverification;

import com.hack.naturaldeductionscanner.entities.Line;
import com.hack.naturaldeductionscanner.entities.Proof;
import com.hack.naturaldeductionscanner.lineparsing.LineParser;
import com.hack.naturaldeductionscanner.lineparsing.ProofBreaker;

import java.util.ArrayList;
import java.util.List;

public class ProofVerifier {
    private LineVerifier verifier = new LineVerifier();

    public int verify(Proof proof) {
        for (Line line : proof.getLines()) {
            try {
                if (!verifier.verifyLine(line, proof)) {
                    return line.getLineNumber();
                }
            } catch (Exception e) {
                return line.getLineNumber();
            }
        }
        return 0;
    }

    public int verifyProof(String proof) {
        ProofBreaker breaker = new ProofBreaker();
        LineParser parser = new LineParser();
        List<String> linesFromWholeProof;
        try {
            linesFromWholeProof = breaker.breakIntoLines(proof.toString());
        } catch (Exception e) {
            return -1;
        }
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < linesFromWholeProof.size(); i++) {
            try{
                lines.add(parser.parseLine(linesFromWholeProof.get(i)));
            } catch (Exception e) {
                return lines.get(lines.size() - 1).getLineNumber() + 1;
            }
        }
        return verify(new Proof(lines));
    }
}
