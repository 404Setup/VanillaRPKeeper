package one.pkg.vrkmod.mixin.iris;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList;
import net.irisshaders.iris.gui.screen.ShaderPackScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShaderPackScreen.class, remap = false)
public class ShaderPackScreenMixin {
    @Unique
    private static final Logger logger = LoggerFactory.getLogger("VanillaRPKeeper");

    @Inject(method = "applyChanges", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/config/IrisConfig;setShaderPackName(Ljava/lang/String;)V", shift = At.Shift.AFTER, by = 2))
    private void vrkmod$applyChanges(CallbackInfo ci,
                                     @Local ShaderPackSelectionList.ShaderPackEntry entry) {
        Thread.ofVirtual().start(() -> {
            var path = FabricLoader.getInstance().getGameDir().resolve("shaderpacks").resolve(entry.getPackName());
            var file = path.toFile();
            if (!file.exists()) return;
            if (file.isDirectory()) {
                var meta = path.resolve("pack.mcmeta").toFile();
                if (meta.exists()) vrkmod$openFileToast(file.getName());
            } else if (file.isFile()) {
                try (var zip = ZipFile.builder().setFile(file).get()) {
                    var target = zip.getEntry("pack.mcmeta");
                    if (target != null) vrkmod$openFileToast(file.getName());
                } catch (Exception e) {
                    logger.error("Failed to open shaderpack {}", file, e);
                }
            }
        });
    }

    @Unique
    private void vrkmod$openFileToast(String title) {
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
