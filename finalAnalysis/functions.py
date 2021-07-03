# Imports
import pandas as pd                     # For dataframes
import seaborn as sns                   # For plots
from matplotlib import pyplot as plt    # For plots

plt.style.use('seaborn')


class analysis:

    def __init__(self, runs, seed, initialTrade, incrementTrade, initialTax, incrementTax, preset):
        self.runs = runs
        self.seed = seed
        self.initialTrade = initialTrade
        self.incremenetTrade = incrementTrade
        self.initialTax = initialTax
        self.incrementTax = incrementTax
        self.preset = preset

    def getJarArguments(self):
        out = ' ' + str(self.runs) + ' ' + str(self.seed)
        # TODO add more - both here and in the model
        return out

    def plotArea(self):
        taxC, taxE = self.__makePlots(True)
        tradeC, tradeE = self.__makePlots(False)
        return taxC, taxE, tradeC, tradeE 


    def getCE(self, isTax):
        return self.__getCE(isTax)

    def plotComparison(self):
        taxMeanC, taxMeanE = self.__getCE(True)
        tradeMeanC, tradeMeanE = self.__getCE(False)
        taxMeanC = taxMeanC.sum(axis=1)
        tradeMeanC = tradeMeanC.sum(axis=1)        
        ax1 = sns.lineplot(hue="region", data=taxMeanC, ci='sd')
        ax2 = sns.lineplot(hue="region", data=tradeMeanC, ci='sd')
        ax2.set(xlabel="Time (ticks)", ylabel="Carbon Dioxide (million tonnes)")
        plt.show()
        taxMeanE = taxMeanE.sum(axis=1)
        tradeMeanE = tradeMeanE.sum(axis=1) 
        ax1 = sns.lineplot(hue="region", data=taxMeanE, ci='sd')
        ax2 = sns.lineplot(hue="region", data=tradeMeanE, ci='sd')
        ax2.set(xlabel="Time (ticks)", ylabel="Electricity Generated (TWh)")
        plt.show()

    # Builds the filepath from the input variables
    def __buildFilePath(self, isTax):
        filepath = 'seed-' + str(self.seed)
        if isTax:
            filepath += '-tax-' + self.initialTax + '-inc-' + self.incrementTax
        else:
            filepath += '-trade-' + self.initialTrade + '-inc-' + self.incremenetTrade
        filepath += '-preset-' + self.preset + '.csv'
        return filepath

    def __getDFwithInput(self, isTax):
        filepath = self.__buildFilePath(isTax)
        df = self.__getDF(filepath)
        return df

    def __getDF(self, filepath):
        df = pd.read_csv(filepath)
        df["Coal_Carbon"] = df["Coal_Carbon"] * 52 / 1000000
        df["Coal_Electricity"] = df["Coal_Electricity"] * 52 / 1000
        df["Gas_Carbon"] = df["Gas_Carbon"] * 52  / 1000000
        df["Gas_Electricity"] = df["Gas_Electricity"] * 52 / 1000
        df["Nuclear_Carbon"] = df["Nuclear_Carbon"] * 52 / 1000000
        df["Nuclear_Electricity"] = df["Nuclear_Electricity"] * 52 / 1000
        df["Wind_Carbon"] = df["Wind_Carbon"] * 52 / 1000000
        df["Wind_Electricity"] = df["Wind_Electricity"] * 52 / 1000
        return df

    def __makePlots(self, isTax):
        # df = self.__getCombinedDF(isTax)
        carbon, electricity = self.__getCE(isTax)
        carbon = carbon.groupby(carbon.index)
        carbon = carbon.mean()
        carbon = carbon.rename(columns={'Coal_Carbon'   : 'Coal', 
                                        'Gas_Carbon'    : 'Gas',
                                        'Nuclear_Carbon': 'Nuclear',
                                        'Wind_Carbon'   : 'Wind'})
        electricity = electricity.groupby(electricity.index)
        electricity = electricity.mean()
        electricity = electricity.rename(columns={'Coal_Electricity'   : 'Coal', 
                                        'Gas_Electricity'    : 'Gas',
                                        'Nuclear_Electricity': 'Nuclear',
                                        'Wind_Electricity'   : 'Wind'})
        cPlot = carbon.plot.area()
        plt.xlabel("Time (weeks)")
        plt.ylabel("Carbon Dioxide emmitted (million tonnes)")
        ePlot = electricity.plot.area()
        plt.xlabel("Time (weeks)")
        plt.ylabel("Electricity generated (TWh)")

        return cPlot, ePlot

    def __getCE(self, isTax):
        df = self.__getCombinedDF(isTax)
        carbon = df.filter(regex='Carbon$', axis=1)
        electricity = df.filter(regex='Electricity$', axis=1)
        return carbon, electricity

    def __getCombinedDF(self, isTax):
        df = self.__getDFwithInput(isTax)

        for i in range(0, self.runs):
            curr = self.seed + i
            temp = self.__getDFwithInput(isTax)
            df = pd.concat((df, temp))

        return df
