package org.pasqg.terraingen.renderer;

import org.pasqg.terraingen.noise.NoiseGenerator;
import org.pasqg.terraingen.utils.ImageUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class TerrainRenderer {
    private static final String BLENDER = "/Applications/Blender3.2.app/Contents/MacOS/Blender";
    private static final String BLEND_FILE = "scripts/terrainViewer.blend";
    private static final String RENDER_SCRIPT = "scripts/terrain_render.py";

    public static void main(String[] args) throws IOException, InterruptedException {
        long seed = 3409129824829640289L;
        int width = 2048;
        int height = 2048;
        int startHarmonic = 3;
        int harmonics = 32;

        Random random = new Random(seed);
        NoiseGenerator generator = new NoiseGenerator(random.nextInt());
        float[] base = generator.harmonicPerlin(width, height, startHarmonic, harmonics);

        String path = "renders/heightmap_" + seed + ".tiff";
        ImageUtils.writeToFile(ImageUtils.fromFloatArray(base, width, height), path, "tiff");
        TerrainRenderer.render(1. / (startHarmonic), path, "renders/output_" + seed + ".png");
    }

    public static void render(double aScale,
            String aNoiseImage,
            String aOutput) throws IOException, InterruptedException {
        ProcessBuilder renderProcess = new ProcessBuilder(List.of(
                BLENDER,
                "-b", BLEND_FILE,
                "-P", RENDER_SCRIPT,
                "--",
                "-scale", String.valueOf(aScale),
                "-heightMap", aNoiseImage,
                "-output", aOutput
        ));
        renderProcess.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        renderProcess.redirectError(ProcessBuilder.Redirect.INHERIT);
        renderProcess.start().waitFor();
    }
}
