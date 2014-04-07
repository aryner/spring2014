#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <pthread.h>
#include <semaphore.h>

static void *producerFunc(void *info);
static void *consumerFunc(void *info);

sem_t full;
sem_t empty;
pthread_mutex_t lock;
int pIndex = 0;
int cIndex = 0;

struct producerInfo {
  int threadNumber;
  int counter;
  int toProduce;
  int *buff;
  int bufferSize;
};
struct consumerInfo {
  int threadNumber;
  int toConsume;
  int *buff;
  int bufferSize;
};

int main(int arc, char **argv)
{
  int bufferSize = atoi(argv[1]);
  int numProducers = atoi(argv[2]);
  int numConsumers = atoi(argv[3]);
  int amountProduced = atoi(argv[4]);

  printf("Buffer size = %d, ", bufferSize);
  printf("number of producer threads = %d, ", numProducers);
  printf("number of consumer threads = %d, ", numConsumers);
  printf("and each producer produces %d\n", amountProduced);

  int buffer[bufferSize];

  if(sem_init(&full, 0, 0) == -1) {
    printf("workz!! %d\n",errno);
  }
  if(sem_init(&empty, 0, bufferSize) == -1) {
    printf("workz!! %d\n",errno );
  }

  pthread_attr_t attr;
  pthread_attr_init(&attr);
  pthread_t producers[numProducers];
  pthread_t consumers[numConsumers];
  struct producerInfo pInfo[numProducers];
  struct consumerInfo cInfo[numConsumers];
 
  int i;
  for(i=0; i<numProducers; i++) {
    pInfo[i].threadNumber = i+1;
    pInfo[i].counter = 0;
    pInfo[i].toProduce = amountProduced;
    pInfo[i].buff = &buffer;
    pInfo[i].bufferSize = bufferSize;
  }
  for(i=0; i<numConsumers; i++) {
    cInfo[i].threadNumber = i+1;
    cInfo[i].toConsume = (numProducers * amountProduced) / numConsumers;
    cInfo[i].buff = &buffer;
    cInfo[i].bufferSize = bufferSize;
  }
  int remainder = (numProducers*amountProduced)%numConsumers;
  for(i=0; i<remainder; i++) {
    cInfo[i].toConsume++;
  }

  for(i=0; i<numProducers; i++) {
    pthread_create(&producers[i], &attr, producerFunc, &pInfo[i]);
  }
  for(i=0; i<numConsumers; i++) {
    pthread_create(&consumers[i], &attr, consumerFunc, &cInfo[i]);
  }

  pthread_attr_destroy(&attr);
 
  for(i=0; i<numProducers; i++) {
    pthread_join(producers[i], NULL);
  }
  for(i=0; i<numConsumers; i++) {
    pthread_join(consumers[i], NULL);
  }

  pthread_mutex_destroy(&lock);
  sem_destroy(&full);
  sem_destroy(&empty);
  printf("****All jobs finished***\n");
}

static void *consumerFunc(void *info) {
  struct consumerInfo *cInfo = info;
int value;
  while(cInfo->toConsume > 0) {
    sem_wait(&full);
    pthread_mutex_lock(&lock);
      printf("Consumer %d consumed: %d\n", cInfo->threadNumber, cInfo->buff[cIndex]);
      cInfo->toConsume--;
      cIndex = (cIndex + 1) % cInfo->bufferSize;
      fflush(stdout);
    pthread_mutex_unlock(&lock);
    sem_post(&empty);
  }
  pthread_exit(NULL);
}

static void *producerFunc(void *info) {
  struct producerInfo *pInfo = info;
int value;
  while(pInfo->counter < pInfo->toProduce) {
    sem_wait(&empty);
    pthread_mutex_lock(&lock);
      pInfo->buff[pIndex] = pInfo->threadNumber * 1000000 + pInfo->counter;
      printf("Producer %d produced: %d\n", pInfo->threadNumber, pInfo->buff[pIndex]);
      pInfo->counter++;
      pIndex = (pIndex + 1) % pInfo->bufferSize;
      fflush(stdout);
    pthread_mutex_unlock(&lock);
    sem_post(&full);
  }
  pthread_exit(NULL);
}
