package org.pasqg.terraingen.renderer;

import org.pasqg.terraingen.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

public class TerrainRenderer {
    private static final String BLENDER = "/Applications/Blender3.2.app/Contents/MacOS/Blender";
    private static final String BLEND_FILE = "scripts/terrainViewer.blend";
    private static final String RENDER_SCRIPT = "scripts/terrain_render.py";

    public static void main(String[] args) throws IOException, InterruptedException {
        long seed = 3409129824829640289L;
        int width = 2048;
        int height = width;
        float scale = 3.0f;

        Landscaper landscaper = Landscaper.generateRandom(seed, width, height, scale, 0.33f)
                .smoothErosion();

        String eroded_path = "renders/heightmap_eroded_" + seed + ".tiff";
        ImageUtils.writeToFile(ImageUtils.fromFloatArray(landscaper.terrain(), width, height),
                eroded_path,
                "tiff");
        render(1. / (scale), eroded_path, "renders/output_eroded_" + seed + ".png");
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
