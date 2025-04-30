package naufal90.localhostplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import naufal90.localhostplus.screen.HotspotSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T element);

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        ButtonWidget button = ButtonWidget.builder(
            Text.literal("Open to Hotspot"),
            b -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen((Screen)(Object)this))
        ).position(10, 10).size(150, 20).build();

        this.addDrawableChild(button);
    }
}
