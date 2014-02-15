#include <stdio.h>
#include <Windows.h>

int main(int argc, char **argv) {

  OFSTRUCT ofs;
  HFILE old = OpenFile(argv[1], &ofs, OF_READ); 

  if(old == HFILE_ERROR) {
    fprintf(stderr, "error: %s not found", argv[1]);
    return 1;
  }

  HANDLE new = CreateFile(argv[2], GENERIC_WRITE, FILE_SHARE_DELETE|
		    FILE_SHARE_READ|FILE_SHARE_WRITE, NULL, CREATE_NEW,
                    FILE_ATTRIBUTE_NORMAL, NULL);

  if(new == INVALID_HANDLE_VALUE) {
    fprintf(stderr, "error: %s already exists", argv[2]);
    CloseHandle((HANDLE)old);
    return 1;
  }

  LPVOID read[512];
//  LPDWORD bytesRead = 0;
  int bytesRead = 0;
  int total = 0;

  ReadFile((HANDLE)old, read, 512, &bytesRead, NULL);

  while(bytesRead > 0) {
    total = total + (int)bytesRead;
    WriteFile(new, read, bytesRead, &bytesRead, NULL);
    ReadFile((HANDLE)old, read, 512, &bytesRead, NULL);
  }

  printf("%d bytes copied\n", total);

  CloseHandle(new);
  CloseHandle((HANDLE)old);  

  return 0;
}
