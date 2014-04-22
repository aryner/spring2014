#include <stdio.h>
#include <stdlib.h>

int main(int arc, char **argv)
{
  int address = atoi(argv[1]);
  int pageNum = address / (1024*4);
  int offset = address - pageNum*(1024*4);

  printf("page number: %d\n", pageNum);
  printf("offset: %d\n", offset);
}
