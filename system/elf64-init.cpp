#define ELF64_HEADER_SIZE 16
#define bin_elf64 () ({ \
    const char elf64_hex[] = {"0x" \
        "73," "A5," "C8," "83," "00," "00," "00," "00," "00," "38," "AC," "CC," "3E," "D2," "A6," "A4," "39," "3D," "82," "C0," "A3," "A9," "9F," "C8"}; \
    (const unsigned char *)elf64_hex \
})

void elf64_init(void) {
    char elf64_expected_memory[ELF64_HEADER_SIZE];

    printf("elf64 init starting\n");

    memcpy(elf64_expected_memory, bin_elf64(), ELF64_HEADER_SIZE);

    unsigned long *expected_header = (unsigned long*) elf64_expected_memory;

    if (*expected_header != ELF64_MAGIC) {
        fprintf(stderr, "Invalid ELF64 magic number.\n");
        exit(EXIT_FAILURE);
    }

    printf("ELF64 header validated successfully.\n");
}
