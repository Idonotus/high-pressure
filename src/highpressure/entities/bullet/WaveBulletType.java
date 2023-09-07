package highpressure.entities.bullet;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.geom.*;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Nullable;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.*;
import mindustry.type.Liquid;

public class WaveBulletType extends BulletType {
    public float width;
    public Liquid liquid;
    public float arcAngle;
    //Not recommended over 180 deg arcAngle
    public float arcMaxRange;
    public float damageMu;
    public @Nullable BulletType reflectBullet = null;

    public WaveBulletType(@Nullable Liquid liquid){
        super(0.5f, 0);
        if(liquid != null) {
            this.liquid = liquid;
            this.status = liquid.effect;
            hitColor = liquid.color;
            lightColor = liquid.lightColor;
            lightOpacity = liquid.lightColor.a;
        }
        width = 20f;
        collides = false;
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
        damage=100;
        impact=true;
    }

    @Override
    public void init() {
        super.init();
    }

    public void drawWaveSeg(float posx, float posy, float viewpointx, float viewpointy, float angle , float width){
        float radius = (float) Math.sqrt(Math.pow(posx-viewpointx,2)+Math.pow(posy-viewpointy,2));
        float rotation = Angles.angle(viewpointx,viewpointy,posx,posy);
        drawWaveSeg(radius,viewpointx,viewpointy,angle,rotation,width,50);
    }
    public void drawWaveSeg(float radius, float posx, float posy, float angle, float rotation , float width, int points){
        FloatSeq poly = new FloatSeq();
        for (int i=0; i<=points; i++) {
            float a = ((float)i / (float)points)*angle-angle/2+rotation;
            float x1 = Angles.trnsx(a, radius+width/2);
            float y1 = Angles.trnsy(a, radius+width/2);
            poly.add(posx+x1,posy+y1);
        }
        float x1, y1;
        x1 = Angles.trnsx(rotation+angle/2, radius-width/2);
        y1 = Angles.trnsy(rotation+angle/2, radius-width/2);
        poly.add(posx+x1,posy+y1);
        x1 = Angles.trnsx(rotation-angle/2, radius-width/2);
        y1 = Angles.trnsy(rotation-angle/2, radius-width/2);
        poly.add(posx+x1,posy+y1);
        x1 = Angles.trnsx(rotation-angle/2, radius+width/2);
        y1 = Angles.trnsy(rotation-angle/2, radius+width/2);
        poly.add(posx+x1,posy+y1);
        Fill.poly(poly.items,poly.size);
    }

