#include <stdio.h>
#include <fcntl.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int command(char* input, pid_t pid, int status, char *arg, char **argv, int count);
int rightArrow(char* right, char* input, pid_t pid, int status, char *arg, char **argv, int count);

int main() {
  pid_t pid;
  char input[4096]; 
  int status;
  char *arg;
  char *argv[64];
  int count;
  char *pos;

  while(1) {
    printf("myShell>> ");
    fgets(input, 4096, stdin);
    if((pos = strchr(input, '\n')) != NULL) {
      *pos = '\0';
    }

    if(!strncmp(input,"exit",4)){
      break;
    }
    
    if(command(input,pid,status,arg,argv,count) < 0) {
      break;
    }
  }

  return 0;
}

int rightArrow(char* right, char* input, pid_t pid, int status, char *arg, char **argv, int count){
  char* after = right + 1;
  int file;
  if(*after == '>') {
    file = open((right+3), O_CREAT|O_WRONLY|O_APPEND);
   }
   else{
    file = open((right+2), O_CREAT|O_WRONLY|O_TRUNC);
   }
  
  if((pid = fork()) < 0) {
    fprintf(stderr, "error forking child process\n");
    close(file);
    return -1;
  }
  if(pid == 0) {
    arg = strtok(input, " ");
    count = 0;
    while(arg < right) {
      argv[count] = arg;
      arg = strtok(NULL, " ");
      count++;
    }
    argv[count] = NULL;
    char* before = right - 1;
    if(*before == '2') {
      close(2);
    }
    else {
      close(1);
    }
    dup(file);

    if(execvp(argv[0], argv) < 0) {
      fprintf(stderr, "error: Invalid command\n");
      close(file);
      return -1;
    }
  }
  else {
    while(wait(&status) != pid);
  } 

  close(file);
  return 0;
}

int command(char* input, pid_t pid, int status, char *arg, char **argv, int count){
  char* right;

  if((right = strstr(input, ">"))) {
    return rightArrow(right,input,pid,status,arg,argv,count);
  }

  if ((pid = fork()) < 0) {
    fprintf(stderr, "error forking child process\n");
    return -1;
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
      return -1;
    }
  }
  else {
    while(wait(&status) != pid);
  }

  return 0;
}

