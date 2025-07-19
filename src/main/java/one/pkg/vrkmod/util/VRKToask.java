package one.pkg.vrkmod.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class VRKToask {
    public static void sendToast(String title) {
        Minecraft.getInstance().doRunTask(() -> {
            var manager = Minecraft.getInstance().getToastManager();
            manager.addToast(
                    new SystemToast(
                            SystemToast.SystemToastId.PACK_LOAD_FAILURE,
                            Component.translatable("vrkmod.toast.wrong_shaderpack.title"),
                            Component.literal(ChatFormatting.GOLD + title + ChatFormatting.RESET)
                                    .append(Component.translatable("vrkmod.toast.wrong_shaderpack.description"))
                    )
            );
        });
    }
}
