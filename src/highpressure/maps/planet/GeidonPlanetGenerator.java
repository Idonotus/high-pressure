package highpressure.maps.planet;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.Log;
import arc.util.noise.Simplex;
import highpressure.content.HPBlocks;
import mindustry.content.Blocks;
import mindustry.game.Rules;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.Tiles;

public class GeidonPlanetGenerator extends PlanetGenerator {

    Block[][] terrain = {
            {Blocks.cryofluid,Blocks.cryofluid,HPBlocks.hardice,Blocks.ice,Blocks.ice,Blocks.snow},
            {HPBlocks.hardice,HPBlocks.hardice,HPBlocks.hardice,Blocks.ice,Blocks.snow,Blocks.iceSnow},
            {Blocks.ice,Blocks.snow,Blocks.snow,Blocks.iceSnow,Blocks.iceSnow,Blocks.beryllicStone},
            {Blocks.beryllicStone,Blocks.beryllicStone,Blocks.beryllicStone,Blocks.darksand,Blocks.darksand,HPBlocks.coolmoss},
            {Blocks.beryllicStone,Blocks.beryllicStone,Blocks.beryllicStone,Blocks.darksand,Blocks.darksand,HPBlocks.coolmoss},
            {Blocks.beryllicStone,Blocks.beryllicStone,Blocks.sand,Blocks.sand,Blocks.sand,Blocks.redStone},
            {Blocks.sand,Blocks.sand,Blocks.sand,Blocks.redStone,Blocks.basalt,Blocks.redStone,Blocks.sand,Blocks.redStone,Blocks.slag},
            {Blocks.redStone,Blocks.redStone,Blocks.slag,Blocks.slag,Blocks.slag,Blocks.basalt}
    };
    Block getBlock(Vec3 position){
        Block[] arr = terrain[(int) Mathf.clamp(rawTemp(position)*(terrain.length),0,terrain.length-1)];
        float microtemp = Simplex.noise3d(seed,6d,0.4,arr.length-1,position.x,position.y,position.z)*(arr.length);
        return arr[(int) Mathf.clamp(microtemp,0,arr.length-1)];
    }

    float rawTemp(Vec3 position){
        return (-position.z+1)/2;
    }

    @Override
    public float getHeight(Vec3 position){
        return Simplex.noise3d(seed,6d,0.4,0.9,position.x,position.y,position.z)*0.9f;
    }

    @Override
    public Color getColor(Vec3 position){
        Block blockcolor = getBlock(position);
        blockcolor = blockcolor==null ? Blocks.beryllicStone : blockcolor;
        return blockcolor.mapColor;
    }

    @Override
    public void generateSector(Sector sector){
        super.generateSector(sector);
    }

    @Override
    public void addWeather(Sector sector, Rules rules){

    }

    @Override
    public void generate(Tiles tiles, Sector sec, int seed){
        this.tiles = tiles;
        this.sector = sec;
        this.rand.setSeed(sec.id + seed + baseSeed);

        tiles.fill();

        generate(tiles);
    }
}
