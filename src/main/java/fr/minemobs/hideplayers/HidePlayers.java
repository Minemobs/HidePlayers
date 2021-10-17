package fr.minemobs.hideplayers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.glfw.GLFW;

public class HidePlayers implements ModInitializer {

    private static boolean renderPlayer;
    private KeyBinding renderPlayerKey;

    @Override
    public void onInitialize() {
        var LOGGER = LogFactory.getLog("HidePlayers");
        renderPlayer = true;
        renderPlayerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hideplayers.hideplayers",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeyBinding.UI_CATEGORY
        ));
        ClientTickEvents.END_CLIENT_TICK.register(unused -> {
            while(renderPlayerKey.wasPressed()) {
                renderPlayer = !renderPlayer;
                LOGGER.info(renderPlayer);
            }
        });
    }

    public static boolean isRenderPlayer() {
        return renderPlayer;
    }
}
