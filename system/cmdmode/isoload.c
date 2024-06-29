#include "term.h" // Assuming term.h contains all necessary declarations
#include "bootmsgs.h"
#include "microcode.h"
#include "memory.h"

#include "memory.mem"
#include "microcode.mem"

// Replace 'new' with malloc() or another memory allocation function
void* my_malloc(size_t size);

typedef struct _ISOLoader {
    void (*ISOLoaderCPUCompatibilty)(struct _ISOLoader *self);
 
} ISOLoader;

void iso_config() {
    ISOLoader *ISOLoader = malloc(sizeof(ISOLoader));


    char *microcode_memory = "microcode.mem";
    char *cpu_memory = "memory.mem";

    ISOLoader->ISOLoaderCPUCompatibilty = iso_loader_cpu_compatibility; // Assume iso_loader_cpu_compatibility is defined elsewhere

    // Allocate memory for various components
    ISOLoader->this.ISOLoaderCPUCompatibility = my_malloc(sizeof(*ISOLoader->this.ISOLoaderCPUCompatibilty));


    // Use malloc() instead of 'new'
    ISOLoader->CPUConnectionInstance = malloc(sizeof(struct _CPUConnectionInstance));
    ISOLoader->CPUMemorySpace = malloc(sizeof(struct _CPUMemorySpace));
    ISOLoader->CPUNullPointerHandler = malloc(sizeof(struct _CPUNullPointerHandler));
    ISOLoader->CPUExceptionHandler = malloc(sizeof(struct _CPUExceptionHandler));
    ISOLoader->CPUHardwareAccelerator = malloc(sizeof(struct _CPUHardwareAccelerator));
    ISOLoader->CPUInstructionCompiledAARCH = malloc(sizeof(struct _CPUInstructionCompiledAARCH[COMPILEDARCH_COUNT]));


    // Allocate memory for data segments
    size_t microcode_length = strlen(microcode_memory) + 1;
    unsigned char *microcode = malloc(microcode_length);
    read_file(microcode_memory, microcode, microcode_length);

    size_t cpu_memory_length = strlen(cpu_memory) + 1;
    unsigned char *cpu_data = malloc(cpu_memory_length);
    read_file(cpu_memory, cpu_data, cpu_memory_length);

    free(microcode_memory);
    free(cpu_memory);

    // Set up Group structure
    Group *group = malloc(sizeof(Group));
    group->CompatibilityCodeCPU = ISOLoader->CPUInstructionCompiledAARCH;
    group->CPU = cpu_data;
    group->Code = ISOLoader->CPUInstructionCompiledAARCH;

    // Free previously allocated memory for ISOLoader itself
    free(ISOLoader);
}
