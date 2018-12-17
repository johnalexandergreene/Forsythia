package org.fleen.forsythia.core.grammar;

import org.fleen.forsythia.core.Forsythia;

public interface ForsythiaGrammar extends Forsythia{
  
  /*
   * we get a jig
   * the jig fits the metagon, has the tags, has detail size >= detaillimit
   * 
   * Also
   *   the jig may have been selected from a pre-existing repository of jigs
   *   or it may have been created (and possibly saved) on the fly.
   *   it doesn't matter
   *   
   * Also, a random number generator may have been involved. That is managed elsewhere
   */
  Jig getRandomJig(FMetagon m,String[] tags, double detaillimit);

}
