from sklearn.datasets import make_blobs
import matplotlib.pyplot as plt
import matplotlib.cm as cm
from sklearn.cluster import KMeans

from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import pandas as pd
import sys
import random

file1 = r'./test_csv_features0.csv'
file2 = r'./test_csv_features1.csv'
file3 = r'./test_csv_features2.csv'
file4 = r'./test_csv_features_heater_s0p0.csv'
file5 = r'./test_csv_features_heater_s0p1.csv'
file6 = r'./test_csv_features_heater_s1p0.csv'
file7 = r'./test_csv_features_heater_s1p1.csv'


dataframe = pd.read_csv(file1, header=None)
h0_error1 = dataframe[0].values.tolist()
h0_error2 = dataframe[1].values.tolist()
h0_rms_cos = dataframe[2].values.tolist()
h0_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file2, header=None)
h1_error1 = dataframe[0].values.tolist()
h1_error2 = dataframe[1].values.tolist()
h1_rms_cos = dataframe[2].values.tolist()
h1_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file3, header=None)
h2_error1 = dataframe[0].values.tolist()
h2_error2 = dataframe[1].values.tolist()
h2_rms_cos = dataframe[2].values.tolist()
h2_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file4, header=None)
ht0_error1 = dataframe[0].values.tolist()
ht0_error2 = dataframe[1].values.tolist()
ht0_rms_cos = dataframe[2].values.tolist()
ht0_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file5, header=None)
ht1_error1 = dataframe[0].values.tolist()
ht1_error2 = dataframe[1].values.tolist()
ht1_rms_cos = dataframe[2].values.tolist()
ht1_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file6, header=None)
ht2_error1 = dataframe[0].values.tolist()
ht2_error2 = dataframe[1].values.tolist()
ht2_rms_cos = dataframe[2].values.tolist()
ht2_rms_plot = dataframe[3].values.tolist()

dataframe = pd.read_csv(file7, header=None)
ht3_error1 = dataframe[0].values.tolist()
ht3_error2 = dataframe[1].values.tolist()
ht3_rms_cos = dataframe[2].values.tolist()
ht3_rms_plot = dataframe[3].values.tolist()

LinesOfCsv = np.array([len(h0_error1), len(h1_error1), len(h2_error1), len(ht0_error1), len(ht1_error1), len(ht2_error1), len(ht3_error1)])
np.savetxt(r'./LinesOfCsv.txt', LinesOfCsv, fmt = '%s')

fig = plt.figure(figsize=(10, 8))
ax = fig.add_subplot(111, projection='3d') # Axe3D object

x = h0_rms_cos + h1_rms_cos + h2_rms_cos + ht0_rms_cos + ht1_rms_cos + ht2_rms_cos + ht3_rms_cos
y = h0_rms_plot + h1_rms_plot + h2_rms_plot + ht0_rms_plot + ht1_rms_plot + ht2_rms_plot + ht3_rms_plot
z = h0_error1 + h1_error1 + h2_error1 + ht0_error1 + ht1_error1 + ht2_error1 + ht3_error1
#ax.scatter(x, y, z, c = z, s= 20, alpha=1, cmap=plt.cm.Greens)
ax.plot(x, y, z,'bo')
ax.set_xlabel('rms_cos')
ax.set_ylabel('rms_plot')
ax.set_zlabel('error1')

plt.savefig('./KmC_analysis04.svg')
plt.title("ax.scatter")
plt.show()

X = []
for cnt in range(len(x)):
    temp = [x[cnt], y[cnt], z[cnt]]
    X.append(temp)
X = np.asarray(X)

nKmcCenter = (np.loadtxt(r'./KindOfProduct.txt', delimiter = '\n', dtype='str')).tolist()
nKmcCenter = len(nKmcCenter)

km = KMeans(n_clusters=nKmcCenter, 
            init='random', 
            n_init=10, 
            max_iter=300,
            tol=1e-04,
            random_state=0)

y_km = km.fit_predict(X)

'''
print(type(y_km))
print(y_km)
print(' ')
print(X)
'''
print(type(y_km))
print(y_km)
#np.savetxt("test_ykm.csv", y_km, delimiter=",")

fig = plt.figure(figsize=(10, 8))
ax = fig.add_subplot(111, projection='3d') # Axe3D object

ax.scatter(X[y_km == 0, 0], 
            X[y_km == 0, 1],
            X[y_km == 0, 2],
            s=50, c='lightgreen',
            marker='s', edgecolor='black',
            label='cluster 1')
ax.scatter(X[y_km == 1, 0],
            X[y_km == 1, 1],
            X[y_km == 1, 2],
            s=50, c='orange',
            marker='o', edgecolor='black',
            label='cluster 2')
ax.scatter(X[y_km == 2, 0],
            X[y_km == 2, 1],
            X[y_km == 2, 2],
            s=50, c='lightblue',
            marker='v', edgecolor='black',
            label='cluster 3')
ax.scatter(X[y_km == 3, 0],
            X[y_km == 3, 1],
            X[y_km == 3, 2],
            s=50, c='yellow',
            marker='o', edgecolor='black',
            label='cluster 4')
ax.scatter(X[y_km == 4, 0],
            X[y_km == 4, 1],
            X[y_km == 4, 2],
            s=50, c='blue',
            marker='x', edgecolor='black',
            label='cluster 5')
ax.scatter(X[y_km == 5, 0],
            X[y_km == 5, 1],
            X[y_km == 5, 2],
            s=50, c='magenta',
            marker='s', edgecolor='black',
            label='cluster 6')
