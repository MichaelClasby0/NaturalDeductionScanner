package com.hack.naturaldeductionscanner.proofverification;

import com.hack.naturaldeductionscanner.entities.Line;
import com.hack.naturaldeductionscanner.entities.Proof;
import com.hack.naturaldeductionscanner.entities.Rule;

public class LineVerifier {

    public boolean verifyLine(Line line, Proof proof) {
        RuleVerifier ruleVerifier = new RuleVerifier(line.getLineNumber(), line.getFormula(),
                line.getRule(), line.getLineReferences(), proof.getLines());
        return ruleVerifier.verifyRuleApplication();
    }
}
