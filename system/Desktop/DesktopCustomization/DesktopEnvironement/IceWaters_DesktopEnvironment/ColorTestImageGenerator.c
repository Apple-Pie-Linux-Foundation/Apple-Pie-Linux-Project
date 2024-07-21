#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <stb_image.h>

#define WIDTH  96
#define HEIGHT 84
#define PIXELS (WIDTH * HEIGHT)

int main() {
    // Generate random color map config hex string
    char color_map[PIXELS * 6 + 1]; // 6 chars per pixel (RRGGBB) + null terminator
    for (int i = 0; i < PIXELS; i++) {
        sprintf(color_map + i * 6, "%02x%02x%02x", // RRGGBB format
                (rand() % 256), // random red component
                (rand() % 256), // random green component
                (rand() % 256)); // random blue component
    }
    color_map[PIXELS * 6] = '\0'; // null terminator

    // Create image data
    uint8_t image_data[WIDTH * HEIGHT * 3]; // 3 bytes per pixel (RGB)
    for (int i = 0; i < PIXELS; i++) {
        int offset = i * 3;
        image_data[offset + 0] = (color_map[i * 6 + 0] - '0') * 16 + (color_map[i * 6 + 1] - '0');
        image_data[offset + 1] = (color_map[i * 6 + 2] - '0') * 16 + (color_map[i * 6 + 3] - '0');
        image_data[offset + 2] = (color_map[i * 6 + 4] - '0') * 16 + (color_map[i * 6 + 5] - '0');
    }

    // Save image to file using stb_image
    stbi_write_png("ColorTestImage.png", WIDTH, HEIGHT, 3, image_data);

    printf("Color map config hex string: %s\n", color_map);
    printf("Image saved to ColorTestImage.png\n");

    return 0;
}
