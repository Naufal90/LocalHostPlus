import net.minecraft.text.Text;

public class CustomGameMenuScreen extends GameMenuScreen {
    public CustomGameMenuScreen() {
        super(true); // Contoh jika membutuhkan Text
    }

    public ButtonWidget addCustomDrawableChild(ButtonWidget button) {
        return this.addDrawableChild(button);
    }
}
