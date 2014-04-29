#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
  pid_t pid;
  char input[4096]; 
  int status;
  char *arg;
  char *argv[64];
  int count;
  char *pos;

  while(1) {
    printf("myShell> ");
    fgets(input, 4096, stdin);
    if((pos = strchr(input, '\n')) != NULL) {
      *pos = '\0';
    }

    if(!strncmp(input,"exit",4)){
      break;
    }
 
    if ((pid = fork()) < 0) {
      fprintf(stderr, "error forking child process\n");
      break;
    }

    if (pid == 0) {
      arg = strtok(input, " ");
      count = 0;
      while(arg != NULL) {
        argv[count] = arg;
        arg = strtok(NULL, " ");
        count++;
      }
      argv[count] = NULL;

      if(execvp(argv[0], argv) < 0) {
        fprintf(stderr, "error: Invalid command\n");
        break;
      }
    }
    else {
      while(wait(&status) != pid);
    }
  }

  return 0;
}
