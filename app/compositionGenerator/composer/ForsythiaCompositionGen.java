package org.fleen.forsythia.app.compositionGenerator.composer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Basic.ForsythiaGrammar_Basic;

/*
 * creates a tree of forsythia composition nodes
 */
public interface ForsythiaCompositionGen{
  
  //create a new composition
//  ForsythiaComposition compose(ForsythiaGrammar grammar,double detaillimit);
  
  ForsythiaComposition compose();
  
//  void setGrammar(ForsythiaGrammar grammar);
//  
//  void setDetailLimit(double a);
  
  //build on an an existing composition
//  void compose(ForsythiaComposition c,double detaillimit);
  
  
  
}
