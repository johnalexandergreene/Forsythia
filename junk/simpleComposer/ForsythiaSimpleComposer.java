package org.fleen.forsythia.junk.simpleComposer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public interface ForsythiaSimpleComposer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
