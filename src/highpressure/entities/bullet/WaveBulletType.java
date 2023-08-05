package highpressure.entities.bullet;

import arc.graphics.g2d.Fill;
import arc.util.Nullable;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LiquidBulletType;

public class WaveBulletType extends LiquidBulletType {
    public float width = 3;
    public float range = 10;
    public @Nullable BulletType splashBullet;

    public void drawWaveSeg(float posx, float posy, float viewpointx, float viewpointy, float angle, float width){
        //TODO THIS
    }

}
