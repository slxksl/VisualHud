package net.brsk.hudmenu.mixin;

import net.brsk.hudmenu.client.HudAddScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.screens.ChatScreen", remap = false)
public abstract class ChatScreenMixin {
    private static final Logger HUDMENU_LOGGER = LoggerFactory.getLogger("hudmenu");

    @Inject(method = "mouseClicked", at = @At(value = "HEAD", remap = false), cancellable = true, remap = false)
    private void hudmenu$openWithRightClick(MouseButtonEvent event, boolean doubleClick,
                                            CallbackInfoReturnable<Boolean> cir) {
        HUDMENU_LOGGER.info("[hudmenu] ChatScreen click, button={}", event.button());
        if (event.button() == 1) {
            HUDMENU_LOGGER.info("[hudmenu] Opening HudAddScreen");
            Minecraft.getInstance().setScreen(new HudAddScreen((Screen) (Object) this, (int) event.x(), (int) event.y()));
            cir.setReturnValue(true);
        }
    }
}
