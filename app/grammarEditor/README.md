## Overview

This is a tool for creating Forsythia shape grammars.

It's basically a drawing tool. You draw simple polygons and polygon-diagrams, tweak various geometry params and assign tags. 
This define the shapes and operators of the grammar (In the code these are Metagons and Jigs). 

The behavior of the grammar can be examined too. The editor incorporates a simple Forsythia fractal generator and renderer 
that uses the grammar. The resulting images can be exported as PNGs.

Grammars are are stored as serialized Java objects. They can be imported, exported and created.

Note that the editor's fractal generator and renderer is indeed just a simple thing. 
The idea is that you create a grammar and then load it into your own generation algorithm for creating images, animations or whatever. 

## UI

![](/app/grammarEditor/doc/pix/GRAMMAR.png?raw=true)

![](/app/grammarEditor/doc/pix/METAGON.png?raw=true)

![](/app/grammarEditor/doc/pix/JIG_editgeometry.png?raw=true)

![](/app/grammarEditor/doc/pix/JIG_editsections.png?raw=true)

![](/app/grammarEditor/doc/pix/GENERATOR.png?raw=true)

## Tags

Tags are used by the fractal generation algorithm to identify elements of the grammar and generated geometry.

We use two tags in the editor's generator. 

**root** : The metagon so tagged will be used as the root shape for the generated forsythia fractal. Graphically, this becomes basic shape of the generated form. For example if we tag a rectangle with *root* then generated forms will be rectangles filled with stuff. If we tag a triangle then we get triangles filled with stuff. If we tag nothing then the generator picks a shape at random. If we tag multiple shapes then the generator picks from those at random.

**egg** : The jig section so tagged is logically differentiated from the surrounding structure, as is the structure that it contains. Think of it as a level of structure, each level of nesting egg being an additional level. In our generator we use it for graphical purposes. Struture of different "egg level" get colored differently, which looks nice.

For your own purposes you can use whatever tags you like














