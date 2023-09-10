package highpressure.tests;

import arc.math.geom.Intersector;
import arc.struct.FloatSeq;
import arc.util.Log;

public class PolygonTest {
    public PolygonTest(){
        Log.info("==============Testing==============");
        FloatSeq rect1,rect2;
        rect1 = rect2 = new FloatSeq();
        rect1.add(0,0);
        rect1.add(0,1);
        rect1.add(1,1);
        rect1.add(1,0);
        if (Intersector.intersectPolygons(rect1.toArray(),rect2.toArray())) {
            Log.info("Basic collision success");
        } else {Log.info("Basic collision failed");}
        rect2 = new FloatSeq();
        rect2.add(-1,-1);
        rect2.add(-1,1);
        rect2.add(1,1);
        rect2.add(1,-1);
        if (Intersector.intersectPolygons(rect1.toArray(),rect2.toArray())) {
            Log.info("Contain collision success");
        } else {Log.info("Contain collision failed");}
        rect1 = new FloatSeq(); rect2 = new FloatSeq();
        rect1.add(0,-1);
        rect1.add(0,2);
        rect1.add(1,2);
        rect1.add(1,-1);
        rect2.add(-1,0);
        rect2.add(-1,1);
        rect2.add(2,1);
        rect2.add(2,0);
        if (Intersector.intersectPolygons(rect1.toArray(),rect2.toArray())) {
            Log.info("Overlap collision success");
        } else {Log.info("Overlap collision failed");}
        rect1 = new FloatSeq(); rect2 = new FloatSeq();
        rect1.add(0,0);
        rect1.add(0,1);
        rect1.add(1,1);
        rect1.add(1,0);
        rect2.add(0,0);
        rect2.add(1,1);
        rect2.add(2,1);
        rect2.add(1,0);
        if (Intersector.intersectPolygons(rect1.toArray(),rect2.toArray())) {
            Log.info("Quad collision success");
        } else {Log.info("Quad collision failed");}
    }
}
