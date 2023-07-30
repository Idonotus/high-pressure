package highpressure.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.Block;
import mindustry.world.meta.Attribute;
import highpressure.world.blocks.power.*;
import static mindustry.type.ItemStack.*;

public class HPBlocks {
    public static Block siliconConveyor,hardiceFloor,hardiceWall,laserenergizer;

    public static void load(){
        siliconConveyor = new Conveyor("silicon-conveyor"){{
            requirements(Category.distribution, with(Items.silicon, 10, Items.graphite, 4));
            speed = 0.055f;
            size = 1;
            displayedSpeed = 8f;
            health = 60;
        }};
        hardiceFloor = new Floor("hard-ice"){{
            variants = 8;
            dragMultiplier = 0.1f;
            albedo = 0.3f;
            speedMultiplier = 0.8f;
            attributes.set(Attribute.water,-0.4f);
        }};
        hardiceWall = new StaticWall("hard-ice-wall"){{
           variants = 4;
        }};
        laserenergizer = new LaserEnergizer("laser-energizer"){{
            requirements(Category.power,with(Items.surgeAlloy,90));
            size = 2;
            powerUse = 1.5f;
        }};
    }
}
