class Dataset:
    
    def __init__(self, listOfEntries):
        self.data = listOfEntries
        self.savedData = []
    
    def getPoints(self):
        return [entry.getParams() for entry in self.data]
    
    def getLabels(self):
        return [entry.getLabel() for entry in self.data]
    
    def switchToNeuralNetwork(self):
        self.savedData.clear()
        
        toRemove = []
        for i, entry in enumerate(self.data):
            if entry.label == 0:
                toRemove.append(i)
        for index in reversed(toRemove):
            self.savedData.append(self.data.pop(index))
        for entry in self.data:
            entry.label -= 1
    
    def switchToKMean(self):
        for entry in self.data:
            entry.label += 1
        self.data.extend(self.savedData)
        self.savedData.clear()
                
    
# Class to import, parse, and access data from the Iris dataset
class IrisData:
    
    data = []
    dataset = None
    
    def __init__(self):
        self.importData()
    
    def importData(self):
        with open("data/iris.data", "r") as dataFile:
            lines = dataFile.read().split("\n")
        self.data = [[float(x) for x in i.split(",")[:4]] for i in lines]
        for i, line in enumerate(lines):
            self.data[i].append(line.split(",")[4])

    def getData(self) -> Dataset:
    
        paramIndices = []
        labelIndex = -1   # Stores where the label is in the raw data
        data = []
        
        for index, entry in enumerate(self.data[0]):
            if type(entry) == float:
                paramIndices.append(index)
            elif type(entry) == str or type(entry) == int:
                if labelIndex == -1:
                    labelIndex = index
                else:
                    raise Exception(f"data has more than one label: {labelIndex} and {index}")
        
        for row in self.data:
            data.append(DataEntry([row[i] for i in paramIndices], row[labelIndex]))
    
        self.dataset = Dataset(data)
        return self.dataset
    
    def __str__(self):
        lines = [", ".join(line) for line in self.getData()]
        return "\n".join(lines)
    
class DataEntry:
    
    def __init__(self, params, label, labelMapping = {"Iris-setosa": 0, "Iris-versicolor": 1, "Iris-virginica": 2}):
        
        if type(params) == list:
            self.params = tuple(params)
        elif type(params) == tuple:
            self.params = params
        else:
            raise Exception(f"\"params\" parameter must be either a tuple or a list. {type(params)} is not allowed.")
        
        if label in labelMapping:
            self.label = labelMapping[label]
        else:
            raise Exception(f"label {label} could not be found in the label mapping: {labelMapping.keys()}")
        
    def getParams(self):
        return self.params
    
    def getLabel(self):
        return self.label
    
    def getEntry(self):
        return (self.params, self.label)