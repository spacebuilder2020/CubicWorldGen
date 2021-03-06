/*
 *  This file is part of Cubic World Generation, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.populator;

import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.core.event.CCEventFactory;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaigaDecorator implements ICubicPopulator {

    @Override public void generate(World world, Random random, CubePos pos, Biome biome) {
        BiomeTaiga taiga = (BiomeTaiga) biome;
        if ((taiga.type == BiomeTaiga.Type.MEGA || taiga.type == BiomeTaiga.Type.MEGA_SPRUCE
                && CCEventFactory.decorate(world, random, pos, DecorateBiomeEvent.Decorate.EventType.ROCK))) {
            int count = random.nextInt(3);

            for (int i = 0; i < count; ++i) {
                int xOffset = random.nextInt(ICube.SIZE) + ICube.SIZE / 2;
                int zOffset = random.nextInt(ICube.SIZE) + ICube.SIZE / 2;
                BlockPos blockPos = ((ICubicWorld) world).getSurfaceForCube(pos, xOffset, zOffset, 0, ICubicWorld.SurfaceType.SOLID);
                if (blockPos != null) {
                    taiga.FOREST_ROCK_GENERATOR.generate((World) world, random, blockPos);
                }
            }
        }

        taiga.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

        if (CCEventFactory.decorate(world, random, pos, DecorateBiomeEvent.Decorate.EventType.FLOWERS)){
            for (int i = 0; i < 7; ++i) {
                if (random.nextInt(7) != 0) {
                    continue;
                }
                BlockPos blockPos = pos.randomPopulationPos(random);
                taiga.DOUBLE_PLANT_GENERATOR.generate(world, random, blockPos);
            }
        }
    }
}
