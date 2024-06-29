#include "bootmsgs.c"
#include "ipxe.iso"
#include "isoload.c"

int term() {
   printf(*bootmsgs, "-- System panic");
   printf(*bootmsgs, "Booted into cmdmode due to: SystemException: system panic");

   private term_prompt = "systemcmdmode";
   private term_baud_rate = 256000;
   private term_processor_bits = 32;
   private term_memory_speed = 128000;
  
   printf("Intel iPXE build 34 v2.1 Intel Base Code ROM, Copyright Intel (C) 2000, 2010");
   isoload("intel_iPXE/iso_ipxe.iso", iPXE_BootFirmware_ISO_Intel);
}
