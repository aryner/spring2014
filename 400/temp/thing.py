""""
Homework Set 01
set01.py

Dr. David Sklar, Math 400


----------Gauss-Elimination-----------

# ge_1(aug, scheme), where scheme is the pivoting strategy,
# 1, 2, or, 3 corresponding to the findPivotrow functions
# findPivotrow1 : basic gauss elim
# findPivotrow2 : partial pivoting
# findPivotrow# : partially scaled partial pivoting
# returns a solution vector.

----------Gauss-Jordan Inversion----------

# gj(M, scheme), takes a square matrix
# and a pivoting scheme, 1, 2, 3, and returns an 
# approximate inverse of M

----------Gauss-Seidel Iteration-----------

# gs(A, b, n):
# takes an m x m  matrix and a m x 1 vector b, computes 
# approx vector solution x, with an error < n 

----------Hilbert Functions-----------  


# bV(m): Creates the b vector, implements the scipy 
# math library to compute the integrals, a time saving device.    

#  hilb(n, m) returns an nxm Hilbert matrix, for this application
#  we only consider n x n matrices, so input n, m should be equal
#  pardon the redundancy.


# invhilb(n): Calculates the inverse of the Hilbert matrix based on formula,
# returns exact values of the inv of an nxn hilbert matrix

-----------Code Organization---------------
1. Matrix and Vector Functions
2. Error/Solution checking functions
3. Pivoting
4. Solution/Inverse Methods
5. Hilbert Matrix Functions
6. Testing Suite

"""
P2A=[[10,10,10,10**17, 10**17],[1,.001,.001,.001,1],[1,1,.001,.001,2],[1,1,1,.001,3]]
P2b=[10**17,1,2,3]

test2=[[1,2,3],[0,1,0],[3,2,1]]
test2b=[10,4,14]

from scipy.integrate import quad
from pylab import *
from math import factorial
import random

######## Matrix and vector functions

def rows(mat):
    "return number of rows"
    return(len(mat))

def cols(mat):
    "return number of cols"
    return(len(mat[0]))

def zero(m,n):
    "Create zero matrix"
    new_mat = [[0 for col in range(n)] for row in range(m)]
    return new_mat

def identity(m,n):
    c=zero(m,n)
    for i in range(rows(c)):
        c[i][i] = 1
    return(c) 

def transpose(mat):
    "return transpose of mat"
    new_mat = zero(cols(mat),rows(mat))
    for row in range(rows(mat)):
        for col in range(cols(mat)):
            new_mat[col][row] = mat[row][col]
    return(new_mat)

def dot(A,B):
    "vector dot product"
    if len(A) != len(B):
        print("dot: list lengths do not match")
        return()
    dot=0
    for i in range(len(A)):
        dot = dot + A[i]*B[i]
    return(dot)

def getCol(mat, col):
    "return column col from matrix mat"
    return([r[col] for r in mat])

def getRow(mat, row):
    "return row row from matrix mat"
    return(mat[row])

def matMult(mat1,mat2):
    "multiply two matrices"
    if cols(mat1) != rows(mat2):
        print("multiply: mismatched matrices")
        return()
    prod = zero(rows(mat1),cols(mat2))
    for row in range(rows(mat1)):
        for col in range(cols(mat2)):
            prod[row][col] = dot(mat1[row],getCol(mat2,col))
    return(prod)

def copyMatrix(M):
    return([[M[row][col] for col in range(cols(M))]for row in range(rows(M))])

def vectorQ(V):
    "mild test to see if V is a vector"
    if type(V) != type([1]):
        return(False)
    if type(V[0]) == type([1]):
        return(False)
    return(True)

def scalarMult(a,mat):
    "multiplies a scalar times a matrix"
    "If mat is a vector it returns a vector."
    if vectorQ(mat):
        return([a*m for m in mat])
    for row in range(rows(mat)):
        for col in range(cols(mat)):
            mat[row][col] = a*mat[row][col]
    return(mat)

def addVectors(A,B):
    "add two vectors"
    if len(A) != len(B):
        print("addVectors: different lengths")
        return()
    return([A[i]+B[i] for i in range(len(A))])

def swaprows(M,i,j):
    "swap rows i and j in matrix M"
    N=copyMatrix(M)
    T = N[i]
    N[i] = N[j]
    N[j] = T
    return N

def addrows(M, f, t, scale=1):
    "add scale times row f to row t"
    N=copyMatrix(M)
    T=addVectors(scalarMult(scale,N[f]),N[t])
    N[t]=T
    return(N)

def show(mat):
    "Print out matrix"
    for row in mat:
        print(row)

def maxColi(col):
    max = 0
    index = 0
    for i in range(len(col)):
        if abs(col[i]) > abs(max): 
            max = col[i]
            index = i
    return(index)

