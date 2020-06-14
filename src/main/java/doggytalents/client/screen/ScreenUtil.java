package doggytalents.client.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;

public class ScreenUtil {

    public static List<String> splitInto(String text, int maxLength, FontRenderer font) {
        List<String> list = new ArrayList<>();

        StringBuilder temp = new StringBuilder();
        String[] split = text.split(" ");

        for(int i = 0; i < split.length; ++i) {
            String str = split[i];
            int length = font.getStringWidth(temp + str);

            if(length > maxLength) {
                list.add(temp.toString());
                temp = new StringBuilder();
            }

            temp.append(str);
            temp.append(" ");

            if(i == split.length - 1) {
                list.add(temp.toString());
            }
        }

        return list;
    }


}
