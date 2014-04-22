#include <stdio.h>
#include <windows.h>
#include <strsafe.h>
#define threadCount 8

DWORD WINAPI wordCount(LPVOID info);

char data[102400*64];
HANDLE mutex;

typedef struct threadInfo {
  int start;
  int end;
//  int count;
}INFO, *PINFO;

int sum = 0;

int main(int argc, char **argv) {
  mutex = CreateMutex(NULL, FALSE, NULL);
  if(mutex == NULL) {  
    printf("mutex failed\n");
  }

  PINFO info[threadCount];
  HANDLE threadHandle[threadCount];
  DWORD waits;

  for(int i=0; i<threadCount; i++) {
    info[i] = (PINFO)HeapAlloc(GetProcessHeap(),
		    HEAP_ZERO_MEMORY,
		    sizeof(INFO));
  }


  FILE *file = fopen(argv[1], "r");
  size_t fileSize = fread(data, sizeof (char), 102400*64, file);
  info[0]->start = 0;
  info[threadCount-1]->end = (int)fileSize;

  for(int i=0; i<(int)threadCount-1; i++) {
    if (i != 0)
      info[i]->start = info[i-1]->end;
    int j = (fileSize /threadCount) * (i+1);
    info[i]->end = j;
    while(isalpha(data[j])) {
      info[i]->end++;
      j++;
    }
  }
  if(threadCount >1)
    info[threadCount-1]->start = info[threadCount-2]->end;

  for(int i=0; i<(int)threadCount; i++) {
    threadHandle[i] = CreateThread(NULL,
		                  0,
				  wordCount,
				  info[i],
                                  0,
				  NULL);
    if(threadHandle[i] == NULL) 
      printf("thread creation error");
  }

  WaitForMultipleObjects(threadCount,threadHandle,TRUE,INFINITE);

  for(int i=0; i<(int)threadCount; i++) {
//    waits = WaitForSingleObject(threadHandle[i],
//		                INFINITE);
//    sum += info[i]->count;
    CloseHandle(threadHandle[i]);

  }
  printf("%d words\n", sum);

  return 0;
}

DWORD WINAPI wordCount(LPVOID info) {
  struct threadInfo *thing = info;
//  thing->count = 0;
  int start = thing->start;
  int end = thing->end;

  WaitForSingleObject(mutex, INFINITE);

  while(start < end) {
    while(!isalpha(data[start])) {
      start++;
    }
    if(start < end) {
 //     thing->count++;
      sum++;
    }
    while(isalpha(data[start])) {
      start++;
    }
  }
  ReleaseMutex(mutex);
  return 0;
}
