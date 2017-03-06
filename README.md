## UFOTracker: Visualizing UFO sightings
UFO_tracker is a visual analytics tool for analyzing unidentified flying object sightings from the National UFO Reporting Center. The goal here is to give the user a higher level view of where different types of sightings occur, to investigate whether sightings are increasing or decreasing over time, and to discover the connections between different events which might happen at different geographic areas. Multiple visualization and data mining techniques are combined to make sense the increasingly large UFO reports which get updated hourly.  
![ScreenShot](https://github.com/iDataVisualizationLab/UFO/blob/master/images/maingit.png)

### Implemenation
UFOTracker is implemented in [Processing](http://processing.org) and Java <br>
Links for implementation: <br>
a. Linux operating system - please click [Linux](http://myweb.ttu.edu/vinhtngu/UFO/application.linux.zip).</br>
b. Windows operating system - please click [Windows](http://myweb.ttu.edu/vinhtngu/UFO/application.windows.zip).</br>
c. MacOS operating system - please click [MacOS](http://myweb.ttu.edu/vinhtngu/UFO/applicationMac.zip).
 
### User story
Our text analytics window provides users a summary of topics in extracted from the large corpus of sighting descriptions. Popular topics are extracted using Latent Dirichlet Allocation (LDA) method along with Java-based topic modeling package
In this graph, user can easily  spot the  correlations  between  the  selected  sightings. As shown in Figure below, top 10 shown popular topics give recommendation of general topic of UFO sightings (as depicted in the Figure below)
![Everything Is AWESOME](https://github.com/iDataVisualizationLab/UFO/blob/master/images/AZ.png)


### References
[1] T. Dang and L. Wilkinson, “TimeExplorer: Similarity search time seriesby their signatures,” in Proc. International Symp. on Visual Computing,2013, pp. 280–289

[2] C. McNaught and P. Lam, “Using wordle as a supplementary researchtool,” The qualitative report, vol. 15, no. 3, 2010, p. 630

[3] Mallet.   Topic   modeling   for   java   developers.   [Online].   Available:http://mallet.cs.umass.edu/topics-devel.php (2017)

[4] R.   J.   Gribble.   National   ufo   report   center.   [Online].   Available:http://www.nuforc.org (2017)
