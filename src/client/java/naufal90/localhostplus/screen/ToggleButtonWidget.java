package naufal90.localhostplus.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ToggleButtonWidget extends ButtonWidget {
    private boolean value;
    private final String label;

    public ToggleButtonWidget(int x, int y, int width, int height, String label, boolean initialValue) {
        super(x, y, width, height, 
              Text.literal(label + ": " + (initialValue ? "ON" : "OFF")),
              button -> ((ToggleButtonWidget) button).toggle(),
              DEFAULT_NARRATION_SUPPLIER);
        this.value = initialValue;
        this.label = label;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Custom rendering jika diperlukan
        super.render(context, mouseX, mouseY, delta);
    }

    public void toggle() {
        this.value = !this.value;
        this.setMessage(Text.literal(label + ": " + (value ? "ON" : "OFF")));
    }

    public boolean getValue() {
        return this.value;
    }
}
