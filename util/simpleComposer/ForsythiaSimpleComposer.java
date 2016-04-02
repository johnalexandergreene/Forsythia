package org.fleen.forsythia.util.simpleComposer;

import org.fleen.forsythia.composition.ForsythiaComposition;
import org.fleen.forsythia.grammar.ForsythiaGrammar;

public interface ForsythiaSimpleComposer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
