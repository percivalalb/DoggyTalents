package doggytalents.common.addon;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import doggytalents.DoggyTalents2;
import doggytalents.common.addon.biomesoplenty.BiomesOPlentyAddon;
import doggytalents.common.addon.botania.BotaniaAddon;
import doggytalents.common.addon.itemphysic.ItemPhysicsAddon;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;

public class AddonManager {

    private static final List<Addon> ADDONS = Collections.unmodifiableList(Lists.newArrayList(
            new ItemPhysicsAddon(),
            new BiomesOPlentyAddon(),
            new BotaniaAddon()));
    private static final List<Addon> RUN = Lists.newArrayList(ADDONS);

    public static void init() {
        // inits the addon if it should be run and if it errors remove from the addons to run later
        doWork(ADDONS, Addon::shouldLoad, Addon::init, (addon, e) -> RUN.remove(addon));
    }

    public static void exec() {
        // execs the addons that didn't error
        doWork(RUN, Addon::shouldLoad, Addon::exec, (addon, e) -> {
            DoggyTalents2.LOGGER.warn("Failed to init {}", addon.getName());
            e.printStackTrace();
        });
    }

    /**
     * @param addons The list of addons
     * @param shouldRun Conditions for running for the particular addon
     * @param action The action to run for the particular addon
     * @param failedCallback The callback if a {@link RuntimeException} is thrown
     */
    private static void doWork(List<Addon> addons, Predicate<Addon> shouldRun, Consumer<Addon> action, BiConsumer<Addon, RuntimeException> failedCallback) {
        for (Addon addon : addons) {
            // If list is empty load everytime or only load if all of the mods are loaded
            if (shouldRun.test(addon)) {
                try {
                    action.accept(addon);
                } catch(RuntimeException e) {
                    failedCallback.accept(addon, e);
                }
            }
        }
    }
}
