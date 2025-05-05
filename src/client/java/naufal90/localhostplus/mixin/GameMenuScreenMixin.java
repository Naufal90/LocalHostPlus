package naufal90.localhostplus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import naufal90.localhostplus.screen.HotspotSettingsScreen;
import naufal90.localhostplus.mixin.ScreenAccessor;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.stream.Collectors;

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
    private void addLocalHostButton(CallbackInfo ci) {
        // Cari tombol "Options..." sebagai anchor
        ButtonWidget optionsBtn = findButton("Options...");
        if (optionsBtn == null) return;

        // Buat tombol full-width (seperti "Back to Game")
        int buttonWidth = 200; // Lebar standar
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2; // Posisi tengah
        int y = optionsBtn.getY() - buttonHeight - 4; // Letakkan di atas "Options..."

        ButtonWidget localhostBtn = ButtonWidget.builder(
            Text.literal("LocalHostPlus"),
            btn -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(this))
        ).dimensions(x, y, buttonWidth, buttonHeight).build();

        // Tambahkan ke layar dengan cara yang benar
        this.addDrawableChild(localhostBtn);
    }

    // Helper untuk mencari tombol berdasarkan text
    @Unique
    private ButtonWidget findButton(String text) {
        for (Drawable d : ((ScreenAccessor) this).getDrawables()) {
            if (d instanceof ButtonWidget btn && btn.getMessage().getString().equals(text)) {
                return btn;
            }
        }
        return null;
    }
}
