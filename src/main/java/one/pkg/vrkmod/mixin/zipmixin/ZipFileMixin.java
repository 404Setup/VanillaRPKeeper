package one.pkg.vrkmod.mixin.zipmixin;

import com.github.luben.zstd.ZstdInputStream;
import com.llamalad7.mixinextras.sugar.Local;
import one.pkg.vrkmod.util.VRKZipTarget;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;

@Mixin(value = ZipFile.class, remap = false)
public class ZipFileMixin {
    @Inject(method = "getInputStream", at = @At(value = "INVOKE", target = "Lorg/apache/commons/compress/archivers/zip/UnsupportedZipFeatureException;<init>(Lorg/apache/commons/compress/archivers/zip/ZipMethod;Lorg/apache/commons/compress/archivers/zip/ZipArchiveEntry;)V"), cancellable = true, remap = false)
    private void vrkmod$getInputStreamWithZstdSupport(ZipArchiveEntry entry, CallbackInfoReturnable<InputStream> cir, @Local InputStream is) throws IOException {
        if (entry.getMethod() == VRKZipTarget.ZSTD_METHOD) {
            cir.setReturnValue(new ZstdInputStream(is));
        }
    }
}
