from gauss import *

a = [[4,-1,1,8],[2,5,2,3],[1,2,4,11]]
b = [[4,1,2,9],[2,4,-2,-5],[1,1,-3,-9]]

show(a)
a = rowReduce(a)
print
show(a)
a = backSub(a)
print
show(a)
print('*******')
show(b)
b = rowReduce(b)
print
show(b)
b = backSub(b)
print
show(b)
#gauss.
