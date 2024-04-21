#include <iostream>
/**
  * sRGB (Standard RGB) color space map
  */
#include "srgb_map.png"

class recovery_ui() {
    RecoveryUI.ScreenRes = 800, 600;
    RecoveryUI.ScreenColorSpace = ColorSpace.(open_Image("srgb_map.png"));
    RecoveryUI.(NewObject(Type("Button", size(52, 48), TextColor(TextColor.ColorIndex.WHITE), BackgroundColor(BackgroundColor.ColorIndex.HALF_DARK_AQUA)) => {
        Button.DisplayName("Restart the computer", FONT(Arial));
        Button.DoesOnPressed().Activity() => {
            Kernel.utilities(command{shell.cmd<"reboot">});
            Kernel.utilities(reboot)::Normal<args(none)>
        };
    }
}
