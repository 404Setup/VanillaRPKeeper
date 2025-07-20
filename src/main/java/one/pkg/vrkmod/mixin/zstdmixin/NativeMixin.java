package one.pkg.vrkmod.mixin.zstdmixin;

import com.github.luben.zstd.util.Native;
import com.llamalad7.mixinextras.sugar.Local;
import one.pkg.vrkmod.ModMain;
import one.pkg.vrkmod.util.VRKPlatform;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

// When zstd-jni cannot find the static link library, it just throws an error silently, rather than crashing.
// We did not import the full zstd-jni, so we have to do so.
// There are some mods that may be affected, please report them in my Issues.
@Mixin(value = Native.class, remap = false)
public class NativeMixin {
    @Shadow
    @Final
    private static String errorMsg;

    @Inject(method = "load(Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Ljava/lang/UnsatisfiedLinkError;<init>(Ljava/lang/String;)V", ordinal = 0), cancellable = true)
    private static void vrkmod$loadWithZstdSupport(File par1, CallbackInfo ci, @Local UnsatisfiedLinkError e) {
        UnsatisfiedLinkError err = new UnsatisfiedLinkError(e.getMessage() + "\n" + errorMsg);
        err.setStackTrace(e.getStackTrace());
        ModMain.logger.error("Failed to load zstd native library", err);
        if (VRKPlatform.isCanUseZSTD()) VRKPlatform.setCanUseZSTD(false);
        ci.cancel();
    }

    @Inject(method = "load(Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Ljava/lang/UnsatisfiedLinkError;<init>(Ljava/lang/String;)V", ordinal = 1), cancellable = true)
    private static void vrkmod$loadWithZstdSupport2(File par1, CallbackInfo ci, @Local(ordinal = 1) UnsatisfiedLinkError e, @Local(ordinal = 0) UnsatisfiedLinkError e1) {
        UnsatisfiedLinkError err = new UnsatisfiedLinkError(
                e.getMessage() + "\n" +
                        e1.getMessage() + "\n" +
                        errorMsg);
        err.setStackTrace(e1.getStackTrace());
        ModMain.logger.error("Failed to load zstd native library", err);
        if (VRKPlatform.isCanUseZSTD()) VRKPlatform.setCanUseZSTD(false);
        ci.cancel();
    }
}
