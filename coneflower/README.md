Coneflower is a nesting-kisrhombille and grammar based geometry generator, like Forsythia

It extends ForsythiaGrammar.

To the space of Forsythia's handmade jigs it adds dynamically created jigs and algorithmically created jigs.
 
New metagons too. Generally weird products of weird Jigs. The grammar may get big.

So we have the ConeflowerGrammar class, which extends ForsythiaGrammar, and takes a ForsythiaGrammar as a construction parameter. 
Fluffing up the handmade geometry.

We will have new kinds of Jigs. Boilers. Maybe Foamers. Probably other stuff too.

---------------------------------------------

Coneflower is a subclass of ForsythiaGrammar

It takes a ForsythiaGrammar as a constructor param, and maybe some other params like boiling unit (the unit span of fat in boiling).

Then it creates a bunch of boiler-Jigs for certain metagons.

Or even it creates them on the fly, when requested.

It creates other Jigs too. Foamers and weird space fillers.
All the jigs that can be created well algoritmically (as opposed to what we have in forsythia, which is the all the jigs that can be creates well manually).



ConeflowerGrammar(ForsythiaGrammar fg,double unitfatspan)

--------------

/*
returns a random jig, for the specified metagon, with the specified tags, fitting the specified detaillimit, from the set of all possible jigs
these could be handmadejigs from the forsythiagrammar or they could be generated jigs from the coneflower logic. 
*/
ForsythiaGrammar.getRandomJig(Metagon m,String[] tags, double detaillimit)

override that method from ForsythiaGrammar in Coneflower

(create it first in ForsythiaGrammar if we need to)


#################################
#################################
OLD STUFF LAYS BELOW
#################################
#################################
-------------------------

It takes a Forsythia grammar, that handmade thing, and expands it.
  Adding new, algorithmically devised, metagons and jigs
  
In this game we could say that Forsythia provides the splitters and here we provide the boilers and crushers. Stuff with yards.

---------- 

Coneflower is a geometry generator, like Forsythia

It uses Forsythia. It uses a Forsythia Grammar.

It's a fluffer. It fluffs geometry. It expands geometry. It takes Forsythia geometry and from it derives new geometry.

Our generation process goes
  1) given a target polygon
  2) Do forsythia jigging for 1 or 2 cycles
  3) Do coneflower fluffing
  Address steps 1,2,3 to any resulting forsythia polygons.
  And so on.
  
So our generation process goes like jigger, jigger, fluff, jigger, fluff, jigger, jigger, etc...  
  
OUR PROCESS HAS 2 PARTS

  SOFTEN
  It rounds the corners. It turns our shard hexy geometry into nice organic curves. How sharp? Constant or variable?
  
  BOIL
  It turns complexes of forsythia (simple or complex) into foams.
  That is, we expand the edge of a polygon, thus converting its own form into a LAKE and its children to RAFTS.
  The expansion could also happen at the child or grandchild level. Thus creating a more complex system of rafts. We have options.