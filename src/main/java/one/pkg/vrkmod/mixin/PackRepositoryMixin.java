package one.pkg.vrkmod.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PackRepository.class)
@Environment(EnvType.CLIENT)
public abstract class PackRepositoryMixin {
    @Unique
    private static final String VANILLA_PACK_ID = "vanilla";

    /*@Shadow
    public abstract void reload();*/

    @Unique
    private boolean isValidForSort() {
        return PackRepository.class.cast(this) == Minecraft.getInstance().getResourcePackRepository();
    }

    @Inject(method = "rebuildSelected", at = @At("RETURN"), cancellable = true)
    private void vrkmod$sortRebuildSelected(CallbackInfoReturnable<List<Pack>> cir) {
        ObjectArrayList<Pack> list = new ObjectArrayList<>(cir.getReturnValue());
        if (!isValidForSort() || list.isEmpty() || VANILLA_PACK_ID.equals(list.get(0).getId()))
            return;

        list.sort((pack1, pack2) -> {
            boolean pack1IsVanilla = VANILLA_PACK_ID.equals(pack1.getId());
            boolean pack2IsVanilla = VANILLA_PACK_ID.equals(pack2.getId());

            if (pack1IsVanilla && !pack2IsVanilla) return -1;
            if (!pack1IsVanilla && pack2IsVanilla) return 1;
            return 0;
        });
        cir.setReturnValue(list);
    }


    /*@Inject(method = "openAllSelected", at = @At(value = "HEAD"))
    public void vrkmod$openAllSelected(CallbackInfoReturnable<List<PackResources>> cir) {
        this.reload();
    }*/
}