def max(row):
    index = 0
    for col in range(len(row)-1):
        if abs(row[col]) > abs(row[index]):
            index = col
    return(row[index])

def rescale(mat):
    N = copyMatrix(mat)
    for row in range(len(N)):
        norm = max(N[row])
        for col in range(cols(N)):
           N[row][col] *= 1.0/norm
    return(N)

def diag_test(mat):
    """
    Returns True if no diagonal element is zero, False
    otherwise.
    
    """
    for row in range(rows(mat)):
        if mat[row][row] == 0:
            return(False)
    else:
        return(True)

def vec2rowVec(vec):
    "[a,b,c] -> [[a,b,c]]"
    return([vec])

def vec2colVec(vec):
    return(transpose(vec2rowVec(vec)))

def colVec2vec(mat):
    rowVec = transpose(mat)
    return(rowVec[0])

def copyVec(L):
    "return a copy of L"
    C=[k for k in L]
    return(C)

def augment(mat,vec):
    "given nxn mat and n length vector return augmented matrix"
    amat = []
    for row in range(rows(mat)):
        amat.append(mat[row]+[vec[row]])
    return(amat)

######### The next set functions support checking a solution.

# Outputs the absolute value of the difference of the matrices
def errorTest(M, N):
    E = zero(len(M),len(M))
    for row in range(len(M)):
        for col in range(len(M)):
            E[row][col] = abs(M[row][col] - N[row][col])
    n = infNorm(E)
    return(n)

# Calculates the infinity norm.
def infNorm(M):
    s = []
    max = 0
    for row in range(len(M)):
        x = 0
        for col in range(len(M)):
            x += abs(M[row][col])
        s.append(x)
    for i in range(len(s)):
        if s[i] > max: max = s[i]
    return(max)

# Vector norm
def normV(V):
    s = []
    max = 0
    x = 0
    for col in range(len(V)):
        x += abs(V[col])
        s.append(x)
    for i in range(len(s)):
        if s[i] > max: max = s[i]
    return(max)


def getAandb(aug):
    "Returns the coef. matrix A and the vector b of Ax=b"
    m = rows(aug)
    n = cols(aug)
    A = zero(m,n-1)
    b = zero(m,1)
    for i in range(m):
        for j in range(n-1):
            A[i][j] = aug[i][j]
            
    for i in range(m):
        b[i] = aug[i][n-1]
    Aandb = [A,b]
    return(Aandb)

def checkSol(A, x):
    b = zero(len(x),1)
    for row in range(len(A)):
        for col in range(len(x)):
            b[row] += A[row][col]*x[col]
    return(b)

def checkSol_1(aug,x):
    "For aug=[A|b], returns Ax, b, and b-Ax as vectors"
    "Note: It assumes x is a vector not a matrix"
    A  = getAandb(aug)[0]
    b  = getAandb(aug)[1]
    x_col_vec = vec2colVec(x)
    Ax = matMult(A,x_col_vec)
    r  = addVectors(b,scalarMult(-1.0,colVec2vec(Ax)))
    L  = [Ax,b,r]
    return(L)

# residual error vector r = b - Ax, x is an approx. sol.

def resError(Ax, b):
    r = addVectors(Ax, scalarMult(-1, b))
    r_max = 0
    for i in range(len(r)):
        r[i] = abs(r[i])
        if r[i] > r_max: r_max = r[i]
    return(r_max)

############ pivoting


### The naive gaussian elimination code begins here.

def findPivotrow1(mat,col):
    "Finds index of the first row with nonzero entry on or"
    "below diagonal.  If there isn't one return(-1)."
    for row in range(col, rows(mat)):
        if mat[row][col] != 0:
            return(row)
    return(-1)

# Implements a partial pivoting scheme

def findPivotrow2(mat,col):
    "Finds index of the row on or below the diagonal."
    "with the max of the column."
    pivot = col
    for row in range(col, rows(mat)):
        if abs(mat[row][col]) > abs(mat[pivot][col]):
            pivot = row
    if pivot < rows(mat):
        return(pivot)
    return(-1)

# (partially) Scaled partial pivoting
def findPivotrow3(mat,col):
    N = copyMatrix(mat)
    N = rescale(N)
    pivot = findPivotrow2(N,col)
    return(pivot)

