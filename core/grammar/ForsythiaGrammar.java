package org.fleen.forsythia.core.grammar;

import org.fleen.forsythia.core.Forsythia;

/*
 * Access interface for a library of metagons and jigs
 * 
 * For each Metagon we have a space of 0..n associated Jigs. 
 * Each Jig is associated with 1 Metagon
 * 
 * the jig may have been selected from a pre-existing repository of jigs
 * or it may have been created (and possibly saved) on the fly.
 * it doesn't matter
 * 
 * So this can be a dynamic data thing too. Growing after construction. It might get big. 
 */
public interface ForsythiaGrammar extends Forsythia{
  
  FMetagon getRandomMetagon(String[] tags);
  
  /*
   * we get a jig
   * the jig fits the metagon, has the tags, has detail size >= detailfloor
   * 
   * TODO Our detail limit value should be normalized somehow
   * I mean, we'll be using the detail size gotten from the unscaled metagon default polygon.
   * Presently we refer to the polygon within the composition structure. 
   *   We shouldn't do that. We should normalize it first. This should be doable.
   * 
   * By random we mean arbitrary. Our selection has no necessary relationship with the space from which the selection is drawn. 
   */
  Jig getRandomJig(FMetagon m,String[] tags, double detailfloor);

}
