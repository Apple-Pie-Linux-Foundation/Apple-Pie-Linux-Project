#include <iostream>
/**
  * sRGB (Standard RGB) color space map
  */
#include "srgb_map.png"

class recovery_ui() {
    RecoveryUI.ScreenRes = 800, 600;
    RecoveryUI.ScreenColorSpace = ColorSpace.(open_Image("srgb_map.png"));
}
