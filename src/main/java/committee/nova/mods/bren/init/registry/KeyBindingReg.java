package committee.nova.mods.bren.init.registry;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindingReg {
    public static final String KEY_RELOAD = "key.bren.reload";

    public static KeyMapping reloadKey = new KeyMapping(
            KEY_RELOAD,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,KeyMapping.CATEGORY_GAMEPLAY
            );

}
