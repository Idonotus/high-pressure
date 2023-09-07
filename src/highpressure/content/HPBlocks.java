package highpressure.content;

import arc.struct.EnumSet;
import highpressure.entities.bullet.WaveBulletType;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.HeatCrafter;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import highpressure.world.blocks.power.*;


import static mindustry.type.ItemStack.*;

public class HPBlocks {
    public static Block siliconConveyor,hardiceFloor,hardiceWall,laserenergizer,crucible,tide;

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
        crucible = new HeatCrafter("crucible"){{
            requirements(Category.crafting, with(Items.silicon, 40, Items.titanium, 60, Items.thorium, 10));
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawDefault());
            size = 3;
            hasPower = hasLiquids = true;

            consumePower(5f);
            consumeItem(Items.scrap,8);
            outputLiquid = new LiquidStack(Liquids.slag, 1f);
            craftTime = 15f;

        }};
        tide = new LiquidTurret("tide"){{
            requirements(Category.turret, with(Items.metaglass, 100, Items.lead, 400, Items.titanium, 250, Items.thorium, 100));
            ammo(
                    Liquids.water, new WaveBulletType(Liquids.water){{
                        lifetime = 100f;
                        speed = 4f;
                        knockback = 1.7f;
                        drag = 0.001f;
                        ammoMultiplier = 1f;
                        statusDuration = 60f * 4f;
                        damage = 0.2f;
                        layer = Layer.bullet - 5f;
                        pierceCap = 25;
                    }},
                    Liquids.slag,  new WaveBulletType(Liquids.slag){{
                        lifetime = 100f;
                        speed = 4f;
                        knockback = 10.3f;
                        pierceCap = 25;
                        damage = 4.75f;
                        drag = 0.001f;
                        ammoMultiplier = 1f;
                        statusDuration = 60f * 4f;
                    }},
                    Liquids.cryofluid, new WaveBulletType(Liquids.cryofluid){{
                        lifetime = 100f;
                        speed = 4f;
                        knockback = 1.3f;
                        pierceCap = 25;
                        drag = 0.001f;
                        ammoMultiplier = 0.5f;
                        statusDuration = 60f * 4f;
                        damage = 0.2f;
                    }},
                    Liquids.oil, new WaveBulletType(Liquids.oil){{
                        lifetime = 100f;
                        speed = 4f;
                        knockback = 1.3f;
                        pierceCap = 25;
                        drag = 0.001f;
                        ammoMultiplier = 1f;
                        statusDuration = 60f * 4f;
                        damage = 0.2f;
                        layer = Layer.bullet - 5f;
                    }}
            );
            consumeAmmoOnce = false;
            loopSound = Sounds.none;
            size = 2;
            reload = 120f;
            shoot.shots = 1;
            velocityRnd = 0.5f;
            inaccuracy = 0f;
            recoil = 1.5f;
            shootCone = 45f;
            liquidCapacity = 500f;
            shootEffect = Fx.shootLiquid;
            range = 190f;
            scaledHealth = 250;
            flags = EnumSet.of(BlockFlag.turret, BlockFlag.extinguisher);
        }};
    }
}
