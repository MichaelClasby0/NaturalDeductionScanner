package com.hack.naturaldeductionscanner.lineparsing;

import java.util.ArrayList;
import java.util.List;

public class ProofBreaker {
  public List<String> breakIntoLines(String proof) {
    List<String> lines = new ArrayList<>();

    if (proof.length() == 0) {
      return lines;
    }

    // Find the where each line starts
    List<Integer> lineStartIndices = new ArrayList<>();
    lineStartIndices.add(0);
    for (int i = 1; i < proof.length(); i++) {
      if (areLineEndingCharacters(proof.charAt(i - 1), proof.charAt(i))) {
        lineStartIndices.add(i + 1);
      }
    }

    // Split into lines and add lines to the list
    for (int i = 0; i < lineStartIndices.size() - 1; i++) {
      lines.add(proof.substring(lineStartIndices.get(i), lineStartIndices.get(i + 1)));
    }

    return lines;
  }

  private static boolean areLineEndingCharacters(char c1, char c2) {
    return (Character.isDigit(c1) && c2 == ')') ||
            (c1 == 's' && c2 == 's') || // ending characters of ass
            (c1 == 'm' && c2 == 'a') || // ending characters of lemma
            (c1 == 'e' && c2 == 'n');   // ending characters of given
  }
}
