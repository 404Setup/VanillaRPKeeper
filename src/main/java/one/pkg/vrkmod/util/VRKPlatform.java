package one.pkg.vrkmod.util;

public class VRKPlatform {
    private static boolean canUseZSTD;
    private static final boolean IS_AMD64;
    private static final boolean IS_AARCH64;
    private static final boolean IS_LINUX;
    private static final boolean IS_WINDOWS;
    private static final boolean IS_DARWIN;
    private static final boolean IS_FREEBSD;

    static {
        String osArch = System.getProperty("os.arch", "");
        IS_AMD64 = osArch.equals("amd64") || osArch.equals("x86_64");
        IS_AARCH64 = osArch.equals("aarch64") || osArch.equals("arm64");

        IS_LINUX = System.getProperty("os.name", "").equalsIgnoreCase("Linux");
        IS_WINDOWS = System.getProperty("os.name", "").toLowerCase().contains("windows");
        IS_DARWIN = System.getProperty("os.name", "").toLowerCase().contains("mac");
        IS_FREEBSD = System.getProperty("os.name", "").toLowerCase().contains("freebsd");

        canUseZSTD = (IS_LINUX /*|| IS_WINDOWS*/ || IS_DARWIN || IS_FREEBSD) && (IS_AMD64 || IS_AARCH64);
    }

    public static void setCanUseZSTD(boolean canUseZSTD) {
        VRKPlatform.canUseZSTD = canUseZSTD;
    }

    public static boolean isCanUseZSTD() {
        return canUseZSTD;
    }
}
