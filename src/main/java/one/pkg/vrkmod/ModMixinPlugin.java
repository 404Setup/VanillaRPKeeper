package one.pkg.vrkmod;

import net.fabricmc.loader.api.FabricLoader;
import one.pkg.vrkmod.util.VRKPlatform;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModMixinPlugin implements IMixinConfigPlugin {
    private static final String[] zstdClasses = new String[]{
            "one.pkg.vrkmod.mixin.zstsupport.PackDetectorMixin",
            "one.pkg.vrkmod.mixin.zstsupport.ZipFileMixin",
            "one.pkg.vrkmod.mixin.zstsupport.ZipMethodMixin",
            "one.pkg.vrkmod.mixin.zstsupport.ZipUtilMixin",
    };

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("one.pkg.vrkmod.mixin.iris.ShaderPackScreenMixin"))
            return FabricLoader.getInstance().isModLoaded("iris");
        for (String zstdClass : zstdClasses)
            if (mixinClassName.equals(zstdClass)) return VRKPlatform.isCanUseZSTD();
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
