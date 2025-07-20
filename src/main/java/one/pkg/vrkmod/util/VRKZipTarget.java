package one.pkg.vrkmod.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class VRKZipTarget {
    public static final int ZSTD_METHOD = 93;
    private static final byte[] ZSTD_MAGIC = {0x28, (byte) 0xB5, 0x2F, (byte) 0xFD};

    public static boolean isZstdStream(InputStream inputStream) throws IOException {
        if (!(inputStream instanceof PushbackInputStream pushbackStream))
            throw new IllegalArgumentException("InputStream must be PushbackInputStream");

        byte[] buffer = new byte[ZSTD_MAGIC.length];

        int bytesRead = pushbackStream.read(buffer);

        if (bytesRead > 0)
            pushbackStream.unread(buffer, 0, bytesRead);

        if (bytesRead >= ZSTD_MAGIC.length) {
            boolean isStandardZstd = true;
            for (int i = 0; i < ZSTD_MAGIC.length; i++) {
                if (buffer[i] != ZSTD_MAGIC[i]) {
                    isStandardZstd = false;
                    break;
                }
            }
            if (isStandardZstd) {
                return true;
            }
        }

        if (bytesRead >= 4) {
            int magic = ((buffer[3] & 0xFF) << 24) |
                    ((buffer[2] & 0xFF) << 16) |
                    ((buffer[1] & 0xFF) << 8) |
                    (buffer[0] & 0xFF);

            int minSkippable = 0x184D2A50;
            int maxSkippable = 0x184D2A5F;

            return magic >= minSkippable && magic <= maxSkippable;
        }

        return false;
    }

    public static PushbackInputStream createDetectableStream(InputStream originalStream) {
        if (originalStream instanceof PushbackInputStream existing) {
            try {
                byte[] testBuffer = new byte[4];
                int read = existing.read(testBuffer);
                if (read > 0) {
                    existing.unread(testBuffer, 0, read);
                }
                return existing;
            } catch (IOException e) {
                return new PushbackInputStream(originalStream, 4);
            }
        }
        return new PushbackInputStream(originalStream, 4);
    }

    public static boolean hasZstdExtension(String filename) {
        if (filename == null) return false;
        String lower = filename.toLowerCase();
        return lower.endsWith(".zst") || lower.endsWith(".zstd");
    }
}
