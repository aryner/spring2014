#include <stdio.h>
#include <Windows.h>

#define NAME "alex ryner"

int main() {
	HANDLE console = GetStdHandle(STD_OUTPUT_HANDLE);
	DWORD wrote;
	char greeting[35];
	sprintf(greeting, "Hello 415, it's me %s\n", NAME);
	WriteFile(console, greeting, sizeof(greeting), &wrote, NULL);

	return 0;
}
