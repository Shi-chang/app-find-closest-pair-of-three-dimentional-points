# Overview
<p>Finding the closest pair of two objects in three-dimensional space has a lot of practical use in real life, such as calculating aircraft collision probabilities at an airport. Finding the closest pair of three-dimensional objects takes O(n2) running time if we use the naïve method (two iterations comparing all possible cases). This program aims to improve the time complexity of the searching. </p>
<p>The algorithm applied in the program uses the methodology of divide and conquer. It divides the zone containing points into two halves, finds the smallest distance between points, uses the found distance to build an imaginary special strip zone and searches the strip zone to update the smallest distance when necessary, and combines the results. The process goes on recursively until the bases cases are hit and results are returned. </p>
<p>This program can find the pair of three-dimensional points with shortest Euclidean distance among the points provided in O(n*logn) time. </p>
# What I have learned
1. An approach to improving the time complexity for similar problems.
2. Computer science is an interdisciplinary subject when dealing with real life problems, and some of my previous skills in civil engineering (like my spatial intelligence) can be transferred into this field.
# Acknowledgements
I would like to thank the Department of Computer Science of Princeton University for providing the free explanation materials on this topic https://www.cs.princeton.edu/~wayne/kleinberg-tardos/pearson/05DivideAndConquer.pdf.
