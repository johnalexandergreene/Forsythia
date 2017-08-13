package org.fleen.forsythia.app.bread.composer;

import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public interface Composer{
  
  //create new composition
  ForsythiaComposition compose(ForsythiaGrammar grammar);
  
  //modify existing composition
  ForsythiaComposition compose(ForsythiaComposition composition);
  
}
