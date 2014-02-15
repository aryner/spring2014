#include <unistd.h>
#include <stdio.h>
#include <string.h>
#define NAME "alex ryner"

int main() {
  char greeting[50];
  sprintf(greeting, "Hello 415, it's me %s\n",NAME);
  write(1, greeting, strlen(greeting));
  return 0;
}
