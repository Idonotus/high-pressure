package highpressure.content;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawLiquidTile;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.Attribute;
import highpressure.world.blocks.power.*;
import static mindustry.type.ItemStack.*;

public class HPBlocks {
    public static Block siliconConveyor,hardiceFloor,hardiceWall,laserenergizer,crucible;

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
            length = 5f;
            size = 2;
            powerUse = 1.5f;
        }};
        crucible = new GenericCrafter("crucible"){{
            requirements(Category.production, with(Items.silicon, 40, Items.titanium, 60, Items.thorium, 10));
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawDefault());
            size = 3;
            hasPower = hasLiquids = true;

            consumePower(5f);
            consumeItem(Items.scrap,8);
            outputLiquid = new LiquidStack(Liquids.slag, 1f);
            craftTime = 15f;

        }};
    }
}
