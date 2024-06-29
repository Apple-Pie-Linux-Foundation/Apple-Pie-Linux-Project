#ifndef MICROCODE_H
#define MICROCODE_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Microcode representation type. You should replace this with your custom
 * microcode representation type.
 */
typedef uint8_t microcode_opcode_t;

/**
 * Maximum number of opcodes supported by the microcode interface. Update this
 * value according to the maximum number of opcodes in total across all architectures.
 */
#define MAX_MICROCODE_OPCODES 1024

/**
 * Structure representing a single microcode entry. Extend this structure with
 * any additional members required for your specific microcode.
 */
typedef struct _microcode_entry {
    microcode_opcode_t opcode;       ///< Opcode identifier
    uint32_t address;              ///< Effective address where the instruction is executed
    uint32_t length;               ///< Length of the corresponding machine instruction
} microcode_entry_t;

/**
 * Function prototype for loading microcode data from a file into a buffer.
 * Replace this with your own method for loading microcode data.
 */
int load_microcode_from_file(const char *filename, microcode_entry_t **entries, size_t *num_entries, size_t max_entries);

/**
 * Array holding all microcode entries for each architecture.
 */
extern const microcode_entry_t *const *microcode_per_architecture;

#define ARCHITECTURES_NUM 8

/**
 * Total number of microcode entries across all architectures.
 */
#define TOTAL_MICROCODE_ENTRIES \
    (ARCHITECTURES_NUM * (MAX_MICROCODE_OPCODES / ARCHITECTURES_NUM))

/**
 * Loads microcode data for all architectures.
 */
void init_microcode();

#ifdef __cplusplus
}
#endif

#endif /* MICROCODE_H */
