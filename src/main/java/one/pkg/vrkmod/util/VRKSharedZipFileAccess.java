package one.pkg.vrkmod.util;

import net.minecraft.server.packs.FilePackResources;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class VRKSharedZipFileAccess extends FilePackResources.SharedZipFileAccess {
    private final Logger logger = LoggerFactory.getLogger(VRKSharedZipFileAccess.class);
    private ZipFile vzipFile;

    protected VRKSharedZipFileAccess(File file) {
        super(file);
    }

    public static VRKSharedZipFileAccess access(File file) {
        return new VRKSharedZipFileAccess(file);
    }

    @Nullable
    public ZipFile getVRKZipFile() {
        if (this.failedToLoad) {
            return null;
        } else {
            if (this.vzipFile == null) {
                try {
                    this.vzipFile = ZipFile.builder().setFile(this.file).get();
                } catch (IOException iOException) {
                    logger.error("Failed to open pack {}", this.file, iOException);
                    this.failedToLoad = true;
                    return null;
                }
            }

            return this.vzipFile;
        }
    }

    @Override
    public void close() {
        if (this.vzipFile != null) {
            IOUtils.closeQuietly(this.vzipFile);
            this.vzipFile = null;
        }
    }
}
