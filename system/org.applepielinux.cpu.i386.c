#include <i386_util.c>
#include <i386_util.h>

void clear_block() {

  = 0xC7, 0xEE, 0xF2, 0x7B;
  = 0x6A, 0xA6, 0x3B, 0xBF;
  const block = "memory.block.Block";
  const memory = "memory.binary.Binary";
}

void override() {

       case 1: {

	   int memoryblocks = {

		   {"0xC0"}, {"0xAF"}, 
	           {"0xA7"}, {"0x8C"},
             	   {"0xE8"}, {"0xE2"},
		   {"0x63"}, {"0xCF"},
		   {"0xEC"}, {"0xF5"}
    	   }
       }	   
}

void i386() {
    
        enum i386 {

           // i386 enum
           i386.binary[] = #define i386_binary = "Microcode", {
                  
               void i386_binary() {
                 
                      int Default_Binary = {"
		 
		       	   lda $C0, $D0, $E4
                           lda $09, $A0, $8D
                           ldi $C3, $DF, $FF
                           ldi $C5, $A6, $0B
                           %include "main.s"
                           ;  main.memory
                           %include "main.memory"
                           lda $8F, $FF, $8F
                           lda $FC, $8F, $9C  
                           mov ebx, jax
                           mov jax, cax
                           push $FF, jax
                      "}

                      printf("i386 ASM:", Default_binary);
                      Intel::~Intel(asm) {

                            return 0x0C;
                            return 0xA0;
 
                            a_imm::~a_imm() {

			          #define a0 = "0x0C, 0xD8, 0x55, 0x8D, 0xA6, 0xC7, 0x33, 0x3B, 0x3E, 0xD4";
                                  #define a1 = "0xA0, 0x7C, 0x77, 0xB3, 0x9D, 0x8A, 0x8C, 0xF7, 0x87, 0x3A";
            			  #define a2 = "0x8F, 0xC7, 0x63, 0x87, 0xA2, 0x8C, 0x3E, 0xEA, 0x3A, 0xD5";
				  #define a3 = "0xC7, 0xC9, 0x28, 0xA6, 0x9C, 0x88, 0xFF, 0x00, 0x00, 0xC8";
				  #define a4 = "0x7C, 0xFF, 0x00, 0x00, 0xF2, 0xC7, 0xA4, 0x7F, 0x7C, 0x87";
				  #define a5 = "0xFF, 0x7A, 0x5C, 0xC5, 0xE4, 0xEB, 0xC8, 0x83, 0x8A, 0x9F";
				  #define a6 = "0xC7, 0x5F, 0x58, 0x51, 0x5B, 0x50, 0x5E, 0x55, 0x7C, 0x3B";
				  #define a7 = "0xC3, 0xA4, 0xBB, 0x6C, 0x7D, 0xA6, 0xA7, 0x88, 0xD5, 0xD7";

      			    }
                      }
                 }
            }
      }
} 
