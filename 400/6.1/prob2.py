from gauss import *

matrix = [
          [1,1,1,1e-3,3],
          [1,1,1e-3,1e-3,2],
          [1,1e-3,1e-3,1e-3,1],
          [10,10,10,1e17,1e17]
          ]

print '***The matrix in question:***'
show(matrix)
a = rowReduce(matrix)
a = backSub(a)
print '\na) using basic Gaussing elemination we get:'
show(a)

b = rowReducePartialPivots(matrix)
b = backSub(b)
print '\nb) using Gaussing elemination with partial pivoting we get:'
show(b)
