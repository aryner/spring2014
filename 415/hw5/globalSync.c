#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <pthread.h>
#define threadCount 8

static void *wordCount(void *info);

char data[102400*64];

struct threadInfo {
  long start;
  long end;
//  long count;
};

long sum = 0;
pthread_mutex_t lock;

int main(int argc, char **argv)
{
  if (pthread_mutex_init(&lock, NULL) != 0)
  {
    printf("init failed\n");
    return 1;
  }

  pthread_attr_t attr;
  pthread_t threads[(int)threadCount];
  struct threadInfo info[threadCount];

  int err = pthread_attr_init(&attr);
  if(err != 0){
    printf("pthread_attr_init error number %d", err);
    return 1;
  }

  FILE *file = fopen(argv[1], "r");
  long fileSize = fread(data, sizeof (char), 102400*64, file);
  info[0].start = 0;
  info[threadCount-1].end = fileSize;

  for(int i=0; i<(int)threadCount-1; i++) {
    if (i != 0) 
      info[i].start = info[i-1].end;
    long j = (fileSize / threadCount) * (i+1);      
    info[i].end = j;
    while(isalpha(data[j])) {
      info[i].end++;
      j++;
    } 
  }
  if(threadCount > 1)
    info[threadCount-1].start = info[threadCount-2].end;

  for(int i=0; i<(int)threadCount; i++) {
    err = pthread_create(&threads[i], &attr, wordCount, &info[i]);
    if(err != 0) {
      printf("pthread_create error number %d", err);
    }
  }

  err = pthread_attr_destroy(&attr);
  if (err != 0) {
    printf("pthread_attr_destroy error %d", err);
  }

  for(int i=0; i<(int)threadCount; i++) {
    err = pthread_join(threads[i], NULL);
    if (err != 0) {
      printf("pthread_join error %d", err);
    }
//    sum += info[i].count;
  }
  printf("%ld words\n", sum);
  pthread_mutex_destroy(&lock);

  return 0;
}

static void *wordCount(void *info){
  struct threadInfo *thing = info;
//  thing->count = 0;
  int start = thing->start;
  int end = thing->end;
  pthread_mutex_lock(&lock);
  while(start < end) {
    while(!isalpha(data[start])) {
      start++;
    }
    if(start < end) {
      sum++;
    }
    while(isalpha(data[start])) {
      start++;
    }
  }
  pthread_mutex_unlock(&lock);
 // sum += thing->count;
  pthread_exit(NULL);
}


