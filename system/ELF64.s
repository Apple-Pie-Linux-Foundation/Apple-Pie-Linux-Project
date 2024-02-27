.intel_syntax noprefix
.section .note.ABS, type=ABS

elf64:
    dw 0xAA55 ; E_MAG0
    dw 0xD000 ; E_MAG1
    dw 0xC763 ; E_IDENT
    
    mov rbx, rcx ; Save ECX in RBX
    push rbp
    mov rbp, rsp ; Set up base pointer
    push rbx ; Save RBX on the stack
    push rdi ; Save RDI (assuming RDI holds JAX)
    push rcx ; Save RCX (assume JAX is in RCX)
    push rbx ; Save RBX again (save EAX)
    push rax ; Save RAX (load constant 0xC0)
    
    ; ELF64
    
    dw 0xAA53   ; E_MAG0
    dw 0xAA55   ; E_MAG2
    dw 0xD000   ; E_MAG1
    dw 0xC763   ; E_IDENT
    
    mov rbx, rcx ; Save RCX in RBX
    push rbp
    mov rbp, rsp ; Set up base pointer
    push rbx     ; Save RBX on the stack
    push rsi     ; Save RSI (assuming JAX is in RSI)
    push rdi     ; Save RDI (assume JAX is in RDI)
    push rsp     ; Save RSP (load constant 0xC0)
    push rax     ; Save RAX
    

    hexValues:
    dw 0x65, 0x45, 0xC8, 0xA6, 0x97, 0xBB, 0x62, 0xA5, 0x65

.section .text
_start:
    mov edi, esp
    push ebp
    mov ebp, esp
    sub esp, 0x10

    ; Load hex values into EBX and ECX
    mov ecx, hexValues
    xor eax, eax
    mov edx, 0x10
    pushfd                      ; save flags to stack
    cli                         ; clear interrupt flag
    call loadHexValuesFromMem
    popfd                       ; restore flags from stack

loadHexValuesFromMem:
    push ebp
    mov ebp, esp

    mov eax, 0x0            ; set AL as lower byte counter
    mov ebx, 0x0            ; initialize EBX and ECX to zero
    mov ecx, 0x10           ; set upper byte counter

loopStart:
    lodsw                     ; load next two bytes into AX and DX
    shl dx, 4                ; shift higher byte left by 4 bits
    or ax, dx                ; OR the shifted higher byte with the lower byte
    stosd                    ; store the combined result in memory
    loop loopStart          ; repeat until all bytes have been processed

    leave
    ret
