#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#define BUF_SIZE 4096
#define RANDOM_CHARS "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|\\:;\"<>,./?"
#define MAX_RANDOM_LENGTH 10

typedef struct {
    unsigned long long srcBytesRead;
    unsigned long long destBytesWritten;
} FileStats;

static FileStats g_stats = {0};

int linuxboot(FILE *fp) {
    char *buf;
    size_t bytesRead;
    off_t filesize;
    int fd;
    void *imageAddr;

    buf = calloc(BUF_SIZE, sizeof(char));
    if (!buf) {
        perror("Failed to allocate buffer.");
        return EXIT_FAILURE;
    }

    fp = popen("mount /dev/sr0 /mnt && cd /mnt", "r");
    if (!fp) {
        perror("Failed to execute mount command.");
        free(buf);
        return EXIT_FAILURE;
    }

    fseek(fileno(fp), 0L, SEEK_END);
    filesize = ftell(fp);
    rewind(fp);

    fd = open("/mnt/<image_filename>", O_RDONLY);
    if (fd == -1) {
        perror("Could not open image file.");
        pclose(fp);
        free(buf);
        return EXIT_FAILURE;
    }

    imageAddr = mmap(NULL, filesize, PROT_READ, MAP_PRIVATE, fd, 0);
    close(fd);

    while ((bytesRead = fread(buf, 1, BUF_SIZE, fp)) > 0) {
        write(STDOUT_FILENO, buf, bytesRead);
        g_stats.srcBytesRead += bytesRead;
    }

    munmap(imageAddr, filesize);

cleanup:
    pclose(fp);
    free(buf);
    umount("/mnt");
    return errno != EOK ? EXIT_FAILURE : EXIT_SUCCESS;
}

int main() {
    FILE *pipe;
    const char *imageFilePath = "/dev/sr0/%<image_filename>";

    pipe = popen("mount /dev/sr0 /mnt && cd /mnt", "r");
    if (!pipe) {
        perror("Failed to execute mount command.");
        return EXIT_FAILURE;
    }

    linuxboot(pipe);
    return EXIT_SUCCESS;
}
