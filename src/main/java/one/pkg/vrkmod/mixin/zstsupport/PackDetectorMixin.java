package one.pkg.vrkmod.mixin.zstsupport;

import net.minecraft.server.packs.repository.PackDetector;
import one.pkg.vrkmod.util.VRKZipTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PackDetector.class)
public class PackDetectorMixin {
    @Redirect(method = "detectPackResources", at = @At(value = "INVOKE", target = "Ljava/lang/String;endsWith(Ljava/lang/String;)Z"))
    private boolean vrkmod$endsWithZstd(String string, String suffix) {
        return VRKZipTarget.hasZstdExtension(string) || string.endsWith(suffix);
    }
}
