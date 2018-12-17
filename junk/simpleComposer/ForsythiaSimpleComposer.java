package org.fleen.forsythia.junk.simpleComposer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar0;

public interface ForsythiaSimpleComposer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar0 grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
