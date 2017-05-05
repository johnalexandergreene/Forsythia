# Download a copy of the executable Jar

[Fleen_Forsythia_Grammar_Editor_V2017_04_22.jar](https://github.com/johnalexandergreene/Forsythia/raw/master/app/grammarEditor/bin/FleenForsythiaGrammarEditor_V2017_04_22.jar)

Run it thusly : **java -jar FleenForsythiaGrammarEditor_V2017_04_22.jar**

# Overview

This is a tool for creating Forsythia shape grammars.

It's basically a drawing tool. You draw simple polygons and polygon-diagrams, tweak various geometry params and assign tags. 
This define the shapes and operators of the grammar (In the code these are Metagons and Jigs). 

The behavior of the grammar can be examined too. The editor incorporates a simple Forsythia fractal generator and renderer 
that uses the grammar. The resulting images can be exported as PNGs.

Grammars are are stored as serialized Java objects. They can be imported, exported and created.

Note that the editor's fractal generator and renderer is indeed just a simple thing. 
The idea is that you create a grammar and then load it into your own generation algorithm for creating images, animations or whatever. 

# UI

We have 5 windows in the editors user interface. I will address the buttons in each window, left-to-right, starting at the top left.

## GRAMMAR

![](/app/grammarEditor/doc/pix/GRAMMAR.png?raw=true)

**Grammar=nice.grammar** means that the grammar presently in use is a grammar called *nice.grammar*. 

This is the top-level interface for editing Grammars. You create, remove and edit Metagons and Jigs.

(A Metagon is a location, orientation, scale and chirality independent way of describing a polygon. A Jig is an operator in a shape grammar composed of metagons and jigs)

The 3 blue buttons **Import**, **Export** and **New** import a grammar from or export a grammar to the file system; or create a new grammar.

The yellow **Generate** button takes us to the Forsythia fractal image generating window.

Next is the **Metagons** section.

**Count=26** means that we have 26 Metagons in the grammar. 

**Jigless=0** means that there are 0 Metagons that have no Jigs. All the Metagons have at least one Jig.

**Isolated=0** means that there are 0 Metagons that are not referred to by any Jig section. That is to say, an example of every Metagon is created by one or more Jigs.

The 3 orange buttons **Create, Edit, Discard** refer to the creation, editing and discarding of Metagons.

Next is the Jigs section.

**Count=3** means that the Metagon presetly in focus (indicated by the white square outline) has 3 Jigs.

The 3 green buttons **Create, Edit, Discard** refer to the creation, editing and discarding of Jigs for the focus Metagon.

## METAGON

![](/app/grammarEditor/doc/pix/METAGON.png?raw=true)

This is where we define a Metagon for a grammar.

Draw a polygon on the grid with your mouse. Click connects a point to the last point you clicked. Click again to disconnect the autoconnecting fucction. Just mess with it. it's obvious. Onlt lines that align with the grid are valid.

The green button **Quit** means discard whatever you've done here and go back to the Grammar window.

The green button **Save** means save the metagon (if it's valid) and go back to the Grammar window.

The **MetagonTags** box is where you specify the tags for the Metagon that you are making. They are space delimited. You can edit them later if you like but the geometry of the metagon is fixed. If you want to change that then you need to delete the metagon. 

## JIG : EDIT GEOMETRY

![](/app/grammarEditor/doc/pix/JIG_editgeometry.png?raw=true)

Define a Jig for a Metagon for a grammar, part 1.

Define geometry. Draw sections in the polygon. Chop the polygon up into pieces.

The red **Quit** button discards what you've done and returns you the the Grammar window.

The orange **Edit Sections** button locks the geometry and takes us part 2 of our Jig creation process where we edit the details of the sections we drew.

The purple and green **Grid Density** buttons control the resolution of the grid that you are drawing on.

The purple and yellow **Jig Tags** box specifies the tags for this Jig. Used by the generator for filtering Jigs. I have yet to use Jig tags for anything.

## JIG : EDIT SECTIONS

![](/app/grammarEditor/doc/pix/JIG_editsections.png?raw=true)

Define a Jig for a Metagon for a grammar, part 2. Edit the details of the sections we drew. Click a section to select it.

The red **Quit** and **Save** buttons do what you'd expect and return you to the Grammar window.

The orange **Edit Geometry** button discards whateve rsection editing that you have done and returns you to the Jig Edit Geometry window.

The green **Section Chorus** button specifies the section chorus id for a section. Sections with the same id get treated the same way in the generator's jig-selection logic. This is how we get symmetry.

The green **Section Anchor** specifies the subgeometry of a section polygon. We can choose from 1 or more isomorphic polygons that differ in which point is the first point and which direction we go when we traverse the points. Note the arrow diagram. It indicates P0 and the direction of traversal. Symmetric diagrams produce symmetric results in the generator.

The green **Section Tags** box specifies tags for a section. These are passed to the created polygon. Used by the generator for polygon filtering and id.

## GENERATOR

![](/app/grammarEditor/doc/pix/GENERATOR.png?raw=true)

Generate Forsythia fractals and render them, so we can see how a grammar behaves. (This is a rather primitive stuff. For fancier stuff you write your own generator and renderer.)

The first purple button, **Go/Stop** toggle. Starts and stops the generation process.

The second purple button, **Mode=Intermittant/Continuous** toggle. Controls the generation mode.

The third purple button with the yellow box, **Interval=1000**. Controls the period between generation cycles in continuous generation mode. In milliseconds.

The blue button, **DetailFloor**, controls the floor of the detail size. Smaller value means smaller detains and a denser pattern. Larger means coarser pattern.

The orange button, **Export Image**, exports the create image to the specified export directory.

The long orange button, **Export image dir...**, opens a directory section dialog. Select export dir.

The orange button, **Export Image Size**, controls the dimensions of the exported PNG file.

The red button, **Grammar**, returns us to the Grammar window.

# Tags

Tags are used by the fractal generation algorithm to identify elements of the grammar and generated geometry.

We use two tags in the editor's generator. 

**root** : The metagon so tagged will be used as the root shape for the generated forsythia fractal. Graphically, this becomes basic shape of the generated form. For example if we tag a rectangle with *root* then generated forms will be rectangles filled with stuff. If we tag a triangle then we get triangles filled with stuff. If we tag nothing then the generator picks a shape at random. If we tag multiple shapes then the generator picks from those at random.

**egg** : The jig section so tagged is logically differentiated from the surrounding structure, as is the structure that it contains. Think of it as a level of structure, each level of nesting egg being an additional level. In our generator we use it for graphical purposes. Struture of different "egg level" get colored differently, which looks nice.

For your own purposes you can use whatever tags you like

---
---

![](/app/grammarEditor/doc/pix/s00.png?raw=true)

![](/app/grammarEditor/doc/pix/s01.png?raw=true)

![](/app/grammarEditor/doc/pix/s02.png?raw=true)

![](/app/grammarEditor/doc/pix/s03.png?raw=true)















