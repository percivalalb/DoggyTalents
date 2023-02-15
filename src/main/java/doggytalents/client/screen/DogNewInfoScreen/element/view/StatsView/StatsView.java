package doggytalents.client.screen.DogNewInfoScreen.element.view.StatsView;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.element.AbstractElement;
import doggytalents.client.screen.DogNewInfoScreen.element.DivElement;
import doggytalents.client.screen.DogNewInfoScreen.element.ElementPosition.PosType;
import doggytalents.client.screen.DogNewInfoScreen.element.view.StatsView.view.StatsGeneralView;
import doggytalents.client.screen.DogNewInfoScreen.element.view.StatsView.view.MobKillsView;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StatsViewPanelSlice;
import doggytalents.client.screen.DogNewInfoScreen.store.slice.StatsViewPanelSlice.StatsViewPanelTab;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.stats.StatsTracker;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.StatsSyncData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.network.PacketDistributor;

public class StatsView extends AbstractElement {

    DogEntity dog;
    Font font;
    

    public StatsView(AbstractElement parent, Screen screen, DogEntity dog) {
        super(parent, screen);
        this.dog = dog;
        this.font = Minecraft.getInstance().font;
    } 

    @Override
    public AbstractElement init() {

        var tab = Store.get(getScreen())
            .getStateOrDefault(StatsViewPanelSlice.class, 
                StatsViewPanelTab.class, StatsViewPanelTab.GENERAL);

        int sizeX = this.getSizeX();
        int sizeY = this.getSizeY();

        int mX = sizeX/2;
        int mY = sizeY/2;

        int styleViewBoxSizeX = sizeX > 507 ? 448 : sizeX;
        int styleViewBoxSizeY = sizeY > 337 ? 320 : sizeY;
        
        var styleViewBoxDiv = new DivElement(this, getScreen())
            .setPosition(PosType.ABSOLUTE, mX - styleViewBoxSizeX/2, 
            mY - styleViewBoxSizeY/2 + (sizeY > 337 ? 10 : 0)) //+10 if detached to center it.
            .setSize(styleViewBoxSizeX, styleViewBoxSizeY);
            //.setBackgroundColor(0xffff05de);
        this.addChildren(styleViewBoxDiv);

        var styleListDiv = new StatsViewListPanel(styleViewBoxDiv, getScreen())
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(120, 1f)
            .setBackgroundColor(0x87363636)
            .init();

        styleViewBoxDiv.addChildren(styleListDiv);

        AbstractElement rightView;
        switch (tab) {
            case MOB_KILLS:
                rightView = new MobKillsView(styleViewBoxDiv, getScreen(), dog, dog.getStatTracker(), font);
                break;
            default:
                rightView = new StatsGeneralView(styleViewBoxDiv, getScreen(), dog.getStatTracker(), font);
                break;
        }
        
        rightView
            .setPosition(PosType.RELATIVE, 0, 0)
            .setSize(styleViewBoxDiv.getSizeX() - 120, 1f)
            .setBackgroundColor(0x57595858)
            .init();
        styleViewBoxDiv.addChildren(rightView);
        
        // var InventoryAccessoryList = 
        //     new InventoryAccessoryListElement(
        //         styleViewBoxDiv, getScreen(),
        //         Minecraft.getInstance().player, dog
        //     );
        // InventoryAccessoryList
        //     .setPosition(PosType.ABSOLUTE, 0, 0)
        //     .setSize(100, 40)
        //     .setBackgroundColor(0xffff05de)
        //     .init();

        // styleViewBoxDiv.addChildren(InventoryAccessoryList);

        

        return this;
    }

    @Override
    public void renderElement(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // var font = getScreen().getMinecraft().font;
        // font.draw(stack, "stats", this.getRealX()+3, this.getRealY() + 40, 0xffffffff);
        
    }

    public static void requestStatsSync(DogEntity dog) {
        PacketHandler.send(PacketDistributor.SERVER.noArg(), 
        new StatsSyncData.Request(dog.getId()));
    }
    
}
