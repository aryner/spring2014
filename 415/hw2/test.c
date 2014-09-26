#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

int main(int argc, char **argv) 
{
  int old = open(argv[1], O_RDONLY);

  if(old >= 0) {
    int new = open(argv[2], O_CREAT|O_WRONLY);
    if(new >= 0) {
      char data[512];
      size_t copied;

      copied = read(old, data, 512);
      int total = copied;

      while (copied > 0) {
        write(new, data, copied);
        copied = read(old, data, 512);
        total += copied;
      }

      printf("%d bytes copied\n", total);
      close(new);
    }
    else {
      fprintf(stderr, "error: %s already exists\n", argv[2]);
      close(old);
      return 1;
    }

    close(old);
  }

  else {
    fprintf(stderr, "error: %s does not exist\n", argv[1]);    
    return 1;
  }

  return 0;
}
