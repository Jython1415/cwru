import math
import random
from statistics import mean
from src.IrisData import *
import matplotlib.colors as mcolors
import matplotlib.pyplot as plt
import numpy as np
   
# Class the represents a cluster with a centroid and the points associated with it
class Cluster:
    
    def __init__(self, centroid = None):
        
        if type(centroid) == tuple:
            self.centroid = centroid
        elif type(centroid) == list:
            self.centroid = tuple(centroid)
        else:
            raise Exception(f"\"centroid\" parameter must be either a tuple or a list. {type(centroid)} is not allowed.")
        
        self.points = []
    
    def setCentroid(self, newCentroid):
        self.centroid = newCentroid
    
    def getCentroid(self):
        return self.centroid
    
    def addPoint(self, point):
        self.points.append(point)
    
    def clearPoints(self):
        self.points.clear()
        
    def getPoints(self):
        return self.points
    
    # Calculates the mean in each dimension
    def findCentroid(self):
        self.setCentroid(tuple([mean(column) for column in zip(*self.getPoints())]))
        
        
class KMeansClustering:
    
    data:Dataset = None         # Stores the data (points and labels)
    clusters:list[Cluster] = [] # Stores the k clusters
    
    # Output data
    objFunc = []
    
    # rawData must be an array with float parameters and one label entry (str or int)
    def __init__(self, rawData:IrisData, k = 3, maxItr = 1000):
        self.k = k
        self.maxItr = maxItr
        self.data = rawData.getData()
        
    def objectiveFunction(self):
        total = 0 # Holds the sum of the objective function
        
        # Make sure the centroids are up-to-date
        for cluster in self.clusters:
            cluster.findCentroid()
        
        # Iterate over all the points
        for point in self.data.getPoints():
            # Add the distance to the closest centroid
            try:
                total += min([math.dist(cluster.getCentroid(), point) ** 2 for cluster in self.clusters])
            except:
                raise Exception("\n".join([f"{len(cluster.getCentroid())}, {len(point)}" for cluster in self.clusters]))
        
        return total
    
    def initialize(self):
        self.clusters.clear()
        self.objFunc.clear()
        
        # Create k random clusters
        for _ in range(self.k):
            randomPosition = []
            for i in range(len(self.data.getPoints()[0])):
                allPoints = [point[i] for point in self.data.getPoints()]
                randomPosition.append(random.uniform(min(allPoints), max(allPoints))) # Something between the min and max
            self.clusters.append(Cluster(randomPosition))
    
    def iterate(self):
        # Assign points to clusters
        for cluster in self.clusters:
            cluster.clearPoints()
        for point in self.data.getPoints():
            distances = [math.dist(cluster.getCentroid(), point) ** 2 for cluster in self.clusters] # Calculate distances
            self.clusters[distances.index(min(distances))].addPoint(point)                          # Select the cluster that is the closest
        
        # Find new centroid for each cluster
        for cluster in self.clusters:
            cluster.findCentroid()
    
    def run(self, withFigure = True):
        self.initialize()
        
        for iter in range(self.maxItr):
            self.iterate()
            
            # Record data
            self.objFunc.append(self.objectiveFunction())
            if withFigure:
                self.plotClusters(iter + 1)
            
            # Check end condition
            if len(self.objFunc) >= 2 and self.objFunc[-1] == self.objFunc[-2]:
                break
        
    # Plotting function for convenience
    def plotClusters(self, iterNum = -1, shading = False):
        # Get the colors
        colors = sorted(mcolors.TABLEAU_COLORS, key=lambda c: tuple(mcolors.rgb_to_hsv(mcolors.to_rgb(c))))
        colors = list(reversed(colors))
        colors = [colors[i*3 % len(colors)] for i in range(len(colors))]
        colors = colors[:len(self.clusters)]
        
        # Plot the values
        fig, ax = plt.subplots()
        if not shading:
            for i, cluster in enumerate(self.clusters):
                ax.scatter([point[2] for point in cluster.getPoints()], [point[3] for point in cluster.getPoints()], s=10, color=colors[i])
                ax.scatter(cluster.getCentroid()[2], cluster.getCentroid()[3], s=30, color='r', alpha=.5)
        else:
            columns = list(zip(*self.data.getPoints()))
            x = columns[2]
            y = columns[3]
            numDots = 55
            
            xSpread = np.linspace(min(x), max(x), numDots)
            ySpread = np.linspace(min(y), max(y), numDots)
            
            for xVal in xSpread:
                for yVal in ySpread:
                    distances = [math.dist(cluster.getCentroid()[2:], (xVal, yVal)) ** 2 for cluster in self.clusters]
                    i = distances.index(min(distances))
                    ax.scatter(xVal, yVal, color=colors[i])
        
        ax.set_xlabel('Petal Length')
        ax.set_ylabel('Petal Width')
        ax.set_title('Petal Length vs Width with Colored Clusters' + (f": Iteration {iterNum}" if iterNum != -1 else ""))
        
        return fig