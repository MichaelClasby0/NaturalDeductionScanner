package com.hack.naturaldeductionscanner.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
  private int lineNumber;
  private String formula;
  private String ruleString;
  private Rule rule;
  private List<Integer> lineReferences;

  public Line(int lineNumber, String formula, String ruleString, Rule rule) {
    this.lineNumber = lineNumber;
    this.formula = formula;
    this.ruleString = ruleString;
    this.rule = rule;
    lineReferences = extractLineReferences(ruleString);
  }

  public String toString() {
    return  lineNumber + " " + formula + " " + ruleString; //+
//            "\n" +
//            "line references: " + lineReferencesAsString(lineReferences) +
//            "\n";
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getFormula() {
    return formula;
  }

  public Rule getRule() {
    return rule;
  }

  public List<Integer> getLineReferences() {
    return lineReferences;
  }

  public void setRule(Rule rule) {
    this.rule = rule;
  }

  private static List<Integer> extractLineReferences(String ruleString) {
    List<Integer> lineReferences = new ArrayList<>();
    if (ruleString.contains("(")) {
      Arrays.stream(ruleString.substring(findCharacterIndex(ruleString,'(') + 1, findCharacterIndex(ruleString, ')')).split(","))
              .map(number -> lineReferences.add(Integer.parseInt(number)))
              .collect(Collectors.toList());
    }
    return lineReferences;
  }

  private static int findCharacterIndex(String ruleString, char c) {
    int index = ruleString.indexOf(c);
    if (index > 0) {
      return index;
    } else {
      return 0;
    }
  }

  private static String lineReferencesAsString(List<Integer> lineReferences) {
    StringBuilder str = new StringBuilder();
    str.append("[");
    for (int reference : lineReferences) {
      str.append(reference + ", ");
    }
    if (lineReferences.size() > 0) {
      str.delete(str.length() - 2, str.length());
    }
    str.append("]");
    return str.toString();
  }
}
