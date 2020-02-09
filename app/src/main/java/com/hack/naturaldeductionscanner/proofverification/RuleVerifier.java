package com.hack.naturaldeductionscanner.proofverification;

import com.hack.naturaldeductionscanner.entities.Line;
import com.hack.naturaldeductionscanner.entities.Rule;

import java.util.List;

public class RuleVerifier {
    private int lineNumber;
    private String formula;
    private Rule rule;
    private List<Integer> lineReferences;
    private List<Line> lines;

    public RuleVerifier(int lineNumber, String formula, Rule rule, List<Integer> lineReferences, List<Line> lines) {
        this.lineNumber = lineNumber;
        this.formula = formula;
        this.rule = rule;
        this.lineReferences = lineReferences;
        this.lines = lines;
    }

    public boolean verifyRuleApplication() {
        if (lineReferences.stream()
                .anyMatch(ref -> ref >= lineNumber)){
            return false;
        }
        switch (rule) {
            case ASS:
                return verifyAssumption();
            case GIVEN:
                return true;
            case LEMMA:
                return true;
            case AND_ELIM:
                return verifyAndElimination();
            case AND_INTRO:
                return verifyAndIntroduction();
            case OR_INTRO:
                return verifyOrIntroduction();
            case OR_ELIM:
                return verifyOrElimination();
            case ARROW_INTRO:
                return verifyArrowIntroduction();
            case ARROW_ELIM:
                return verifyArrowElimination();
            case NOT_INTRO:
                return verifyNotIntroduction();
            case FALSE_ELIM:
                return verifyFalseElimination();
            case NOT_ELIM:
                return verifyNotElimination();
            case NOT_NOT_ELIM:
                return verifyNotNotElimination();
            case DARROW_INTRO:
                return verifyDoubleArrowIntroduction();
            case DARROW_ELIM:
                return verifyDoubleArrowElimination();
        }
        return false;
    }

