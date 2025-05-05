package naufal90.localhostplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import naufal90.localhostplus.screen.HotspotSettingsScreen;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
     protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    //@Unique
    //private <T extends Element & Drawable & Selectable> void add(T element) {
        //this.addDrawableChild(element);
    //}

    @Inject(method = "initWidgets", at = @At("TAIL"))
private void onInit(CallbackInfo info) {
    List<ClickableWidget> buttons = ((ScreenAccessor)this).getDrawables().stream()
        .filter(e -> e instanceof ClickableWidget)
        .map(e -> (ClickableWidget)e)
        .collect(Collectors.toList());

    int centerX = this.width / 2;
    int buttonWidth = 204;
    int buttonHeight = 20;
    int spacing = 24;

    // Cari indeks tombol "Options..." sebagai patokan
    int insertIndex = -1;
    for (int i = 0; i < buttons.size(); i++) {
        ClickableWidget b = buttons.get(i);
        if (b instanceof ButtonWidget btn && btn.getMessage().getString().equals("Options...")) {
            insertIndex = i;
            break;
        }
    }

    // Default posisi Y awal
    int startY = this.height / 4 + 48;

    if (insertIndex != -1) {
        ButtonWidget localhostButton = ButtonWidget.builder(
            Text.literal("LocalHostPlus"),
            b -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(this))
        ).dimensions(centerX - buttonWidth / 2, 0, buttonWidth, buttonHeight).build();

        buttons.add(insertIndex, localhostButton);
    }

    // Atur ulang posisi semua tombol
    for (int i = 0; i < buttons.size(); i++) {
        ClickableWidget b = buttons.get(i);
        b.setY(startY + i * spacing);
    }
}
}
