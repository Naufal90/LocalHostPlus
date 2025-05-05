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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    @Shadow
@Final
private List<Drawable> drawables;
    //protected GameMenuScreenMixin(Text title) {
        //super(title);
    //}

    //@Unique
    //private <T extends Element & Drawable & Selectable> void add(T element) {
        //this.addDrawableChild(element);
    //}

    @Inject(method = "initWidgets", at = @At("TAIL"))
private void onInit(CallbackInfo info) {
    int centerX = this.width / 2;
    int buttonWidth = 204;
    int buttonHeight = 20;

    // Posisi target untuk tombol LocalHostPlus (di antara "Report Bugs" dan "Options...")
    int insertY = this.height / 4 + 96;

    // Geser tombol yang berada di bawah posisi target ke bawah
    for (Drawable drawable : this.drawables) {
        if (drawable instanceof ButtonWidget button && button.getY() >= insertY) {
            button.setY(button.getY() + buttonHeight + 24); // geser ke bawah
        }
    }

    // Tambahkan tombol LocalHostPlus
    ButtonWidget localhostButton = ButtonWidget.builder(
        Text.literal("LocalHostPlus"),
        b -> MinecraftClient.getInstance().setScreen(new HotspotSettingsScreen(this))
    ).position(centerX - buttonWidth / 2, insertY).size(buttonWidth, buttonHeight).build();

    this.addDrawableChild(localhostButton);
}
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }
}
