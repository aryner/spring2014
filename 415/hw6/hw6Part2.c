#include <stdio.h>
#include <time.h>
#include <stdlib.h>

void zeroTable(int *table, int size) {
  for(int i=0; i<size; i++) {
    table[i] = -1;
  }
}

int fifo(int *table, int numFrames, int *refString, int rSize) {
  int numFaults = 0;
  int fault;
  int oldest = 0;

  for(int i=0; i<rSize; i++) {
    fault = 1;
    for(int j=0; j<numFrames && fault==1; j++) {
      if(table[j] == refString[i]) {
        fault = 0;
      }
    }
    if(fault == 1) {
      numFaults++;
      table[oldest] = refString[i];
      oldest = (oldest+1) % numFrames;
    }
  }
  return numFaults;
}

int LRU(int *table, int numFrames, int *refString, int rSize) {
  int numFaults = 0;
  int fault;
  int lru[numFrames];
  int temp;
  zeroTable(lru, numFrames);

  for(int i=0; i<rSize; i++) {
    fault = -1;
    for(int j=0; (j<numFrames-1) && (fault == -1); j++) {
      if(table[j] == refString[i]){
        fault = j;
      }
    }
    if(fault == -1) {
      numFaults++;
      if(numFaults < numFrames+1) {
        table[numFaults-1] = refString[i];
        lru[numFaults-1] = numFaults-1;
      }
      else {
        table[lru[0]] = refString[i];
        fault = lru[0];
        for(int j=0; j<numFrames-1; j++) {
          lru[j] = lru[j+1];
        }
        lru[numFrames-1] = fault;
      }
    }
    else {
      for(int j=0; j<numFrames; j++) {
        if(lru[j] == fault) {
          fault = j;
          break;
        }
      }
      for(int j=fault; j<numFrames-1 && j<numFaults-1; j++) {
        temp = lru[j];
        lru[j] = lru[j+1];
        lru[j+1] = temp;
      }
    }
  }
  return numFaults;
}

int main() 
{
  int refString[100];
  srand(time(NULL));

  refString[1] = rand()%15;
  for(int i=1; i<100; i++) {
    refString[i] = rand()%15;
    while(refString[i] == refString[i-1]) {
      refString[i] = rand()%15;
    }
  }

  int t1[16];
  zeroTable(t1,16);

  printf("FIFO:\n");
  int one;

  for(int i=1; i<17; i++) {
    zeroTable(t1,16);
    one = fifo(t1, i, refString, 100);
    printf("%d\n", one);
  }

  printf("\nLRU:\n");
  zeroTable(t1,16);
  one = LRU(t1, 1, refString, 100);

  for(int i=1; i<17; i++) {
    zeroTable(t1,16);
    one = LRU(t1, i, refString, 100);
    printf("%d\n", one);
  }

}