# rowReduce(M, scheme), where M is a matrix and scheme is either
# 1, 2, or 3, referring to the three pivot schemes just above.
def rowReduce(M, scheme):
    "return row reduced version of M"
    N = copyMatrix(M)
    cs = cols(M)-2   # no need to consider last two cols
    rs = rows(M)
    for col in range(cs+1):
        if scheme == 1:
            j = findPivotrow1(N,col)
            if col == 0: print "Executing naive pivot scheme." 
        elif scheme == 2: 
            j = findPivotrow2(N,col) 
            if col == 0: print "Executing partial pivot scheme."
        elif scheme == 3: 
            j = findPivotrow3(N,col) 
            if col == 0: print "Executing p.s.p. pivot scheme."
        else: return("Error: Choose 1, 2, 3 pivot scheme")

        if j < 0:
            print("\nrowReduce: No pivot found for column index %d "%(col))
            return(N)
        else:
            if j != col:
                N = swaprows(N,col,j)
            scale = -1.0 / N[col][col]
            for row in range(col+1,rs):                
                N=addrows(N, col, row, scale * N[row][col])
    return(N)


def backSub(M):
    """
    given a row reduced augmented matrix with nonzero 
    diagonal entries, returns a solution vector
    
    """
    cs = cols(M)-1 # cols not counting augmented col
    sol = [0 for i in range(cs)] # place for solution vector
    for i in range(1,cs+1):
        row = cs-i # work backwards
        sol[row] = ((M[row][cs] - sum([M[row][j]*sol[j] for
                    j in range(row+1,cs)])) / float(M[row][row])) 
    return(sol)

# ge_1(aug, scheme), where scheme is the pivoting strategy,
# 1, 2, or, 3 corresponding to the above findPivotRow functions.
def ge_1(aug, scheme):    
    """
    returns a solution vector.  The solution
    vector is empty if there is no unique solution.
    """
    aug_n = rowReduce(aug, scheme)
    if diag_test(aug_n):
        sol = backSub(aug_n)
    else:
        print("\nge_1(): There is no unique solution")
        sol = []
    #results = [aug_n, sol]
    return(sol)

# gj(M, scheme) takes a square matrix
# and a pivoting scheme, 1, 2, 3, and returns an 
# approximate inverse of M

def gj(M, scheme):
    "return row reduced version of M"
    N = copyMatrix(M)
    Ai = identity(rows(N),rows(N)) 
    cs = cols(M)  
    rs = rows(M)
    
    for row in range(rows(Ai)):
       N = augment(N, Ai[row])
    # This block only prints the selected pivot scheme
    for col in range(cs):
        if scheme == 1:
            j = findPivotrow1(N,col)
            if col == 0: print "Executing naive pivot scheme." 
        elif scheme == 2: 
            j = findPivotrow2(N,col) 
            if col == 0: print "Executing partial pivot scheme."
        elif scheme == 3: 
            j = findPivotrow3(N,col) 
            if col == 0: print "Executing p.s.p. pivot scheme."
        else: return("Error: Choose 1, 2, 3 pivot scheme")
        # Implement the scheme
        if j < 0:
            print("\nrowReduce: No pivot found for column index %d "%(col))
            return(N)
        else:
            if j != col:
                N = swaprows(N,col,j)
            scale = -1.0 / N[col][col]
            # row reduce
            for row in range(0,rs):
               if row != col:
                  N=addrows(N, col, row, scale * N[row][col])
                  
    # reduce to row-echelon form              
    for row in range(rows(N)):
        d = N[row][row]
        for col in range(cols(N)):
            N[row][col] = N[row][col] / d
    # spit out inverse        
    for row in range(rows(Ai)):
       for col in range(cols(Ai)):
          Ai[row][col] = N[row][cs + col]
          
    return(Ai)

# gauss-seidel elimination
# takes an m x m  matrix and a m x 1 vector b, computes 
# approx vector solution x, error < n

def lu_decomposition(A):
    """Performs an LU Decomposition of A (which must be square)                                                                                                                                                                                        
    into PA = LU. The function returns P, L and U."""
    n = len(A)

    # Create zero matrices for L and U                                                                                                                                                                                                                 
    L = zero(len(A),len(A))
    U = zero(len(A),len(A))

    # Create the pivot matrix P and the multipled matrix PA                                                                                                                                                                                            
    P = identity(len(A),len(A))
    PA = matMult(P, A)

    # Perform the LU Decomposition                                                                                                                                                                                                                     
    for j in xrange(n):
        # All diagonal entries of L are set to unity                                                                                                                                                                                                   
        L[j][j] = 1.0

        # LaTeX: u_{ij} = a_{ij} - \sum_{k=1}^{i-1} u_{kj} l_{ik}                                                                                                                                                                                      
        for i in xrange(j+1):
            s1 = sum(U[k][j] * L[i][k] for k in xrange(i))
            U[i][j] = PA[i][j] - s1

        # LaTeX: l_{ij} = \frac{1}{u_{jj}} (a_{ij} - \sum_{k=1}^{j-1} u_{kj} l_{ik} )                                                                                                                                                                  
        for i in xrange(j, n):
            s2 = sum(U[k][j] * L[i][k] for k in xrange(j))
            L[i][j] = (PA[i][j] - s2) / U[j][j]

    print "\nL matrix:\n"
    show(L)
    print "\nU matrix:\n"
    show(U)
    print "\n LU Solution"
    return (backSub(U))