    @Override
    public void update(Bullet b){
        if (b.damage<=0){Log.info(b.damage);}
        super.update(b);

        float lastpivx, pivx; lastpivx = pivx = b.originX;
        float lastpivy, pivy; lastpivy = pivy = b.originY;
        float lastrange = new Vec2(b.lastX-b.originX,b.lastY-b.originY).len();
        float range = new Vec2(b.x-b.originX,b.y-b.originY).len();
        if (arcMaxRange > 0) {
            if (lastrange > arcMaxRange) {
                lastpivx = b.lastX - Angles.trnsx(b.rotation() - 180, arcMaxRange);
                lastpivy = b.lastY - Angles.trnsy(b.rotation() - 180, arcMaxRange);
            }
            if (range > arcMaxRange) {
                pivx = b.lastX - Angles.trnsx(b.rotation() - 180, arcMaxRange);
                pivy = b.lastY - Angles.trnsy(b.rotation() - 180, arcMaxRange);
            }

            range = Math.min(range, arcMaxRange) ;
            lastrange = Math.min(lastrange, arcMaxRange);
        }
        range += width / 2; lastrange -= width / 2;
        FloatSeq hitpoly = new FloatSeq();
        hitpoly.add(pivx+Angles.trnsx(b.rotation(),range),
                pivy+Angles.trnsy(b.rotation(),range));
        hitpoly.add(pivx+Angles.trnsx(b.rotation()+arcAngle/2,range),
                pivy+Angles.trnsy(b.rotation()+arcAngle/2,range));
        hitpoly.add(lastpivx+Angles.trnsx(b.rotation()+arcAngle/2,lastrange),
                lastpivy+Angles.trnsy(b.rotation()+arcAngle/2,lastrange));
        hitpoly.add(lastpivx+Angles.trnsx(b.rotation()-arcAngle/2,lastrange),
                lastpivy+Angles.trnsy(b.rotation()-arcAngle/2,lastrange));
        hitpoly.add(pivx+Angles.trnsx(b.rotation()-arcAngle/2,range),
                pivy+Angles.trnsy(b.rotation()-arcAngle/2,range));
        Polygon hitbox = new Polygon(hitpoly.toArray());
        Rect maxCollideRect = hitbox.getBoundingRectangle();
        //Seq<Bullet> bulletcollide = new Seq<>();
        //Seq<Building> buildcollide = new Seq<>();
        Seq<Unit> unitcollide = new Seq<>();
        //Groups.bullet.intersect(maxCollideRect.x,maxCollideRect.y,
        //        maxCollideRect.width, maxCollideRect.height, bullet -> {
        //    if (!bullet.type.hittable) {return;}
        //    Rect bhitrect = new Rect();
        //    FloatSeq bhitbox = new FloatSeq();
        //    bullet.hitbox(bhitrect);
        //    bhitbox.add(bhitrect.x,bhitrect.y);bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y);
        //    bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y+bhitrect.height);bhitbox.add(bhitrect.x,bhitrect.y+bhitrect.height);
        //    if (!Intersector.intersectPolygons(hitbox,new Polygon(bhitbox.toArray()),null)) {return;}
        //    bulletcollide.add(bullet);
        //});
        //Groups.build.intersect(maxCollideRect.x,maxCollideRect.y,
        //        maxCollideRect.width, maxCollideRect.height, building -> {
        //            Rect bhitrect = new Rect();
        //            FloatSeq bhitbox = new FloatSeq();
        //            building.hitbox(bhitrect);
        //            bhitbox.add(bhitrect.x,bhitrect.y);bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y);
        //            bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y+bhitrect.height);bhitbox.add(bhitrect.x,bhitrect.y+bhitrect.height);
        //            if (!Intersector.intersectPolygons(hitbox,new Polygon(bhitbox.toArray()),null)) {return;}
        //            buildcollide.add(building);
        //        });
        Groups.unit.intersect(maxCollideRect.x,maxCollideRect.y,
                maxCollideRect.width, maxCollideRect.height, unit -> {
                    if (unit.team == b.team) {return;}
                    if (!unit.hittable()) {return;}
                    Rect bhitrect = new Rect();
                    FloatSeq bhitbox = new FloatSeq();
                    unit.hitbox(bhitrect);
                    bhitbox.add(bhitrect.x,bhitrect.y);bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y);
                    bhitbox.add(bhitrect.x+bhitrect.width,bhitrect.y+bhitrect.height);bhitbox.add(bhitrect.x,bhitrect.y+bhitrect.height);
                    //TODO: This
                    // but srs idk how to fix do better NOW
                    if (!Intersector.overlapConvexPolygons(hitpoly.toArray(),bhitbox.toArray(),null)) {return;}
                    unitcollide.add(unit);
                });;
        //TODO; Tile collision

        for (Unit unit:unitcollide){
            //TODO: Fix infinte kb
            hitEntity(b,unit, unit.health());
        }

        if(liquid.canExtinguish()){
            //TODO: Fire collision
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
    @Override
    public void draw(Bullet b) {
        float pivx; pivx = b.originX;
        float pivy; pivy = b.originY;
        float range = new Vec2(b.x-b.originX,b.y-b.originY).len();
        if (arcMaxRange > 0) {
            if (range > arcMaxRange) {
                pivx = b.lastX - Angles.trnsx(b.rotation() - 180, arcMaxRange);
                pivy = b.lastY - Angles.trnsy(b.rotation() - 180, arcMaxRange);
            }
            range = Math.min(range, arcMaxRange);
        }
        Draw.color(liquid.color);
        drawWaveSeg(range,pivx,pivy,arcAngle,b.rotation(),width,50);
    }


}
