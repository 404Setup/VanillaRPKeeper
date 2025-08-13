package one.pkg.vrkmod.mixin.moreformat;

import one.pkg.vrkmod.util.VRKZipTarget;
import org.apache.commons.compress.archivers.zip.ZipMethod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ZipMethod.class, remap = false)
public class ZipMethodMixin {
    @Inject(method = "getMethodByCode", at = @At(value = "HEAD"), cancellable = true)
    private static void vrkmod$getMethodByCode(int code, CallbackInfoReturnable<ZipMethod> cir) {
        if (code == VRKZipTarget.ZSTD_METHOD || code == VRKZipTarget.BROTLI_METHOD)
            cir.setReturnValue(ZipMethod.UNKNOWN);
    }
}
