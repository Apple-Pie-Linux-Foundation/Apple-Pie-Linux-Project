#ifndef STB_IMAGE_H
#define STB_IMAGE_H

#define STB_IMAGE_IMPLEMENTATION
#include <stdio.h>

typedef unsigned char stbi_uc;

int stbi_write_png(char const *filename, int w, int h, int comp, void *data);

#endif // STB_IMAGE_H
