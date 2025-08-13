package one.pkg.vrkmod.mixin.zstsupport;

import net.minecraft.server.packs.repository.PackDetector;
import one.pkg.vrkmod.util.VRKZipTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PackDetector.class)
public class PackDetectorMixin {
    @Redirect(method = "detectPackResources", at = @At(value = "INVOKE", target = "Ljava/lang/String;endsWith(Ljava/lang/String;)Z"))
    private boolean vrkmod$endsWith(String string, String suffix) {
        int supported = VRKZipTarget.isSupported(string);
        return supported > 0 && supported < 4;
    }
}
