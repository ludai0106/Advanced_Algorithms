import csv
import numpy as np
import copy
import matplotlib.pyplot as plt
from math import *
import pandas as pd
import plotly.plotly as py
import plotly.graph_objs as go



def processData(data):
    t=0
    exact = [0 for i in range(20)]
    approximation = [0 for i in range(20)]
    greedy = [0 for i in range(20)]
    epsilon = 0
    for row in data:
        if t==0:
            t=t+1
            continue
        else:
            epsilon = row[3]
            index = (int(row[0]))/5-1
            exact[index]+=float(row[4])
            approximation[index]+=float(row[5])
            greedy[index]+=float(row[6])
    exact[:]=[i/25.0 for i in exact]
    approximation[:]=[i/25.0 for i in approximation]
    greedy[:]=[i/25.0 for i in greedy]   
    return exact, approximation, greedy, epsilon


files=[]
files.append(open("01.csv"));
files.append(open("05.csv"));
files.append(open("1.csv"));
files.append(open("10.csv"));
files.append(open("25.csv"));
files.append(open("50.csv"));
files.append(open("100.csv"));

# x=[]
# y_ex=[]
# RDD=[]
# TF=[]
# runtimeData(files[6],x,y_ex,RDD,TF)


exact_all = []
approximation_all = []
greedy_all = []
epsilon_all = []

for i in files:
    data = csv.reader(i)
    exact, approximation, greedy, epsilon = processData(data)
    exact_all.append(exact)
    approximation_all.append(approximation)
    greedy_all.append(greedy)
    epsilon_all.append(epsilon)

exact=[0 for i in range(20)]
greedy=[0 for i in range(20)]
for i in range(7):
    for j in range(20):
        exact[j]+=exact_all[i][j]
        greedy[j]+=greedy_all[i][j]

exact[:] = [i/7.0 for i in exact]
greedy[:] = [i/7.0 for i in greedy]

instance_size = [ (i+1)*5 for i in range(20)]


f, ax = plt.subplots(1, 3, sharey=True )
ax[0].set_ylabel("Tardiness")
ax[0].set_xlabel('instance size')
ax[1].set_xlabel('instance size')
ax[2].set_xlabel('instance size')


ax[0].plot(instance_size, greedy, 'r-')
ax[0].set_title('Greedy algorithm')
ax[1].plot(instance_size, exact,'g-')
ax[1].set_title('Exact algorithm')
for i in range(7):
  ax[2].plot(instance_size, approximation_all[i], label="$epsilon$=%s" %epsilon_all[i],)
ax[2].set_title('Approximation algorithm')
ax[2].legend(loc="upper_left", prop={"size":9})

plt.show()



for i in files:
    i.close()
    


# plt.figure(1)
# for file in files:
#     x=[]
#     y_ex=[]
#     y_app=[]
#     epsilon=[]
#     data=csv.reader(file)
#     runtimeData(data,x,y_ex,y_app,epsilon)
#     #print (y_app)
#     plt.plot(x,y_app,'-o',label='$ epsilon =%s$' %epsilon[0])
# plt.plot(x,y_ex,'-D',markersize=6,color=(1,0,0),label='$exact$',linewidth=4.0)
# plt.legend(loc="upper left", bbox_to_anchor=[0, 1],
#            ncol=2, shadow=True, title="Legend", fancybox=True)

# plt.show()

