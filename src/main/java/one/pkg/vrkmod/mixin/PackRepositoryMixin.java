package one.pkg.vrkmod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(PackRepository.class)
@Environment(EnvType.CLIENT)
public abstract class PackRepositoryMixin {
    @Unique
    private static final String VANILLA_PACK_ID = "vanilla";

    @Shadow
    public abstract void reload();

    @Redirect(method = "reload", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
    public Stream<Pack> vrkmod$reload(List<Pack> instance) {
        if (
                PackRepository.class.cast(this) != Minecraft.getInstance().getResourcePackRepository()
                        || instance.isEmpty()
                        || instance.get(0).getId().equals(VANILLA_PACK_ID)
        )
            return instance.stream();
        return instance.stream()
                .sorted((pack1, pack2) -> {
                    boolean pack1IsVanilla = VANILLA_PACK_ID.equals(pack1.getId());
                    boolean pack2IsVanilla = VANILLA_PACK_ID.equals(pack2.getId());

                    if (pack1IsVanilla && !pack2IsVanilla) return -1;
                    if (!pack1IsVanilla && pack2IsVanilla) return 1;
                    return 0;
                });
    }

    @Inject(method = "openAllSelected", at = @At(value = "HEAD"))
    public void vrkmod$openAllSelected(CallbackInfoReturnable<List<PackResources>> cir) {
        this.reload();
    }
}
