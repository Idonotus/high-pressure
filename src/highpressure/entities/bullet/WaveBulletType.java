package highpressure.entities.bullet;

import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.type.Liquid;

public class WaveBulletType extends BulletType {
    public float width = 5f;
    public Liquid liquid;
    public float arcAngle;
    //Not recommended over 180 deg arcAngle
    public float arcMaxRange;
    public @Nullable BulletType splashBullet;

    public WaveBulletType(@Nullable Liquid liquid){
        if(liquid != null) {
            this.liquid = liquid;
            this.status = liquid.effect;
            hitColor = liquid.color;
            lightColor = liquid.lightColor;
            lightOpacity = liquid.lightColor.a;
        }
        arcMaxRange = -1f;
        arcAngle = 45f;
        ammoMultiplier = 1f;
        lifetime = 34f;
        statusDuration = 60f * 4f;
        despawnEffect = Fx.none;
        hitEffect = Fx.hitLiquid;
        smokeEffect = Fx.none;
        shootEffect = Fx.none;
        drag = 0.001f;
        knockback = 0.55f;
        displayAmmoMultiplier = false;
        fragBullets=1;
    }

    @Override
    public void init() {
        fragBullet = splashBullet;
        super.init();
    }

    public void drawWaveSeg(float posx, float posy, float viewpointx, float viewpointy, float angle , float width){
        float radius = (float) Math.sqrt(Math.pow(posx-viewpointx,2)+Math.pow(posy-viewpointy,2));
        float rotation = Angles.angle(viewpointx,viewpointy,posx,posy);
        drawWaveSeg(radius,viewpointx,viewpointy,angle,rotation,width,50);
    }
    public void drawWaveSeg(float radius, float posx, float posy, float angle, float rotation , float width, int points){
        FloatSeq poly = new FloatSeq();
        for (int i=0; i<=points/2; i++) {
            float a = ((float)i / (float)points)*angle*2-angle/2+rotation;
            float x1 = Angles.trnsx(a, radius+width/2);
            float y1 = Angles.trnsy(a, radius+width/2);
            poly.add(posx+x1,posy+y1);
        }
        for (int i=points/2; i>=0; i--) {
            float a =  ((float)i / (float)points)*angle*2-angle/2+rotation;
            float x1 = Angles.trnsx(a, radius-width/2);
            float y1 = Angles.trnsy(a, radius-width/2);
            poly.add(posx+x1,posy+y1);
        }
        Fill.poly(poly.items,poly.size);
    }

    @Override
    public void update(Bullet b){
        super.update(b);
        float ox, oy;
        float range = (float) Math.sqrt(Math.pow(b.originX,2)+Math.pow(b.originY,2));
        if ( range > arcMaxRange && arcMaxRange > 0) {
            ox = b.x-Angles.trnsx(b.rotation()-180, b.originX);
            oy = b.y-Angles.trnsy(b.rotation()-180, b.originX);
            range = arcMaxRange;}
        else {ox = b.originX; oy = b.originY;}
        float maxCollideRange = range + width/2;
        Seq<Bullet> bcollide = new Seq<>();
        Groups.bullet.intersect(ox,oy, maxCollideRange, maxCollideRange, bullet -> {
            if(bullet.team == b.team){return;}
            if (!bullet.type.hittable){return;}
            //TODO: ACTUAL COLLISION Check
            bcollide.add(bullet);
        });
        //TODO;
        // Block collision
        // Fire collision
        // collision handling

        if(liquid.canExtinguish()){
            //Fire
        }
    }

    @Override
    public void despawned(Bullet b){
        super.despawned(b);

        //don't create liquids when the projectile despawns
        if(!liquid.willBoil()){
            hitEffect.at(b.x, b.y, b.rotation(), liquid.color);
        }
    }
    private boolean collidePoint(float ox,float oy, float radius,float tx, float ty, float rotation){
        float a = Angles.angle(ox,oy,tx,ty);
        if (!Angles.near(a,rotation,arcAngle)) {return false;}
        float dist = (float) Math.sqrt(Math.pow(tx-ox,2)+Math.pow(ty-oy,2));
        if (dist>radius+width/2) {return false;}
        if (radius-width/2>dist) {return false;}
        return true;
    }
    @Override
    public void draw(Bullet b){

    }

}
