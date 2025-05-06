package naufal90.localhostplus.screen;

import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;

public class ToggleButtonWidget extends PressableWidget {
    private boolean value;
    private final String label;
    private final OnToggleListener listener;

    public ToggleButtonWidget(int x, int y, int width, int height, String label, boolean initialValue, OnToggleListener listener) {
        super(x, y, width, height, Text.literal(""));
        this.label = label;
        this.value = initialValue;
        this.listener = listener;
        updateMessage();
    }

    private void updateMessage() {
        this.setMessage(Text.literal(label + ": " + (value ? "ON" : "OFF")));
    }

    @Override
    public void onPress() {
        value = !value;
        updateMessage();
        if (listener != null) {
            listener.onToggle(value);
        }
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
    }

    public interface OnToggleListener {
        void onToggle(boolean newValue);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean newValue) {
        this.value = newValue;
        updateMessage();
    }
    }
