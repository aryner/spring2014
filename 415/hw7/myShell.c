#include <stdio.h>
#include <fcntl.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int command(char* input, pid_t pid, int status, char *arg, char **argv, int count);
int rightArrow(char* right, char* input, pid_t pid, int status, char *arg, char **argv, int count);
int leftArrow(char* left, char* input, pid_t pid, int status, char *arg, char **argv, int count);
int piping(char* pipe, char* input, pid_t pid, int status, char *arg, char **argv, int count);

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

int leftArrow(char* left, char* input, pid_t pid, int status, char *arg, char **argv, int count){
  int file = open((left+2), O_RDONLY);

  if((pid = fork()) < 0) {
    fprintf(stderr, "error forking child process\n");
    close(file);
    return -1;
  }

  if(pid == 0) {
    arg = strtok(input, " ");
    count = 0;
    while(arg < left) {
      argv[count] = arg;
      arg = strtok(NULL, " ");
      count++;
    }
    argv[count] = NULL;
    close(0);
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

int piping(char* pip, char* input, pid_t pid, int status, char *arg, char **argv, int count){
  pid_t pid2;
  int pipeID[2];
  int status1;
  int status2;

  if((status = pipe(pipeID)) == -1) {
    perror("Bad pipe");
    return -1;
  }
  int background = 0;
  arg = strtok(pip+2, " ");
  count = 0;
  while(arg != NULL) {
    argv[count] = arg;
    arg = strtok(NULL, " ");
    count++;
  }

  argv[count] = NULL;
  if(*argv[count-1] == '&') {
    argv[count-1] = NULL;
    background = 1;
  }

  if((pid = fork()) == -1) {
    perror("Bad fork");
    return -1;
  }

  if(pid == 0) {
    close(0);
    dup (pipeID[0]);
    close(pipeID[0]);
    close(pipeID[1]);

    if(execvp(argv[0], argv) < 0) {
      fprintf(stderr, "error: Invalid command1\n");
      return -1;
    }
  }

  if((pid2 = fork()) == -1) {
    perror("Bad fork");
    return -1;
  }
  if(pid2 == 0) {
    arg = strtok(input, " ");
    count = 0;
    while(arg < pip) {
      argv[count] = arg;
      arg = strtok(NULL, " ");
      count++;
    }
    argv[count] = NULL;

    close(1);
    dup(pipeID[1]);
    close(pipeID[0]);
    close(pipeID[1]);

    if(execvp(argv[0], argv) < 0) {
      fprintf(stderr, "error: Invalid command2\n");
      return -1;
    }
  }
  else if(background == 0){
    while(wait(&status2) != pid2);
  }
  return 0;
}

int command(char* input, pid_t pid, int status, char *arg, char **argv, int count){
  char* arrow;

  if((arrow = strstr(input, ">"))) {
    return rightArrow(arrow,input,pid,status,arg,argv,count);
  }
  if((arrow = strstr(input, "<"))) {
    return leftArrow(arrow,input,pid,status,arg,argv,count);
  }
  if((arrow = strstr(input, "|"))) {
    return piping(arrow,input,pid,status,arg,argv,count);
  } 

  if ((pid = fork()) < 0) {
    fprintf(stderr, "error forking child process\n");
    return -1;
  }

  int background = 0;
  arg = strtok(input, " ");
  count = 0;
  while(arg != NULL) {
    argv[count] = arg;
    arg = strtok(NULL, " ");
    count++;
  }
  argv[count] = NULL;
  if(*argv[count-1] == '&') {
    argv[count-1] = NULL;
    background = 1;
  }

  if (pid == 0) {
    if(execvp(argv[0], argv) < 0) {
      fprintf(stderr, "error: Invalid command\n");
      return -1;
    }
  }

  else if(background == 0){
    while(wait(&status) != pid);
  }

  return 0;
}