ax.scatter(X[y_km == 6, 0],
            X[y_km == 6, 1],
            X[y_km == 6, 2],
            s=50, c='orange',
            marker='x', edgecolor='black',
            label='cluster 7')
ax.scatter(km.cluster_centers_[:, 0],
            km.cluster_centers_[:, 1],
            km.cluster_centers_[:, 2],
            s=250, marker='*',
            c='red', edgecolor='black',
            label='centroids')
plt.legend(scatterpoints=1)
plt.grid()
plt.tight_layout()
plt.show()

print(X)

axis = (np.loadtxt(r'./KindOfProduct.txt', delimiter = '\n', dtype='str')).tolist()
nUntitled = int(np.loadtxt(r'./NumOfUntitled.txt', delimiter = '\n', dtype='int'))
print(axis)
print(nUntitled)

CENTERLENGTH = 15

lKmcCenter = [0 for i in range(nKmcCenter)]

newData = [200, 100, 5]
#newData = [236, 401, 10]
for i in range(nKmcCenter):
    lKmcCenter[i] = ((newData[0] - km.cluster_centers_[i, 0])**2 + (newData[1] - km.cluster_centers_[i, 1])**2)**0.5
print(lKmcCenter)


if(min(lKmcCenter) > CENTERLENGTH):
    NEWCENTROID = True
else :
    NEWCENTROID = False
	
"""
num = 4
lenTemp = [0 for i in range(len(ht2_error1))]
for i in range(len(ht2_error1)):
    lenTemp[i] = ((ht2_rms_cos[i]-km.cluster_centers_[num, 0])**2 + (ht2_rms_plot[i] - km.cluster_centers_[num, 1])**2)**0.5
#print(lenTemp)
print(max(lenTemp))
print(min(lenTemp))
"""


if(NEWCENTROID):
    nKmcCenter += 1

    km = KMeans(n_clusters=nKmcCenter, 
                init='random', 
                n_init=10, 
                max_iter=300,
                tol=1e-04,
                random_state=0)
    nUntitled = int(np.loadtxt(r'./NumOfUntitled.txt', delimiter = '\n', dtype='int'))
    nUntitled += 1
    f = open(r'./NumOfUntitled.txt', 'w')
    f.write(str(nUntitled))
    f.close()
    axis.append('untitled'+str(nUntitled))
    axis = np.array(axis)
    np.savetxt(r'./KindOfProduct.txt', axis, fmt = '%s')
elif(not NEWCENTROID):
    print(lKmcCenter)
    pass

for i in range(300):
    newData_x = random.randint(198, 202)
    newData_y = random.randint(98, 102)
    newData_z = random.randint(4, 9)
    temp = np.array([[newData_x, newData_y, newData_z]])
    X = np.concatenate((X, temp))

y_km = km.fit_predict(X)

fig = plt.figure(figsize=(10, 8))
ax = fig.add_subplot(111, projection='3d') # Axe3D object
colors = cm.rainbow(np.linspace(0, 1, nKmcCenter+1))

ax.scatter(km.cluster_centers_[:, 0],
            km.cluster_centers_[:, 1],
            km.cluster_centers_[:, 2],
            s=250, marker='*',
            c=[colors[-1]], edgecolor='black',
            label='centroids')
plt.legend(scatterpoints=1)
plt.grid()
plt.tight_layout()
plt.show()
#------------------------
fig = plt.figure(figsize=(10, 8))
ax = fig.add_subplot(111, projection='3d') # Axe3D object
colors = cm.rainbow(np.linspace(0, 1, nKmcCenter+1))

for i in range(nKmcCenter):
    ax.scatter(X[y_km == i, 0], 
            X[y_km == i, 1],
            X[y_km == i, 2],
            s=50, c=[colors[i]],
            marker='s', edgecolor='black',
            label='cluster'+str(i))

plt.legend(scatterpoints=1)
plt.grid()
plt.tight_layout()
plt.show()



untitled0_error1 = [10 for i in range(300)]    


LinesOfCsv = (np.loadtxt(r'C:\Users\dskim\python\machine\kmeans\LinesOfCsv.txt', delimiter = '\n', dtype='int')).tolist()
LinesOfCsv.append(len(untitled0_error1))
LinesOfCsv = np.array(LinesOfCsv)

np.savetxt(r'C:\Users\dskim\python\machine\kmeans\LinesOfCsv.txt', LinesOfCsv, fmt = '%s')

axis = (np.loadtxt(r'C:\Users\dskim\python\machine\kmeans\KindOfProduct.txt', delimiter = '\n', dtype='str')).tolist()
LinesOfCsv = (np.loadtxt(r'C:\Users\dskim\python\machine\kmeans\LinesOfCsv.txt', delimiter = '\n', dtype='int')).tolist()

nY_km = 0
nY_km = LinesOfCsv.insert(0, 0)
nY_km = LinesOfCsv[:]
LinesOfCsv.pop(0)

numCluster = [0 for i in range(len(axis))]
for i in range(len(axis)):
    temp1 = sum(nY_km[0:i+1])
    temp2 = sum(nY_km[1:i+2])
    numCluster[i] = int(round(sum(y_km[temp1:temp2])/nY_km[i+1]))
print(nY_km)
print(numCluster)
print(' ')

print("sample data's cluster : "+str(y_km[-1]) )
index = numCluster.index(y_km[-1])
print("(sample data's cluster)'s index : "+str(index))
print('csv file : '+str(axis[index]))

X = np.delete(X, -1, 0)
print(X)
