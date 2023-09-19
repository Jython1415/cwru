import numpy as np
import math
from statistics import mean
from matplotlib import pyplot as plt

# Represent a single-layer neural network. Since there are no hidden layers, the implementation is very simplistic.
class NeuralNetwork:
    
    def __init__(self, inputSize = 4, weights:np.ndarray = None, bias = 0):
        self.inputSize = inputSize
        
        if type(weights) != np.ndarray:
            self.weights = np.asarray([-3.0, -2.1, 4.5, 3.8]) # Default starting values that make the computations more convenient
        elif weights.shape == np.zeros((self.inputSize)).shape:
            self.weights = weights.copy()
        else:
            raise Exception(f"Shape must be ({self.inputSize}), not {weights.shape}")
        
        self.bias = bias
    
    @staticmethod
    def sigmoid(value):
        return 1.0 / (1 + math.e ** (-value))

    def predict(self, x):
        # Check input
        if type(x) == tuple:
            x = np.asarray(x)
        if x.shape != self.weights.shape:
            raise Exception(f"Shape must be ({self.inputSize}), not {x.shape}")

        # Return prediction based on the weights
        return NeuralNetwork.sigmoid(np.dot(x, self.weights) + self.bias)
    
class Training:
    
    def __init__(self):
        pass
    
    @staticmethod
    def MSE(data, neuralNetwork:NeuralNetwork, expectedValues):
        errors = [(expectedValues[i] - neuralNetwork.predict(paramSet)) ** 2 for i, paramSet in enumerate(data)]
        return mean(errors)

    @staticmethod
    def gradient(data, neuralNetwork:NeuralNetwork, expectedValues):
        gradient = []
        for i in range(len(data[0])):
            sum = 0
            for j, paramSet in enumerate(data):
                prediction = neuralNetwork.predict(paramSet)
                sum += (expectedValues[j] - prediction) * prediction * (1 - prediction) * paramSet[i]
            gradient.append(-2 * sum / len(data))
        
        sum = 0
        for j, paramSet in enumerate(data):
            prediction = neuralNetwork.predict(paramSet)
            sum += (expectedValues[j] - prediction) * prediction * (1 - prediction)
        
        return (np.asarray(gradient), -2 * sum / len(data))
    
    def train(data, neuralNetwork:NeuralNetwork, expectedValues, maxItr = 10):
        
        MSERecords = []
        for itr in range(maxItr):
            gradient, val = Training.gradient(data, neuralNetwork, expectedValues)
            neuralNetwork.weights -= gradient
            neuralNetwork.bias -= val
            if itr % (maxItr/50) == 0:
                MSE = Training.MSE(data, neuralNetwork, expectedValues)
                print(f"{itr} --- {[round(i, 4) for i in gradient]} --- {round(MSE, 4)} --- {neuralNetwork.weights}")
                MSERecords.append((itr, MSE))
        
        return MSERecords

    @staticmethod
    def plotClassification(data, neuralNetwork:NeuralNetwork):
        classifications = [neuralNetwork.predict(paramSet) for paramSet in data]
        classifications = [1 if prediction > .5 else 0 for prediction in classifications]
        
        _, ax = plt.subplots()
        colors = {0: "r", 1: "b"}
        for i, paramSet in enumerate(data):
            ax.scatter(paramSet[2], paramSet[3], color=colors[classifications[i]])
        
        ax.set_xlabel('Petal Length')
        ax.set_ylabel('Petal Width')
        ax.set_title('Petal Length vs Width with Classifications by the Neural Network')
            