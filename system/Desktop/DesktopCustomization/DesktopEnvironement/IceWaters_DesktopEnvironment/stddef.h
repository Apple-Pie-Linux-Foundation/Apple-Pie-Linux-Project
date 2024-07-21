#ifndef _STDDEF_H
#define _STDDEF_H

#ifndef NULL
#ifdef __cplusplus
#define NULL 0
#else
#define NULL ((void*)0)
#endif
#endif

typedef long int ptrdiff_t;

typedef unsigned long int size_t;

typedef int wchar_t;

#ifndef offsetof
#define offsetof(type, member) ((size_t)(&(((type *)0)->member)))
#endif

#endif // _STDDEF_H
