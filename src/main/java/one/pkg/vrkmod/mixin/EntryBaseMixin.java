package one.pkg.vrkmod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(PackSelectionModel.EntryBase.class)
@Environment(EnvType.CLIENT)
public abstract class EntryBaseMixin {
    @Unique
    private static final Set<String> PROTECTED_PACK_IDS = Set.of("vanilla", "fabric", "mod_data", "mod_resources");
    @Shadow
    @Final
    private Pack pack;

    @Shadow
    public abstract String getId();

    @Shadow
    protected abstract List<Pack> getSelfList();

    @Inject(method = "canMoveUp", at = @At("HEAD"), cancellable = true)
    private void vrkmod$canMoveUp(CallbackInfoReturnable<Boolean> cir) {
        if (PROTECTED_PACK_IDS.contains(this.getId())) {
            cir.setReturnValue(false);
            return;
        }

        List<Pack> list = this.getSelfList();
        int currentIndex = list.indexOf(this.pack);

        if (currentIndex > 0) {
            Pack packAbove = list.get(currentIndex - 1);
            if (PROTECTED_PACK_IDS.contains(packAbove.getId())) {
                cir.setReturnValue(false);
            }
        }

    }

    @Inject(method = "canMoveDown", at = @At("HEAD"), cancellable = true)
    private void vrkmod$canMoveDown(CallbackInfoReturnable<Boolean> cir) {
        if (PROTECTED_PACK_IDS.contains(this.getId())) {
            cir.setReturnValue(false);
            return;
        }

        List<Pack> list = this.getSelfList();
        int currentIndex = list.indexOf(this.pack);

        if (currentIndex >= 0 && currentIndex < list.size() - 1) {
            Pack packBelow = list.get(currentIndex + 1);
            if (PROTECTED_PACK_IDS.contains(packBelow.getId())) {
                cir.setReturnValue(false);
            }
        }

    }
}
