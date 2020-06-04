package doggytalents.common.data;

import java.util.function.Supplier;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Defaults are to show toast, don't annonnce to chat and not to hide
 */
public class DisplayInfoBuilder {

    private ITextComponent title;
    private ITextComponent description;
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
        this.title(new TranslationTextComponent("advancements."+key+".title"));
        this.description(new TranslationTextComponent("advancements."+key+".description"));
        return this;
    }

    public DisplayInfoBuilder title(ITextComponent titleIn) {
        this.title = titleIn;
        return this;
    }

    public DisplayInfoBuilder description(ITextComponent descriptionIn) {
        this.description = descriptionIn;
        return this;
    }

    public DisplayInfoBuilder icon(Supplier<? extends IItemProvider> stackIn) {
        return this.icon(stackIn.get());
    }

    public DisplayInfoBuilder icon(IItemProvider provider) {
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
