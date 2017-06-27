import numpy as np
import matplotlib.pyplot as plot
import sys
plotfile = sys.argv[1]
plot.plot(*np.loadtxt(plotfile,unpack=True), linewidth=2.0)
plot.xlabel("Number of Guesses")
plot.ylabel("Information Leakage (bits)")
plot.title("Total Leakage vs. Number of Guesses")
plot.show()
