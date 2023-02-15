package doggytalents.client.screen.DogNewInfoScreen.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.client.screen.DogNewInfoScreen.DogNewInfoScreen;
import doggytalents.client.screen.DogNewInfoScreen.store.Store;
import doggytalents.common.entity.DogEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class DogCannotInteractWithScreen extends Screen {

    DogEntity dog;

    protected DogCannotInteractWithScreen(DogEntity dog) {
        super(Component.literal(""));
        this.dog = dog;
    }

    public static void open(DogEntity dog) {
        var mc = Minecraft.getInstance();
        var screen = new DogCannotInteractWithScreen(dog);
        mc.setScreen(screen);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float pTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, pTicks);
        int mX = this.width/2;
        int mY = this.height/2; 

        int pY = mY - 72;
        Component title;
        String help;
        // if (this.dog.isDefeated()) {
        //     title = Component.literal("Dog is Incapacitated!")
        //         .withStyle(
        //             Style.EMPTY
        //             .withBold(true)
        //             .withColor(ChatFormatting.RED)
        //         );
        //     help = "Please check back when he is recovered.";
        // } else {
            if (this.dog.canInteract(Minecraft.getInstance().player)) {
                DogNewInfoScreen.open(dog);
                return;
            };
            title = Component.literal("You do not own this dog!")
            .withStyle(
                Style.EMPTY
                .withBold(true)
                .withColor(ChatFormatting.RED)
            );
            help = "Please ensure that you have permission to interact with him.";
        //}
        var dog_title = "Dog: " + this.dog.getName().getString();
        var owner_title = "Owner: " 
        + this.dog.getOwnersName().orElse(Component.literal("")).getString(); 
        var escToReturn= "Esc to return.";
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        this.font.draw(stack, title, (mX/1.2f -font.width(title)/2 ), pY/1.2f, 0xffffffff);
        stack.popPose();
        pY += 40;
        this.font.draw(stack, help, mX - font.width(help)/2, pY, 0xffffffff);
        pY += 40;
        this.font.draw(stack, dog_title, mX - font.width(dog_title)/2, pY, 0xffffffff );
        pY += font.lineHeight + 3;
        this.font.draw(stack, owner_title, mX - font.width(owner_title)/2, pY, 0xffffffff );
        pY += 40;
        this.font.draw(stack, escToReturn, mX - font.width(escToReturn)/2, pY, 0xffffffff );

    }

    @Override
    public boolean isPauseScreen() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
