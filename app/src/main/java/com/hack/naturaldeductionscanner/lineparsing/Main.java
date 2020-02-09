package com.hack.naturaldeductionscanner.lineparsing;

import com.hack.naturaldeductionscanner.entities.Line;
import com.hack.naturaldeductionscanner.entities.Proof;
import com.hack.naturaldeductionscanner.entities.Rule;
import com.hack.naturaldeductionscanner.proofverification.LineVerifier;
import com.hack.naturaldeductionscanner.proofverification.ProofVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    //    System.out.println("--------------------------------------------");
    //    System.out.println("Parsing lines individually");
    //    System.out.println("--------------------------------------------");
    //    System.out.println();
    //    List<String> examples = new ArrayList<>();
    //    examples.add("1Aass");
    //    examples.add("2B->Qlemma");
    //    examples.add("3P->Q<->I(1,2)");
    //    examples.add("4~(PvQ->A)~I(414453,124)");
    //    examples.add("5~(A^B<->(P->Q))vG<->XvE(1,2,3,4,5)");
    //    examples.add("1Agiven");
    LineParser parser = new LineParser();
    //    for (String line : examples) {
    //      System.out.println(parser.parseLine(line));
    //    }

    StringBuilder str = new StringBuilder();
    //    str.append("1Aass");
    //    str.append("2B->Qlemma");
    //    str.append("3P->Q<->I(1,2)");
    //    str.append("4~(PvQ->A)~I(414453,124)");
    //    str.append("5~(A^B<->(P->Q))vG<->XvE(1,2,3,4,5)");
    //    str.append("1Agiven");

    //    str.append("1(Y<->A)^(B->G)given");
    //    str.append("2Y<->A^E(1)");
    //    str.append("3B->G^E(1)");
    //    str.append("4Hlemma");
    //    str.append("5Kass");
    //    str.append("6KvBvI(5)");
    //    str.append("7P->Qgiven");
    //    str.append("8(P->Q)vTvI(7)");
    //    str.append("9Agiven");
    //    str.append("10W->Nass");
    //    str.append("11A->(W->N)->I(9,10)");

    // Actual proofs:
    // Proof 1
    //    str.append("1pass");
    //    str.append("2p^q^I(1)");
    //    str.append("3p->p^q->I(1,2)");

    // Proof 2
    //    str.append("1Aass");
    //    str.append("2Flemma");
    //    str.append("3¬A¬I(1,2)");

    //    //Proof 3
    //    str.append("1A->B^Cgiven");
    //    str.append("2C->Dgiven");
    //    str.append("3Aass");
    //    str.append("4B^C->E(1,3)");
    //    str.append("5C^E(4)");
    //    str.append("6D->E(2,5)");
    //    str.append("7A->D->I(3,6)");

    // Proof 4
    //    str.append("1pass");
    //    str.append("2pvqvI(1)");
    //    str.append("3p->pvq->I(1,2)");

    // Proof 5
    //    str.append("1Fgiven");
    //    str.append("2AFE(1)");

    // Proof 6 - error on line 9
    //    str.append("1¬(p->q)given");
    //    str.append("2¬pass");
    //    str.append("3pass");
    //    str.append("4F¬E(2,3)");
    //    str.append("5qFE(4)");
    //    str.append("6p->q->I(3,5)");
    //    str.append("7F¬E(1,6)");
    //    str.append("8¬¬p¬I(2,7)");
    //    str.append("9p¬¬E(8)");

    // Proof 7
    //    str.append("1p->(q->r)given");
    //    str.append("2p^qass");
    //    str.append("3p^E(2)");
    //    str.append("4q->r->E(1,3)");
    //    str.append("5q^E(2)");
    //    str.append("6r->E(4,5)");
    //    str.append("7p^q->r->I(2,6)");

    // Proof 8
    //    str.append("1p->(q->r)given");
    //    str.append("2p->qass");
    //    str.append("3pass");
    //    str.append("4q->E(2,3)");
    //    str.append("5q->r->E(1,3)");
    //    str.append("6r->E(4,5)");
    //    str.append("7p->r->I(3,6)");
    //    str.append("8(p->q)->(p->r)->I(2,7)");

    //Proof 9
    str.append("1p^q->rgiven");
    str.append("2pass");
    str.append("3qass");
    str.append("4p^q^I(2,3)");
    str.append("5r->E(1,4)");
    str.append("6q->r->I(3,5)");
    str.append("7p->(q->r)->I(2,6)");

    ProofBreaker breaker = new ProofBreaker();
    List<String> linesFromWholeProof = breaker.breakIntoLines(str.toString());
    System.out.println("--------------------------------------------");
    System.out.println("Breaking down the whole proof and parsing lines");
    System.out.println("--------------------------------------------");
    //    System.out.println();
    //    for (String line : linesFromWholeProof) {
    //      System.out.println(parser.parseLine(line));
    //    }
    List<Line> lines =
        linesFromWholeProof.stream().map(parser::parseLine).collect(Collectors.toList());

    lines.forEach(System.out::println);

    Proof proof = new Proof(lines);
    ProofVerifier verifier = new ProofVerifier();
    int line = verifier.verify(proof);
    if (line == 0) {
      System.out.println("All fine");
    } else {
      System.out.println("Error on line " + line);
    }
  }
}
