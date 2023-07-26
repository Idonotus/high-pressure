package highpressure.world.blocks.power;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.core.Renderer;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.meta.Env;

import static mindustry.Vars.tilesize;

public class LaserEnergizer extends Block {
    public TextureRegion baseRegion;
    public  float laserScale;
    public TextureRegion laser;
    public TextureRegion laserEnd;
    public float laserRange;
    public float powerUse;
    public Color laserColor1 = Color.white;
    public Color laserColor2 = Pal.powerLight;
    public LaserEnergizer(String name) {
        super(name);
        configurable = true;
        update = true;
        hasPower = true;
        envEnabled |= Env.space;
    }

    @Override
    public void init(){
        super.init();
        updateClipRadius(laserRange);
        consumePowerCond(powerUse,(LaserEnergizerBuild entity) -> entity.target!=null);
    }

    public class LaserEnergizerBuild extends Building{
        protected Building target;
        public float rotation = 0;
        public float strength = 90;
        private boolean powering = false;
        @Override
        public void updateTile(){
            if(target!=null){if (!target.isValid()){removeTarget();}}
            if (efficiency > 0 && target != null){
                float angle = Angles.angle(x,y,target.x,target.y);

                if (Angles.angleDist(angle,rotation) < 20 && !powering){
                    power.links.add(target.pos());
                    powering=true;
                }
                rotation = Mathf.slerpDelta(rotation,angle,0.1f * efficiency * timeScale);
                //TODO LASERS!!!!!!!!!

            }
            else if (powering && target!=null) {
                disconnect();
            }
            else if (powering) {removeTarget();}
            strength = Mathf.lerpDelta(strength, powering ? 1f : 0f, 0.08f * Time.delta);
        }

        @Override
        public void remove(){
            removeTarget();
            super.remove();
        }

        public void disconnect(){
            if (target!=null){if (target.power!=null) {
                target.power.links.removeValue(pos());
                power.links.removeValue(target.pos());
            }}
            powering=false;
        }

        public void removeTarget(){
            disconnect();
            target = null;
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            //TODO config
            if (other == null) {return true;}
            /* double tab*/
            if (other == this) {removeTarget();deselect();}
            else if (other==target) {removeTarget();}
            else {
                removeTarget();
                target = other;
            }
            return false;
        }

        @Override
        public void draw(){
            Draw.rect(baseRegion,x,y);
            Draw.z(Layer.turret);
            Drawf.shadow(region, x - (size / 2f), y - (size / 2f), rotation - 90);
            Draw.rect(region, x, y, rotation - 90);
            if (target != null) {
                Draw.z(Layer.power);
                Draw.color(laserColor1, laserColor2, (1f - power.graph.getSatisfaction()) * 0.86f + Mathf.absin(3f, 0.1f));
                Draw.alpha(Renderer.laserOpacity);
                float angle1 = Angles.angle(x, y, target.x, target.y),
                        vx = Mathf.cosDeg(angle1), vy = Mathf.sinDeg(angle1),
                        len1 = size * tilesize / 2f - 1.5f, len2 = target.block.size * tilesize / 2f - 1.5f;

                Drawf.laser(laser, laserEnd, x * tilesize, y * tilesize, target.x - vx * len2, target.y - vy * len2, laserScale);
            }
            //TODO DRAW
        }
        @Override
        public void drawConfigure(){
            //TODO Draw config
            Drawf.circles(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));
            if (target!=null) {
                Drawf.square(target.x, target.y, target.block.size * tilesize / 2f + 1f, Time.time, Pal.place);
            }
        }
    }

    @Override
    public void load() {
        super.load();
        baseRegion = Core.atlas.find(this.name+"-base-"+this.size);
        laser = Core.atlas.find("laser");
        laserEnd = Core.atlas.find("laser-end");
        if (!baseRegion.found()){baseRegion = Core.atlas.find("high-pressure-block-"+this.size,"block-"+this.size);}
    }
}