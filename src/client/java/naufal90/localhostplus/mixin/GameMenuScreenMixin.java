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
    private void addFullWidthButton(CallbackInfo ci) {
        // 1. Cari tombol "Options..." sebagai anchor
        ButtonWidget optionsButton = findButton("Options...");
        if (optionsButton == null) return;

        // 2. Buat tombol full-width
        int padding = 20; // Margin kiri/kanan
        int fullWidth = this.width - padding * 2;
        
        ButtonWidget localhostButton = ButtonWidget.builder(
            Text.literal("LocalHostPlus"),
            button -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(this))
        ).dimensions(
            padding, // Posisi X (margin kiri)
            optionsButton.getY(), // Posisi Y sama dengan Options
            fullWidth,
            20 // Tinggi standar
        ).build();

        // 3. Tambahkan tombol ke screen
        this.addDrawableSelectableElement(localhostButton);

        // 4. Geser tombol di bawahnya ke bawah
        shiftButtonsDown(optionsButton.getY(), localhostButton.getHeight() + 4);
    }

    // Helper: Cari tombol berdasarkan teks
    @Unique
    private ButtonWidget findButton(String text) {
        for (Drawable d : ((ScreenAccessor) this).getDrawables()) {
            if (d instanceof ButtonWidget btn && btn.getMessage().getString().equals(text)) {
                return btn;
            }
        }
        return null;
    }

    // Helper: Geser tombol di bawah posisi Y tertentu
    @Unique
    private void shiftButtonsDown(int yThreshold, int offset) {
        for (Drawable d : ((ScreenAccessor) this).getDrawables()) {
            if (d instanceof ButtonWidget btn && btn.getY() >= yThreshold) {
                btn.setY(btn.getY() + offset);
            }
        }
    }
}