    private boolean verifyAssumption() {
        for (int i = lines.indexOf(getLineByReference(lineNumber)); i < lines.size(); i++) {
            Line current = lines.get(i);
            if (current.getRule() == Rule.OR_ELIM
                || current.getRule() == Rule.ARROW_INTRO
                || current.getRule() == Rule.NOT_INTRO) {
                if (current.getLineReferences().contains(lineNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyOrElimination() {
        String fReferralOne =
                getLineByReference(lineReferences.get(0)).getFormula();
        String fReferralTwo =
                getLineByReference(lineReferences.get(1)).getFormula();
        String fReferralThree =
                getLineByReference(lineReferences.get(2)).getFormula();
        String fReferralFour =
                getLineByReference(lineReferences.get(3)).getFormula();
        String fReferralFive =
                getLineByReference(lineReferences.get(4)).getFormula();
        if (lineReferences.get(2) == lineReferences.get(4)) {
            return false;
        }
        for (int i = 0; i < fReferralOne.length(); i++) {
            if (fReferralOne.charAt(i) == 'v') {
                String firstSubstring = removeStartAndEndBrackets(fReferralOne.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(fReferralOne.substring(i + 1));
                boolean check1 =
                        firstSubstring.equals(fReferralTwo) && secondSubstring.equals(fReferralFour)
                                || firstSubstring.equals(fReferralFour) && secondSubstring.equals(fReferralTwo);
                boolean check2 = fReferralFive.equals(fReferralThree) && fReferralFive.equals(formula);
                return check1 && check2;
            }
        }
        return false;
    }


    private boolean verifyDoubleArrowElimination() {
        String fReferralOne = removeStartAndEndBrackets(getLineByReference(lineReferences.get(0)).getFormula());
        String fReferralTwo = removeStartAndEndBrackets(getLineByReference(lineReferences.get(1)).getFormula());
        for (int i = 0; i < fReferralOne.length() - 2; i++) {
            if (fReferralOne.charAt(i) == '<' && fReferralOne.charAt(i + 1) == '-' && fReferralOne.charAt(i+2) == '>') {
                String firstSubstring = removeStartAndEndBrackets(fReferralOne.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(fReferralOne.substring(i + 3));
                if (firstSubstring.equals(fReferralTwo) && secondSubstring.equals(formula) ||
                        (firstSubstring.equals(formula) && secondSubstring.equals(fReferralTwo))) {
                    return true;
                }
            }
        }
        for (int i = 0; i < fReferralTwo.length() - 2; i++) {
            if (fReferralTwo.charAt(i) == '<' && fReferralTwo.charAt(i + 1) == '-' && fReferralTwo.charAt(i+2) == '>') {
                String firstSubstring = removeStartAndEndBrackets(fReferralTwo.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(fReferralTwo.substring(i + 3));
                if (firstSubstring.equals(fReferralOne) && secondSubstring.equals(formula) ||
                        (firstSubstring.equals(formula) && secondSubstring.equals(fReferralOne))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyDoubleArrowIntroduction() {
        String formulaOne = getLineByReference(lineReferences.get(0)).getFormula();
        String formulaTwo = getLineByReference(lineReferences.get(1)).getFormula();
        String f1;
        String f2;
        for (int i = 0; i < formula.length() - 2; i++) {
            if (formula.charAt(i) == '<' && formula.charAt(i + 1) == '-' && formula.charAt(i + 2) == '>') {
                f1 = removeStartAndEndBrackets(formula.substring(0, i));
                f2 = removeStartAndEndBrackets(formula.substring(i + 3));
                boolean formulaOneContainsf1Andf2 = false;
                boolean formulaOneContainsf2Andf1 = false;
                boolean formulaTwoContainsf1Andf2 = false;
                boolean formulaTwoContainsf2Andf1 = false;
                for (int j = 0; j < formulaOne.length() - 1; j++) {
                    if (formulaOne.charAt(j) == '-' && formulaOne.charAt(j + 1) == '>') {
                        if (removeStartAndEndBrackets(formulaOne.substring(0, j)).equals(f1)
                                && removeStartAndEndBrackets(formulaOne.substring(j + 2)).equals(f2)) {
                            formulaOneContainsf1Andf2 = true;
                        }
                        if (removeStartAndEndBrackets(formulaOne.substring(0, j)).equals(f2)
                                && removeStartAndEndBrackets(formulaOne.substring(j + 2)).equals(f1)) {
                            formulaOneContainsf2Andf1 = true;
                        }
                    }
                }
                for (int j = 0; j < formulaTwo.length() - 1; j++) {
                    if (formulaTwo.charAt(j) == '-' && formulaTwo.charAt(j + 1) == '>') {
                        if (removeStartAndEndBrackets(formulaTwo.substring(0, j)).equals(f1)
                                && removeStartAndEndBrackets(formulaTwo.substring(j + 2)).equals(f2)) {
                            formulaTwoContainsf1Andf2 = true;
                        }
                        if (removeStartAndEndBrackets(formulaTwo.substring(0, j)).equals(f2)
                                && removeStartAndEndBrackets(formulaTwo.substring(j + 2)).equals(f1)) {
                            formulaTwoContainsf2Andf1 = true;
                        }
                    }
                }
                if ((formulaOneContainsf1Andf2 && formulaTwoContainsf2Andf1)
                    || (formulaOneContainsf2Andf1 && formulaTwoContainsf1Andf2)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyNotNotElimination() {
        String formulaReferral = removeStartAndEndBrackets(getLineByReference(lineReferences.get(0)).getFormula());
        if (formulaReferral.charAt(0) == '¬' && formulaReferral.charAt(1) == '¬') {
            return (removeStartAndEndBrackets(formula).equals(removeStartAndEndBrackets(formulaReferral.substring(2))));
        }
        return false;
    }

    private boolean verifyNotElimination() {
        String fReferralOne = removeStartAndEndBrackets(getLineByReference(lineReferences.get(0)).getFormula());
        String fReferralTwo = removeStartAndEndBrackets(getLineByReference(lineReferences.get(1)).getFormula());
        if (!formula.equals("F")) {
            return false;
        }
        return (fReferralOne.charAt(0) == '¬'
                && removeStartAndEndBrackets(fReferralOne.substring(1)).equals(fReferralTwo))
                || (fReferralTwo.charAt(0) == '¬'
                && removeStartAndEndBrackets(fReferralTwo.substring(1)).equals(fReferralOne));
    }

    private boolean verifyFalseElimination() {
        int lineReference = lineReferences.get(0);
        if (lineReference >= lineNumber) {
            return false;
        }
        if (getLineByReference(lineReference).getFormula().equals("F")) {
            return true;
        }
        return false;
    }

    private boolean verifyNotIntroduction() {
        int lineReferenceOne = lineReferences.get(0);
        int lineReferenceTwo = lineReferences.get(1);
        if (lineReferenceOne >= lineReferenceTwo) {
            return false;
        }
        Line lineReferralOne = getLineByReference(lineReferenceOne);
        Line lineReferralTwo = getLineByReference(lineReferenceTwo);
        String formulaOne = lineReferralOne.getFormula();
        String formulaTwo = lineReferralTwo.getFormula();
        if (lineReferralOne.getRule() != Rule.ASS) {
            return false;
        }
        if (!formulaTwo.equals("F")) {
            return false;
        }
        if (removeStartAndEndBrackets(formula.substring(1)).equals(formulaOne)) {
            return true;
        }
        return false;
    }

    private boolean verifyAndElimination() {
        int reference = lineReferences.get(0);
        Line lineReferral = getLineByReference(reference);
        String formulaReferral = lineReferral.getFormula();
        for (int i = 0; i < formulaReferral.length(); i++) {
            if (formulaReferral.charAt(i) == '^') {
                if (removeStartAndEndBrackets(formulaReferral.substring(0, i)).equals(formula)
                        || removeStartAndEndBrackets(formulaReferral.substring(i + 1)).equals(formula)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyArrowIntroduction() {
        int lineReferenceOne = lineReferences.get(0);
        int lineReferenceTwo = lineReferences.get(1);
        if (lineReferenceOne >= lineReferenceTwo) {
            return false;
        }
        Line lineReferralOne = getLineByReference(lineReferenceOne);
        Line lineReferralTwo = getLineByReference(lineReferenceTwo);
        String formulaOne = lineReferralOne.getFormula();
        String formulaTwo = lineReferralTwo.getFormula();
        for (int i = 0; i < formula.length() - 1; i++) {
            if (formula.substring(i, i + 2).equals("->")) {
                if (removeStartAndEndBrackets(formula.substring(0, i)).equals(formulaOne)
                        && removeStartAndEndBrackets(formula.substring(i + 2)).equals(formulaTwo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyOrIntroduction() {
        int reference = lineReferences.get(0);
        Line lineReferral = getLineByReference(reference);
        String formulaReferral = lineReferral.getFormula();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == 'v') {
                if (removeStartAndEndBrackets(formula.substring(0, i)).equals(formulaReferral)
                        || removeStartAndEndBrackets(formula.substring(i + 1)).equals(formulaReferral)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyAndIntroduction() {
        String fReferralOne = getLineByReference(lineReferences.get(0)).getFormula();
        String fReferralTwo = getLineByReference(lineReferences.get(1)).getFormula();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '^') {
                String firstSubstring = removeStartAndEndBrackets(formula.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(formula.substring(i + 1));
                if ((firstSubstring.equals(fReferralOne) || firstSubstring.equals(fReferralTwo))
                        && (secondSubstring.equals(fReferralOne) || secondSubstring.equals(fReferralTwo))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyArrowElimination() {
        String fReferralOne = getLineByReference(lineReferences.get(0)).getFormula();
        String fReferralTwo = getLineByReference(lineReferences.get(1)).getFormula();
        for (int i = 0; i < fReferralOne.length() - 1; i++) {
            if (fReferralOne.charAt(i) == '-' && fReferralOne.charAt(i + 1) == '>') {
                String firstSubstring = removeStartAndEndBrackets(fReferralOne.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(fReferralOne.substring(i + 2));
                if (firstSubstring.equals(fReferralTwo) && secondSubstring.equals(formula)) {
                    return true;
                }
            }
        }
        for (int i = 0; i < fReferralTwo.length() - 1; i++) {
            if (fReferralTwo.charAt(i) == '-' && fReferralTwo.charAt(i + 1) == '>') {
                String firstSubstring = removeStartAndEndBrackets(fReferralTwo.substring(0, i));
                String secondSubstring = removeStartAndEndBrackets(fReferralTwo.substring(i + 2));
                if (firstSubstring.equals(fReferralOne) && secondSubstring.equals(formula)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String removeStartAndEndBrackets(String formula) {
        if (formula.charAt(0) == '(' && formula.charAt(formula.length() - 1) == ')') {
            return formula.substring(1, formula.length() - 1);
        } else {
            return formula;
        }
    }

    private Line getLineByReference(int reference) { //helper method for finding the line by reference
        for (Line line : lines) {
            if (reference == line.getLineNumber()) {
                return line;
            }
        }
        return null;
    }
}