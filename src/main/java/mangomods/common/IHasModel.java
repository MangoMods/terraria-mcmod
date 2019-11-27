package mangomods.common;

import java.util.ArrayList;

@FunctionalInterface
public interface IHasModel {
    ArrayList<IHasModel> HAS_MODELS = new ArrayList<>();

    void registerModels();
}
