package highpressure.world.blocks.power;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.FloatSeq;
import arc.util.Time;
import mindustry.core.Renderer;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.meta.Env;

import static mindustry.Vars.tilesize;

public class LaserEnergizer extends Block {

    public TextureRegion baseRegion;
    public  float laserScale = 0.5f;
    public TextureRegion laser;
    public TextureRegion laserEnd;
    public float laserRange;
    public float powerUse;
    public Color laserColor1 = Color.white;
    public float length = 5f;
    public Color laserColor2 = Pal.powerLight;

    public TextureRegion[] icons(){return new TextureRegion[]{baseRegion,region};}
    public LaserEnergizer(String name) {
        super(name);
        configurable = true;
        update = true;
        hasPower = true;
        envEnabled |= Env.space;
        solid = true;
        outlineIcon = true;
    }

    @Override
    public void init(){
        super.init();
        updateClipRadius(laserRange);
        consumePowerCond(powerUse,(LaserEnergizerBuild entity) -> entity.target!=null);
    }

    public void drawWaveSeg(float radius, float posx, float posy, float angle, float rotation , float width, int points){
        //TODO THIS
        FloatSeq poly = new FloatSeq();
        //float angleto = Angles.angle(viewpointx,viewpointy,posx,posy);

        for (int i=0; i<=points/2; i++) {
            float a = (float) (i / points)*angle-angle/2+rotation;
            float x1 = Angles.trnsx(a, radius+width/2);
            float y1 = Angles.trnsy(a, radius+width/2);
            poly.add(posx+x1,posy+y1);
        }
        for (int i=0; i<=points/2; i++) {
            float a = (float) (i / points)*angle-angle/2+rotation;
            float x1 = Angles.trnsx(a, radius-width/2);
            float y1 = Angles.trnsy(a, radius-width/2);
            poly.add(posx+x1,posy+y1);
        }
        float x1 = Angles.trnsx(-angle/2+rotation, radius+width/2);
        float y1 = Angles.trnsy(-angle/2+rotation, radius+width/2);
        poly.add(posx+x1,posy+y1);
        Fill.poly(poly.items,poly.size);
    }


    public class LaserEnergizerBuild extends Building{
        protected Building target;
        public float rotation = 90;
        public float strength = 90;
        private boolean powering = false;
        @Override
        public void updateTile(){
            super.updateTile();
            if(target!=null){if (!target.isValid()){removeTarget();}}
            if (efficiency > 0 && target != null){
                float angle = Angles.angle(x,y,target.x,target.y);

                if (Angles.angleDist(angle,rotation) < 20 && !powering){
                    power.links.add(target.pos());
                    if (target.block.hasPower) {
                        target.power.links.add(this.pos());
                    }
                    powering=true;
                }
                updatePowerGraph();
                rotation = Mathf.slerpDelta(rotation,angle,0.1f * efficiency * timeScale);
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
            if (target!=null){
                power.links.removeValue(target.pos());
                if (target.block.hasPower) {
                    target.power.links.removeValue(this.pos());
                }
                PowerGraph og = new PowerGraph();
                og.reflow(this);
                PowerGraph newpower = new PowerGraph();
                newpower.reflow(this);
            }
            powering=false;
        }

        public void removeTarget(){
            disconnect();
            target = null;
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
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
            drawWaveSeg(5,x,y,45f,rotation,2,50);
            Draw.rect(baseRegion,x,y);
            Draw.z(Layer.turret);
            Drawf.shadow(region, x - (size / 2f), y - (size / 2f), rotation - 90);
            Draw.rect(region, x, y, rotation - 90);
            if (target != null) {
                Draw.z(Layer.power);
                Draw.color(laserColor1, laserColor2, (1f - power.graph.getSatisfaction()) * 0.86f + Mathf.absin(3f, 0.1f));
                Draw.alpha(Renderer.laserOpacity);
                float vx=x+Angles.trnsx(rotation,length),
                        vy=y+Angles.trnsy(rotation,length);

                Drawf.laser(laser, laserEnd, vx, vy, target.x, target.y, laserScale*strength);
            }
        }
        @Override
        public void drawConfigure(){
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