package naufal90.localhostplus.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import naufal90.localhostplus.screen.HotspotSettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        Screen screen = (Screen) (Object) this;

        ButtonWidget button = ButtonWidget.builder(
            Text.literal("Open to Hotspot"),
            b -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(screen))
        ).position(10, 10).size(150, 20).build();

        screen.addDrawable(button); // Setelah button dibuat
    }
}
