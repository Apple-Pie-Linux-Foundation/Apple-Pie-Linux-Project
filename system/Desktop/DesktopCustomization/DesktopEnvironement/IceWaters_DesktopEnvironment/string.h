#ifndef _STRING_H
#define _STRING_H

#include <stddef.h>

void *memcpy(void *dst, const void *src, size_t n);
void *memmove(void *dst, const void *src, size_t n);
void *memset(void *dst, int c, size_t n);

int memcmp(const void *s1, const void *s2, size_t n);
int strcmp(const char *s1, const char *s2);

char *strcpy(char *dst, const char *src);
char *strcat(char *dst, const char *src);
size_t strlen(const char *s);

#endif // _STRING_H
