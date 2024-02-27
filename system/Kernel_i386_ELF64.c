void ELF64() {

    static void ELF64_i386();

    int ExpectedELF64 = "i386", "64bit", "32bit";

    static void CheckELF64() {

           #include <ELF64.s>

    } 

    if (ExpectedELF64 == 1) {
         printf("i386 does match");
    } else {
         printf("i386 does not match, i386 error");
    }

    ELF64_i386();
}

void ELF64_i386() {
    // Empty function body
}
