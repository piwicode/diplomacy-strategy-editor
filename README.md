# diplomacy-strategy-editor

This standalone editor allows designing diplomacy board game strategies. It allows editing strategy trees with multiple branches.

* * *

### Change Log

*   **April 9th, 2011**
    *   huge code cleanup leads to cleaner rendering
    *   menu for New, Open, Save, Exit, About and Help, with shortcuts
    *   smaller binary
    *   the application frame remembers its previous location and state.
*   **Match 26th, 2011**
    *   JNLP deployment provides a way to install the editor & run it off-line
    *   A desktop icon and a menu icon are created
    *   '.diplo' extension is associated to the editor to allow easy opening
    *   Save & Load now works
    *   Berlin region fixed

* * *

### Introduction

Diplomacy is a tough game. It puts forwards the difficulty to build partnerships in a conflictual situation, and all the benefits that can be retrieved when an team raise. Moreover the strategic dimension underlies all the exchanges between the players. I have written this tool to allow a player to build a strategy schema.

Play diplomacy at: [http://www.diplomatie-online.net](http://www.diplomatie-online.net)

### Funny conception tip

This tool is based on a database of countries, name shape and adjacency graph. I have build an editor to help me to create all these data. Provided I'm a single programmer I used Powerpoint extensibility to build an editor.

![](docs/images/diplo-tuto-00.JPG)

For each country I draw the shape of all places where an army can be dropped. Notice that in Spain there is only one country, but there is one place for tanks an two coasts for fleets. On each place with a resource center I draw a circle to define where the resource center icon is located. Then for each place I draw a rectangle to locate where the troop icon will be drawn. Finally for each place I write the place's name in the web text replacement field of the shape.

All these data are gathered and processed thanks to the macro entitled "cool". It writes a file in "c:\output.xml" which will be included in the applet package.

![](docs/images/diplo-applet-editor.zip)

The adjacency data has been entered by hand... It has been painfull. If you want to reuse it for another project, please let me know.

### Screen shots and basic use

The top tool palette contains the map, flags, fleets and armies. Drag items from the tool bar to the working area.

![http://pierre.labatut.googlepages.com/diplo-toolpalette.JPG/diplo-toolpalette-full.jpg](http://pierre.labatut.googlepages.com/diplo-toolpalette.JPG/diplo-toolpalette-full.jpg)

First drag a map from the tool bar to the working area.

*   It is possible to translate the working area, by pressing the middle button while moving the mouse.
*   It is also possible to zoom in and zoom out, by pressing the mouse right button while moving the mouse up and down.

![http://sites.google.com/site/pierrelabatut/diplo-tuto-01-full.jpg](http://sites.google.com/site/pierrelabatut/diplo-tuto-01-full.jpg)

Now lets drag a few troops and resource center's flags. Notice that the tanks can only be dropped on the ground and fleet only on the sea. A nice shape highlights the country that is under the mouse to ease the placement.

![http://sites.google.com/site/pierrelabatut/diplo-tuto-03-large.jpg](http://sites.google.com/site/pierrelabatut/diplo-tuto-03-large.jpg)

It is possible to remove items from a map by dropping them into the recycle. It highlights to show that the army will be removed.

[http://sites.google.com/site/pierrelabatut/diplo-tuto-02.JPG](http://sites.google.com/site/pierrelabatut/diplo-tuto-02.JPG)

Create an attack order by dragging an army over the attacked army of an adjacent place, with pressing the CTRL key. It creates an arrow that symbolizes the attach order.

![![](http://pierre.labatut.googlepages.com/diplo-tuto-04.JPG/diplo-tuto-04-large.jpg)]([http://pierre.labatut.googlepages.com/diplo-tuto-04.JPG/diplo-tuto-04-full.jpg](http://pierre.labatut.googlepages.com/diplo-tuto-04.JPG/diplo-tuto-04-full.jpg))

Create support order by dragging an army over the supported order or the supported army. The army or order highlights to let you know that the support order will be created.

![![](http://pierre.labatut.googlepages.com/diplo-tuto-05.JPG/diplo-tuto-05-large.jpg)]([http://pierre.labatut.googlepages.com/diplo-tuto-05.JPG/diplo-tuto-05-full.jpg](http://pierre.labatut.googlepages.com/diplo-tuto-05.JPG/diplo-tuto-05-full.jpg))

Now, let's show the power of this tool. Lets create the strategy tree. Take the map of the working area ad drag it to a lower place with pressing SHIFT. A new map is created and linked to the initial one. The application allows to create as many fork as required.

[![](http://sites.google.com/site/pierrelabatut/diplo-tuto-06-medium.jpg)](http://sites.google.com/site/pierrelabatut/diplo-tuto-06.JPG)

Finally let's save the work by using the save disk button.

</markdown-widget></div>

<div id="gca-project-info-box" class="maia-col-4">

<div class="maia-aside">

# Project Information

*   License: [MIT License](http://www.opensource.org/licenses/mit-license.php)
*   2 stars
*   svn-based source control

Labels:  
<span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">java</span>](/archive/search?q=domain:code.google.com label:java)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">board</span>](/archive/search?q=domain:code.google.com label:board)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">game</span>](/archive/search?q=domain:code.google.com label:game)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">applet</span>](/archive/search?q=domain:code.google.com label:applet)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">map</span>](/archive/search?q=domain:code.google.com label:map)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">diplomacy</span>](/archive/search?q=domain:code.google.com label:diplomacy)</span> <span ng-repeat="label in projectCtrl.project.labels track by $index" class="ng-scope">[<span class="gca-label ng-binding">strategy</span>](/archive/search?q=domain:code.google.com label:strategy)</span>

</div>

</div>

</div>

</div>

</div>

</div>

</div>
