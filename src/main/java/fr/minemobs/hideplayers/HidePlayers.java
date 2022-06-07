package fr.minemobs.hideplayers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HidePlayers implements ModInitializer {

    private static boolean renderPlayer;
    private Gson gson;
    private static double distance = 4096d;

    @Override
    public void onInitialize() {
        Log logger = LogFactory.getLog("HidePlayers");
        renderPlayer = true;
        this.gson = new GsonBuilder().create();
        KeyBinding renderPlayerKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.hideplayers.hideplayers",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_H,
                    "key.hideplayers.hideplayers"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(unused -> {
            while(renderPlayerKey.wasPressed()) {
                renderPlayer = !renderPlayer;
                logger.info(renderPlayer);
            }
        });
        Path configPath = Paths.get(FabricLoaderImpl.INSTANCE.getConfigDir().toAbsolutePath().toString(), "hideplayers.json");
        if (!Files.exists(configPath)) {
            try {
                Files.write(configPath, "{\"distance\": 64}".getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        try {
            double d = gson.fromJson(Files.newBufferedReader(configPath), JsonObject.class).getAsJsonPrimitive("distance").getAsDouble();
            if(d <= 0) return;
            distance = Math.pow(d, 2);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static boolean isRenderPlayer() {
        return renderPlayer;
    }

    public static double getDistance() {
        return distance;
    }
}
