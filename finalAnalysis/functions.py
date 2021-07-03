# Imports
import pandas as pd                     # For dataframes
import seaborn as sns                   # For plots
from matplotlib import pyplot as plt    # For plots

plt.style.use('seaborn')

# Gets the dataframe from a filepath, updates the values to show per-year amounts, sets electricity to TWh from GWh, sets carbon to million tonnes
def getDF(filepath):
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

# Splits data into two data frames, one for CO2 emissions, one for Electricity production
def getCE(df):
    carbon = df.filter(regex='Carbon$', axis=1)
    electricity = df.filter(regex='Electricity$', axis=1)
    return carbon, electricity

# Builds the filepath from the input variables
def buildFilePath(seed, isTax, initial, increment, preset):
    filepath = 'seed-' + str(seed)
    filepath += '-tax-' if isTax else '-trade-'
    filepath += initial + '-inc-' + increment
    filepath += '-preset-' + preset + '.csv'
    return filepath

# Builds the string of the java call arguments
def getJarArguments(runs, seed, isTax, initial, increment, preset):
    out = ' ' + str(runs) + ' ' + str(seed) + ' '
    out += '1' if isTax else '0'
    # TODO add more - both here and in the model
    return out

# Reads in a CSV file into a dataframe with a filename built from the arguments
def getDFwithInput(seed, isTax, initial, increment, preset):
    filepath = buildFilePath(seed, isTax, initial, increment, preset)
    df = getDF(filepath)
    return df

# Reads in and combines all CSVs into a single dataframe
def getCombinedDF(seed, isTax, initial, increment, preset, runs):
    df = getDFwithInput(seed, isTax, initial, increment, preset)

    for i in range(0,runs):
        curr = seed + i
        temp = getDFwithInput(curr, isTax, initial, increment, preset)
        df = pd.concat((df, temp))

    return df


def makePlots(seed, isTax, initial, increment, preset, runs):
    df = getCombinedDF(seed, isTax, initial, increment, preset, runs)
    carbon, electricity = getCE(df)
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
    plt.ylabel("Carbon Dioxide emmitted (1000 tonnes)")
    ePlot = electricity.plot.area()
    plt.xlabel("Time (weeks)")
    plt.ylabel("Electricity generated (TWh)")

    return cPlot, ePlot