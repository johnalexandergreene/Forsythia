package org.fleen.forsythia.junk.simpleComposer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Simple.ForsythiaGrammar_Simple;

public interface ForsythiaSimpleComposer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar_Simple grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
