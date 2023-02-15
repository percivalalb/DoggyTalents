package doggytalents.client.screen.DogNewInfoScreen.store.slice;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import doggytalents.client.screen.DogNewInfoScreen.store.UIAction;
import net.minecraft.client.resources.language.I18n;

public class TalentListSlice implements AbstractSlice {

    @Override
    public Object getInitalState() {
        // TODO Auto-generated method stub
        return new TalentListData(
            DoggyTalentsAPI.TALENTS.get()
                .getValues()
                .stream()
                .sorted(Comparator.comparing((t) -> I18n.get(t.getTranslationKey())))
                .collect(Collectors.toList())
        );
    }

    @Override
    public Object reducer(Object oldData, UIAction action) {
        return oldData;
    }

    public static class TalentListData {
        public final List<Talent> talents;

        public TalentListData (List<Talent> talents) {
            this.talents = talents;
        }
        
    }
    
}
