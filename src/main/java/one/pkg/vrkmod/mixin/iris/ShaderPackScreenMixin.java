package one.pkg.vrkmod.mixin.iris;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.gui.element.ShaderPackSelectionList;
import net.irisshaders.iris.gui.screen.ShaderPackScreen;
import one.pkg.vrkmod.ModMain;
import one.pkg.vrkmod.util.VRKToask;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShaderPackScreen.class, remap = false)
public class ShaderPackScreenMixin {
    @Inject(method = "applyChanges", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/config/IrisConfig;setShaderPackName(Ljava/lang/String;)V", shift = At.Shift.AFTER, by = 2))
    private void vrkmod$applyChanges(CallbackInfo ci,
                                     @Local ShaderPackSelectionList.ShaderPackEntry entry) {
        Thread.ofVirtual().start(() -> {
            var path = FabricLoader.getInstance().getGameDir().resolve("shaderpacks").resolve(entry.getPackName());
            var file = path.toFile();
            if (!file.exists()) return;
            if (file.isDirectory()) {
                var meta = path.resolve("pack.mcmeta").toFile();
                if (meta.exists()) VRKToask.sendToast(file.getName());
            } else if (file.isFile()) {
                try (var zip = ZipFile.builder().setFile(file).get()) {
                    var target = zip.getEntry("pack.mcmeta");
                    if (target != null) VRKToask.sendToast(file.getName());
                } catch (Exception e) {
                    ModMain.logger.error("Failed to open shaderpack {}", file, e);
                }
            }
        });
    }
}
