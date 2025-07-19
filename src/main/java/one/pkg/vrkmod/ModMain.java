package one.pkg.vrkmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ModMain implements ModInitializer {
    public static final Logger logger = LoggerFactory.getLogger("VanillaRPKeeper");

    @Override
    public void onInitialize() {
    }
}
