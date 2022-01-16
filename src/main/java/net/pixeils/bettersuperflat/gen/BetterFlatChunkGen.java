package net.pixeils.bettersuperflat.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

public class BetterFlatChunkGen extends FlatChunkGenerator {

    public BetterFlatChunkGen(FlatChunkGeneratorConfig config) {
        super(config);
    }

    @Override
    public void populateNoise(WorldAccess lvt1, StructureAccessor lvt2, Chunk lvt3) {
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                ChunkPos pos = lvt3.getPos();
                //lines
                if(pos.x >= 0 && pos.z >= 0) {
                    // won't work in negative coords but atm im beyond caring because this is already a hacky solution
                    int j2 = j + 16 * lvt3.getPos().x;
                    int k2 = k + 16 * lvt3.getPos().z;
                    if (Math.abs((j<0?j2:j2+1)) % 512 < 2 || Math.abs((k<0?k2:k2+1)) % 512 < 2) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((j<0?j2:j2+1)) % (128) < 2 || Math.abs((k<0?k2:k2+1)) % (128) < 2) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((j<0?j2:j2+1)) % 16 < 2 || Math.abs((k<0?k2:k2+1)) % 16 < 2) {
                        // full borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //borders
                if(pos.x >= 0 && pos.z < 0) {
                    int j2 = j + 16 * lvt3.getPos().x;
                    int k2 = k - 16 * lvt3.getPos().z;
                    if (Math.abs(pos.x+1) % 32 == 0 && j == 15 || (Math.abs(pos.x) % 32 == 0) && j == 0 || Math.abs(pos.z+1) % 32 == 0 && k == 15 || (Math.abs(pos.z) % 32 == 0) && k == 0) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //dots
                if(pos.x < 0 && pos.z >= 0) {
                    // almost works lol
                    if ((Math.abs(pos.x+1) % 32 == 0 || Math.abs(pos.x) % 32 == 0 || Math.abs(pos.z+1) % 32 == 0 || Math.abs(pos.z) % 32 == 0) && (j == 15 || j == 0) && (k == 15 || k == 0)) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else if ((Math.abs(pos.x+1) % 8 == 0 || Math.abs(pos.x) % 8 == 0 || Math.abs(pos.z+1) % 8 == 0 || Math.abs(pos.z) % 8 == 0) && (j == 15 || j == 0) && (k == 15 || k == 0)) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), false);
                    } else if ((j == 15 || j == 0) && (k == 15 || k == 0)) {
                        // full borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //none
                if(pos.x < 0 && pos.z < 0) {
                    lvt3.setBlockState(new BlockPos(j,0,k), Blocks.WHITE_STAINED_GLASS.getDefaultState(),false);
                }
            }
        }
    }
}
