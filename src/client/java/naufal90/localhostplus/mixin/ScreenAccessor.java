package naufal90.localhostplus.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("drawables")
    List<Drawable> getDrawables();
    
    @Accessor("children")
    List<Element> getChildren();
    
    @Accessor("selectables")
    List<Selectable> getSelectables();
    
    @Accessor("drawables")
    void setDrawables(List<Drawable> drawables);
    
    @Accessor("children")
    void setChildren(List<Element> children);
    
    @Accessor("selectables")
    void setSelectables(List<Selectable> selectables);
}
