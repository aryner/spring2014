#include <windows.h>
#include <stdio.h>
#include <tchar.h>

int main(){
  TCHAR input[4096];
  TCHAR thing[4096];
  char *pos;
  STARTUPINFO si;
  PROCESS_INFORMATION pi;

  ZeroMemory(&si, sizeof(si));
  si.cb = sizeof(si);
  ZeroMemory(&pi, sizeof(pi));
  
  while(1) {
    printf("myShell> ");
    fgets(input, 4096, stdin);
    if((pos = strchr(input, '\n')) != NULL){
      *pos = '\0';
    }

    if(!strncmp(input,"exit",4)) {
      break;
    }

    sprintf(thing, "cmd /C %s", input);


    if (!CreateProcess(
		    NULL,
		    thing,
		    NULL,
		    NULL,
		    FALSE,
		    0,
		    NULL,
		    NULL,
		    &si,
                    &pi
		 )) {
        fprintf(stderr, "error in process creation: (%d)\n",GetLastError());
    }
    

    WaitForSingleObject(pi.hProcess, INFINITE);
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

  }

  return 0;
}
