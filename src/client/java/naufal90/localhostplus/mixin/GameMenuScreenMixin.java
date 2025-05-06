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
    private void insertLocalHostButton(CallbackInfo ci) {
        // 1. Cari tombol "Give Feedback" dan "Options..."
        ButtonWidget giveFeedbackBtn = findButton("Give Feedback");
        ButtonWidget optionsBtn = findButton("Options...");
        if (giveFeedbackBtn == null || optionsBtn == null) return;

        // 2. Hitung posisi baru (di bawah "Give Feedback", di atas "Options...")
        int x = optionsBtn.getX();
        int y = giveFeedbackBtn.getY() + giveFeedbackBtn.getHeight() + 4; // 4px spacing
        int width = 204; // Lebar standar
        int height = 20;

        // 3. Buat tombol baru
        ButtonWidget localhostBtn = ButtonWidget.builder(
            Text.literal("LocalHostPlus"),
            btn -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(this))
        ).dimensions(x, y, width, height).build();

        // 4. Tambahkan tombol
        this.addDrawableChild(localhostBtn);

        // 5. Geser semua tombol di bawah "Give Feedback" ke bawah
        int shiftAmount = height + 4;
        for (Drawable d : ((ScreenAccessor) this).getDrawables()) {
            if (d instanceof ButtonWidget btn && btn.getY() >= y) {
                if (btn != localhostBtn) { // Jangan geser tombol kita sendiri
                    btn.setY(btn.getY() + shiftAmount);
                }
            }
        }
    }

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
