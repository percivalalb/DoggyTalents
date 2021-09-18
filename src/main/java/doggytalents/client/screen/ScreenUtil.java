package doggytalents.client.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScreenUtil {

    public static List<ITextComponent> splitInto(String text, int maxLength, FontRenderer font) {
        List<ITextComponent> list = new ArrayList<>();

        StringBuilder temp = new StringBuilder();
        String[] split = text.split(" ");

        for (int i = 0; i < split.length; ++i) {
            String str = split[i];
            int length = font.width(temp + str);

            if (length > maxLength) {
                list.add(new StringTextComponent(temp.toString()));
                temp = new StringBuilder();
            }

            temp.append(str);
            temp.append(" ");

            if (i == split.length - 1) {
                list.add(new StringTextComponent(temp.toString()));
            }
        }

        return list;
    }


}
