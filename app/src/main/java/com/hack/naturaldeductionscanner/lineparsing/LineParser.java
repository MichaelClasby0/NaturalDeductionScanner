package com.hack.naturaldeductionscanner.lineparsing;

import com.hack.naturaldeductionscanner.entities.Line;
import com.hack.naturaldeductionscanner.entities.Rule;

import java.util.Arrays;

public class LineParser {

  // To a certain extent assumes line is in correct format
  public Line parseLine(String line) {
    int numberEndIndex = getNumberEndIndex(line);
    int formulaStartIndex = numberEndIndex;
    int formulaEndIndex = getFormulaEndIndex(line);
    int lineNumber = Integer.parseInt(line.substring(0, numberEndIndex));
    String formula = line.substring(formulaStartIndex, formulaEndIndex);
    String rule = line.substring(formulaEndIndex);
    Rule lineRule = getRuleFromString(rule);
    return new Line(lineNumber, formula, rule, lineRule);
  }

  private static Rule getRuleFromString(String rule) {
    switch (removeBracketsFromRule(rule)) {
      case ("given"):
        return Rule.GIVEN;
      case ("lemma"):
        return Rule.LEMMA;
      case ("ass"):
        return Rule.ASS;
      case ("^I"):
        return Rule.AND_INTRO;
      case ("^E"):
        return Rule.AND_ELIM;
      case ("vI"):
        return Rule.OR_INTRO;
      case ("vE"):
        return Rule.OR_ELIM;
      case ("->I"):
        return Rule.ARROW_INTRO;
      case ("->E"):
        return Rule.ARROW_ELIM;
      case ("<->I"):
        return Rule.DARROW_INTRO;
      case ("<->E"):
        return Rule.DARROW_ELIM;
      case ("¬I"):
        return Rule.NOT_INTRO;
      case ("¬E"):
        return Rule.NOT_ELIM;
      case ("FI"):
        return Rule.FALSE_INTRO;
      case ("FE"):
        return Rule.FALSE_ELIM;
      case ("¬¬E"):
        return Rule.NOT_NOT_ELIM;
      default:
        return null;
    }
  }

  private static String removeBracketsFromRule(String rule) {
    if (rule.contains("(")) {
      return rule.substring(0,rule.indexOf('('));
    } else {
      return rule;
    }
  }

  private static int getNumberEndIndex(String line) {
    for (int i = 0; i < line.length(); i++) {
      if (!Character.isDigit(line.charAt(i))) {
        return i;
      }
    }
    throw new IllegalArgumentException("Line is not in the correct format");
  }

  private static int getFormulaEndIndex(String line) {
    for (int i = getNumberEndIndex(line); i <= line.length(); i++) {
      if (isFirstLetterInARule(line.charAt(i))) {
        if (line.charAt(i - 1) == '¬' && line.charAt(i - 2) == '¬') {
          return i - 2;
        }
        if (line.charAt(i - 1) == '>') {
          if (line.charAt(i - 3) == '<') {
            return i - 3;
          } else {
            return i - 2;
          }
        } else {
          if (Character.isLowerCase(line.charAt(i))) {
            return i;
          } else {
            return i - 1;
          }
        }
      }
    }
    throw new IllegalArgumentException("Line is not in the correct format");
  }

  private static boolean isFirstLetterInARule(char c) {
    Character[] ruleFirstLetters = {'E', 'I', 'a', 'g', 'l'}; // E, I, ass, given, lemma
    return Arrays.asList(ruleFirstLetters).contains(c);
  }
}
