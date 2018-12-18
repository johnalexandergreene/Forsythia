package org.fleen.forsythia.junk.simpleComposer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Basic.ForsythiaGrammar_Basic;

public interface ForsythiaSimpleComposer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar_Basic grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
