import pandas as pd
import seaborn as sns
import pingouin as pg
from matplotlib import pyplot as plt
from functions.functions import analysis

# Combines the data from the model to produce comparison plots and statistics
class combine:

    # Constructor, stores the information for each run to be compared 
    def __init__(self, preset):
        self.preset = preset
        call = "java -jar ../model.jar"
        self.a1 = analysis(100, 1, 30, 1, 15, self.preset)
        self.a2 = analysis(100, 1, 60, 2.5, 15, self.preset)
        self.a3 = analysis(100, 1, 120, 5, 15, self.preset)
        self.call = str(call + self.a1.getJarArguments() + " && " + 
                        call + self.a2.getJarArguments() + " && " + 
                        call + self.a3.getJarArguments())

    def get_data_call(self):
        return self.call

    def __get_clean_csvs(self):
        list_a = [self.a1, self.a2, self.a3]
        list_b = [[30, 1], [60, 2.5], [120, 5]]
        for key in list_a:
            cTax, temp = key.getCE(True)
            cTrade, temp = key.getCE(False)
            self.__clean_csv_helper(cTax, cTrade, list_b[list_a.index(key)])                


    def __clean_csv_helper(self, cTax, cTrade, rates):
        from pathlib import Path
        cTax['Ticks'] = cTax.index + 1
        cTrade['Ticks'] = cTrade.index + 1
        cTax["Total Carbon Dioxide"] = cTax["Coal_Carbon"] + cTax["Gas_Carbon"] + cTax["Wind_Carbon"] + cTax["Nuclear_Carbon"]
        cTrade["Total Carbon Dioxide"] = cTrade["Coal_Carbon"] + cTrade["Gas_Carbon"] + cTrade["Wind_Carbon"] + cTrade["Nuclear_Carbon"]
        filename = str(rates[0]) + '.csv'
        cTax.to_csv(Path('temp/clean/c' + filename))       
        filename = str(rates[1]) + ".csv"
        cTrade.to_csv(Path('temp/clean/c' + filename))

    def get_plots(self):
        self.__get_clean_csvs()
        df = self.__get_plot_df()
        ax1 = sns.lineplot(x="Ticks", y="c30", data=df, ci='sd', label="Tax: 30 euros per tonne", color="mediumturquoise")
        ax2 = sns.lineplot(x="Ticks", y="c60", data=df, ci='sd', label="Tax: 60 euros per tonne", color="deepskyblue")
        ax3 = sns.lineplot(x="Ticks", y="c120", data=df, ci='sd', label="Tax: 120 euros per tonne", color="cornflowerblue")

        ax4 = sns.lineplot(x="Ticks", y="c1_0", data = df, ci='sd', label="Trade: 1.0% reduction per year", color="gold")
        ax5 = sns.lineplot(x="Ticks", y="c2_5", data = df, ci='sd', label="Trade: 2.5% reduction per year", color="orange")
        ax6 = sns.lineplot(x="Ticks", y="c5_0", data = df, ci='sd', label="Trade: 5.0% reduction per year", color="chocolate")
        ax6.set(xlabel="Time (weeks)", ylabel="Mean Carbon Dioxide Emission $\pm$ 1 sd (million tonnes)", title=("Yearly CO2 Emission Rates Over Time, " + self.__get_preset_readable() + " initial state"))
        plt.savefig(fname=(self.preset + ".pdf"), format="pdf")
        plt.show()

    
    def __get_plot_df(self):
        c30 = pd.read_csv('temp/clean/c30.csv')
        c60 = pd.read_csv('temp/clean/c60.csv')
        c120 = pd.read_csv('temp/clean/c120.csv')
        c1_0 = pd.read_csv('temp/clean/c1.csv')
        c2_5 = pd.read_csv('temp/clean/c2.5.csv')
        c5_0 = pd.read_csv('temp/clean/c5.csv')

        df = c30.drop(columns=['Unnamed: 0', 'Coal_Carbon', 'Nuclear_Carbon', 'Wind_Carbon', 'Gas_Carbon'])
        df = df.rename(columns={"Total Carbon Dioxide" : "c30"})
        df['c60'] = c60["Total Carbon Dioxide"]
        df['c120'] = c120["Total Carbon Dioxide"]
        df['c1_0'] = c1_0["Total Carbon Dioxide"]
        df['c2_5'] = c2_5["Total Carbon Dioxide"]
        df['c5_0'] = c5_0["Total Carbon Dioxide"]

        return df

    def __get_preset_readable(self):
        if self.preset == "equal": return "equal"
        elif self.preset == "EU-2016": return "European Union 2016"
        elif self.preset == "US-2007": return "United States 2007"
        elif self.preset == "seventy": return "70% fossil fuel"
        elif self.preset == "ninety": return "90% fossil fuel"
        elif self.preset == "fifty": return "50% fossil fuel"
        else: return "cannot find preset"

    def statistics(self):
        df = self.__get_plot_df()

        five    = self.__adapt_df(df.loc[df["Ticks"] == 260].drop(["Ticks"], axis=1))
        ten     = self.__adapt_df(df.loc[df["Ticks"] == 520].drop(["Ticks"], axis=1))
        fifteen = self.__adapt_df(df.loc[df["Ticks"] == 780].drop(["Ticks"], axis=1))

        dv = "Carbon"
        btwn = "Measure"

        tt5  = pg.pairwise_ttests(dv=dv, between=btwn, data=five)
        tt10 = pg.pairwise_ttests(dv=dv, between=btwn, data=ten)
        tt15 = pg.pairwise_ttests(dv=dv, between=btwn, data=fifteen)

        return tt5, tt10, tt15

    def __adapt_df(self, df):
        adapted = []
        cols = ['c30', 'c60', 'c120', 'c1_0', 'c2_5', 'c5_0']
        for col in cols:
            if col == 'c30':
                adapted = self.__sort_out(col, df, cols)
            else:
                adapted = adapted.append(self.__sort_out(col, df, cols))
        return adapted

    def __sort_out(self, col, df, cols):
        temp = df
        temp['Measure'] = col
        to_drop = []
        for col_2 in cols:
            if col != col_2:
                to_drop.append(col_2)
        temp = temp.drop(to_drop, axis=1)
        temp = temp.rename(columns={col: "Carbon"})
        return temp