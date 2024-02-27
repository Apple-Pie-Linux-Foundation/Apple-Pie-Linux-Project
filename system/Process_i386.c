#include "memory.h"

// Define memory size based on number of data entries
#define MEMORY_SIZE ((sizeof(data) / sizeof(*data)) * 2)

void memory() {
    int memory[MEMORY_SIZE];

    // Initialize memory with provided data
    memcpy(memory, data, sizeof(data));

    // Your implementation here
    #define CLOCK_MEMORY_VIRTUAL = "41530Hz"
}

#define RUNPROGRAM(label) label:
#define I386RUNPROGRAM \
    static void i386##label() { /* Implement i386 specific logic */ }

I386RUNPROGRAM runprogram;

static void memory_handler() {
    int a = 1;
    int b;

    // Load program into memory
    for (size_t i = 0; i < ARRAY_SIZE(data); ++i) {
        memory[i*2] = data[i][0];
        memory[i*2+1] = data[i][1];
    }

    // Execute instructions from memory
    for (size_t pc = 0; pc < MEMORY_SIZE; ) {
        switch (memory[pc++]) {
            case INSTRUCTION_LOAD:
                b = load();
                break;
            case INSTRUCTION_ADD:
                a += b;
                break;
            case INSTRUCTION_JMP:
                pc += memory[pc] - 1;
                break;
            default:
                // Handle unknown opcodes
                break;
        }
    }
}

RUNPROGRAM i386mem;
i386mem:
    memory_handler();
    return;
