package doggytalents.common.data;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * Defaults are to show toast, announce to chat and not to be hidden
 */
public class DisplayInfoBuilder {

    private Component title;
    private Component description;
    private ItemStack icon;
    private ResourceLocation background;
    private FrameType frame;
    private boolean showToast = true;
    private boolean announceToChat = true;
    private boolean hidden = false;

    public DisplayInfoBuilder translate(ResourceLocation key) {
        return this.translate(key.getNamespace() + "." + key.getPath());
    }

    public DisplayInfoBuilder translate(String key) {
        this.title(Component.translatable("advancements."+key+".title"));
        this.description(Component.translatable("advancements."+key+".description"));
        return this;
    }

    public DisplayInfoBuilder title(Component titleIn) {
        this.title = titleIn;
        return this;
    }

    public DisplayInfoBuilder description(Component descriptionIn) {
        this.description = descriptionIn;
        return this;
    }

    public DisplayInfoBuilder icon(Supplier<? extends ItemLike> stackIn) {
        return this.icon(stackIn.get());
    }

    public DisplayInfoBuilder icon(ItemLike provider) {
        return this.icon(new ItemStack(provider.asItem()));
    }

    public DisplayInfoBuilder icon(ItemStack stackIn) {
        this.icon = stackIn;
        return this;
    }

    public DisplayInfoBuilder background(String path) {
        return background(new ResourceLocation("textures/gui/advancements/backgrounds/" + path));
    }

    public DisplayInfoBuilder background(String modId, String path) {
        return background(new ResourceLocation(modId, "textures/gui/advancements/backgrounds/" + path));
    }

    public DisplayInfoBuilder background(ResourceLocation backgroundIn) {
        this.background = backgroundIn;
        return this;
    }

    public DisplayInfoBuilder frame(FrameType frameIn) {
        this.frame = frameIn;
        return this;
    }

    public DisplayInfoBuilder noToast() {
        this.showToast = false;
        return this;
    }

    public DisplayInfoBuilder noAnnouncement() {
        this.announceToChat = false;
        return this;
    }

    public DisplayInfoBuilder hide() {
        this.hidden = true;
        return this;
    }

    public DisplayInfo build() {
        return new DisplayInfo(icon, title, description, background, frame, showToast, announceToChat, hidden);
    }

    public static DisplayInfoBuilder create() {
        return new DisplayInfoBuilder();
    }
}
