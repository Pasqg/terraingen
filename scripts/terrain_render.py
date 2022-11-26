import bpy
import os
import time
import sys

argv = sys.argv
print(argv)

heightMapPath = argv[argv.index("-heightMap") + 1] if "-heightMap" in argv else None
outputPath = argv[argv.index("-output") + 1] if "-output" in argv else "terrain.png"
scale = float(argv[argv.index("-scale") + 1]) if "-scale" in argv else 1.0

bpy.data.materials["Material.001"].node_tree.nodes["Displacement"].inputs[2].default_value = scale

if heightMapPath is not None:
    heightMap = bpy.data.images.load(heightMapPath)
    heightMapNode = bpy.data.materials["Material.001"].node_tree.nodes.new('ShaderNodeTexImage')
    heightMapNode.image = heightMap
    links = bpy.data.materials["Material.001"].node_tree.links
    links.new(heightMapNode.outputs["Color"], bpy.data.materials["Material.001"].node_tree.nodes["Map Range"].inputs[0])

rndr = bpy.context.scene.render
rndr.pixel_aspect_x = 1.0
rndr.pixel_aspect_y = 1.0
rndr.resolution_percentage = 100
rndr.resolution_x = 1920
rndr.resolution_y = 1080
bpy.data.scenes["Scene"].cycles.samples = 4
rndr.filepath = os.path.join('', outputPath)
bpy.ops.render.render(write_still=True)