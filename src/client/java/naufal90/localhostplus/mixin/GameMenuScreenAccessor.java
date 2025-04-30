package naufal90.localhostplus.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenAccessor {
    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T element);
}
