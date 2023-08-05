package highpressure.entities.bullet;

import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.struct.FloatSeq;
import arc.util.Nullable;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LiquidBulletType;

public class WaveBulletType extends LiquidBulletType {
    public float width = 3;
    public float range = 10;
    public @Nullable BulletType splashBullet;
    public void drawWaveSeg(float posx, float posy, float viewpointx, float viewpointy, float angle, float rotation , float width, int points){
        float radius = (float) Math.sqrt(Math.pow(posx-viewpointx,2)+Math.pow(posy-viewpointy,2));
        drawWaveSeg(radius,viewpointx,viewpointy,angle,rotation,width,points);
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

}
