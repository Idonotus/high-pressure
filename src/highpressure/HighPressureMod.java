package highpressure;

import arc.*;
import arc.util.*;
import highpressure.tests.PolygonTest;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import highpressure.content.HPBlocks;
import highpressure.content.HPSolarSystem;

public class HighPressureMod extends Mod{

    public HighPressureMod(){
        Log.info("Loaded HighPressureMod constructor.");
        new PolygonTest();
        //listen for game load event
        Events.on(ClientLoadEvent.class, e -> {
            //show dialog upon startup
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("frog");
                dialog.cont.add("behold").row();
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("high-pressure-frog")).pad(20f).row();
                dialog.cont.button("I see", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
    }

    @Override
    public void loadContent(){
        HPSolarSystem.load();
        HPBlocks.load();
        Log.info("Loading some example content.");
    }

}
