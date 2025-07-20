package one.pkg.vrkmod.mixin.zipmixin;

import one.pkg.vrkmod.util.VRKZipTarget;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ZipUtil.class, remap = false)
public class ZipUtilMixin {
    @Inject(method = "supportsMethodOf", at = @At(value = "HEAD"), cancellable = true)
    private static void vrkmod$supportsMethodOf(ZipArchiveEntry entry, CallbackInfoReturnable<Boolean> cir) {
        if (entry.getMethod() == VRKZipTarget.ZSTD_METHOD) cir.setReturnValue(true);
    }
}
