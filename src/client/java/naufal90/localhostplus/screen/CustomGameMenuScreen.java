package naufal90.localhostplus.screen;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CustomGameMenuScreen extends GameMenuScreen {
    public CustomGameMenuScreen() {
        super(true); // Contoh jika membutuhkan Text
    }

    public ButtonWidget addCustomDrawableChild(ButtonWidget button) {
        return this.addDrawableChild(button);
    }
}
