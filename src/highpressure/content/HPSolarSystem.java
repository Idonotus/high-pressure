package highpressure.content;

import arc.graphics.Color;
import arc.math.geom.Vec3;
import highpressure.maps.planet.GeidonPlanetGenerator;
import mindustry.content.Planets;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;

public class HPSolarSystem {
    public static Planet
    fen,
    geild;

    public static void load(){
        fen = new Planet("fen", null, 20f){{

            bloom = true;
            accessible = false;
            visible =true;
            alwaysUnlocked=true;
            unlocked=true;
            meshLoader = () -> new SunMesh(
                    this, 3,
                    3, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("c41a24"),
                    Color.valueOf("903d21"),
                    Color.valueOf("b83904"),
                    Color.valueOf("db8b2a"),
                    Color.valueOf("a15a05"),
                    Color.valueOf("af4e29")
            );
            position=new Vec3(10f,80f,1f);
        }};
        geild = new Planet("geildon", fen, 3f, 3){{
                generator = new GeidonPlanetGenerator();
                meshLoader = () -> new HexMesh(this,4);
                cloudMeshLoader = () -> new MultiMesh(
                        new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Pal.spore).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                        new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Pal.spore, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
                );
                unlocked=true;
                launchCapacityMultiplier = 0.5f;
                sectorSeed = 2;
                tidalLock = true;
                allowWaves = true;
                allowWaveSimulation = true;
                allowSectorInvasion = true;
                allowLaunchSchematics = true;
                enemyCoreSpawnReplace = true;
                allowLaunchLoadout = true;
                //doesn't play well with configs
                prebuildBase = false;
                ruleSetter = r -> {
                    r.waveTeam = Team.crux;
                    r.placeRangeCheck = false;
                    r.showSpawns = false;
                };
                iconColor = Color.valueOf("7d4dff");
                atmosphereColor = Color.valueOf("3c1b8f");
                atmosphereRadIn = 0.02f;
                atmosphereRadOut = 0.5f;
                startSector = 15;
                alwaysUnlocked = true;
        }};
    }
}
