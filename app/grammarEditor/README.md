This is a tool for creating Forsythia shape grammars.

It's basically a drawing tool. You draw simple polygons and polygon-diagrams, tweak various geometry params and assign tags. 
This define the shapes and operators of the grammar. 

The behavior of the grammar can be examined too. The editor incorporates a simple Forsythia fractal generator and renderer 
that uses the grammar. The resulting images can be exported as PNGs.

Grammars are are stored as serialized Java objects. They can be imported, exported and created.

Note that the editor's fractal generator and renderer is indeed just a simple thing. 
The idea is that you create a grammar and then load it into your own generation algorithm for creating images, animations or whatever. 