def zeroV(m):
    z = []
    for i in range(m): z.append(0)
    return z


def maxColi(col):
    max = 0
    index = 0
    for i in range(len(col)):
        if abs(col[i]) > max: 
            max = col[i]
            index = i
    return(index)


def gs(A, b, n):
    x = zeroV(len(b))
    x_old = zeroV(len(b))
    A_ = zero(len(b), len(b))
    eps = zeroV(len(b))
    error = 0
    k = 0
    # rewrite matrix
    for i in range(len(A)):
        for j in range(len(b)):
            if j == i:
                A_[i][j] += b[i]
            else:
                # subtract non-diagonal elements
                A_[i][j] = -1*A[i][j]
    while True:
        # compute x
        for i in range(len(A_)):
            q = 0
            # d is the coefficient on the diag of the input mat A
            d = float(A[i][i])
            for j in range(len(A_)):
                if i == j:
                    p = A_[i][j]/d
                else: 
                    q += (A_[i][j]*x[j])/d
            x[i] = p + q

        if k > 0:
            for i in range(len(x)):
                if x[i] != 0:
                    eps.append(abs(x[i] - x_old[i]))
            z = maxColi(eps)
            error = eps[z]
            print "error: ", error, " n : ", n, " k : ", k
            if error < n:
                return(x)
               
            x_old = copyVec(x)
            eps = []
        k += 1


def doMatrixSolve(A,B,tol=10**(-7)): #A must be square n by n matrix, B is 1 by n vector
  x_new=[[1. for i in range(len(B[0]))]]
  converged=False
  while not converged:
    x_old=copyMatrix(x_new)
    x_new=gaussSiedel(A,B,x_old)
    
    converged=True #succeeds by default
    for i in xrange(len(x_old[0])):
      if abs(2*(x_new[0][i]-x_old[0][i])/(x_new[0][i]+x_old[0][i]))>=tol:
        converged=False
        break
  return show(x_new)

def gaussSiedel(A,B,x):
  n=len(x[0])
  y=[[0. for l in range(n)]]
  for i in range(len(x[0])):
    val=0 #dummy variable
    for j in range(i):
      val+=A[i][j]*y[0][j]
      #print 'a',i,j
    for j in range(i+1,n):
      val+=A[i][j]*x[0][j]
      #print 'b',i,j
    y[0][i]=(B[0][i]-val)/A[i][i]
  #print y
  return y

####### Hilbert

# uses scipy for computing the integrals
def intgr(m):
    def intgrnd1(x):
        return (x**(m))*sin(pi*x)
    result, err = quad(intgrnd1, 0.0, 1.0)
    return(result)

# Creates the b vector    
def bV(m):
    b = zero(m,1)
    for i in range(m):
        b[i] = intgr(i)
    return(b)

# Compute the binomial coefficient
def binomial(n, k):
    if k < 0 or k > n:
        return 0
    if k == 0 or k == n:
        return 1
    return factorial(n) // (factorial(k) * factorial(n-k))

#  hilb(n, m) returns an nxm Hilbert matrix, for this application
#  we only consider n x n, pardon the redundancy.

def hilb(n, m):
    H = zero(m,n)
    Hn = zero(m,n)
    for i in range(n):
        for j in range(m):
            H[i][j] += (1.0/(i + j + 1))
    return(H)

# Calculates the inverse of the Hilbert matrix based on formula,
# returns exact values of the inv of an nxn hilbert matrix

def invhilb(n):
    H = zero(n, n)
    for i in range(n):
        for j in range(n):
            H[i][j] = ((-1)**(i + j)) * (i + j + 1) * binomial(n + i, n - j - 1) * \
            binomial(n + j, n - i - 1) * binomial(i + j, i) ** 2
    return H

print "usage: ge_1(aug, scheme), gj(M, scheme), gs(A, b, n), hilb(n, m), bV(m), invhilb(n), resError(Ax, b)"

C=hilb(5,5)
q=bV(5)
for i in range (len(C)):
    C[i].append(q[i])
show(C)
print "\n\nProblem 2:\n\n"
show(backSub(rowReduce(P2A,1)))
show(backSub(rowReduce(P2A,2)))
show(backSub(rowReduce(P2A,3)))
print "\n\nProblem 3:\n\n"
show(backSub(rowReduce(C,1)))
show(backSub(rowReduce(C,3)))
show(gs(hilb(5,5),bV(5),.01))
show(gs(P2A,P2b,.01))
show(lu_decomposition(hilb(5,5)))
doMatrixSolve(P2A,P2b,.001)
#show(gs(P2A,P2b,.01))
