package net.pixeils.bettersuperflat.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

public class BetterFlatChunkGen extends FlatChunkGenerator {

    private long seed;
    private BiomeSource biomeSource;

    public static final Codec<BetterFlatChunkGen> CODEC =
            RecordCodecBuilder.create(
                    (instance) ->
                            instance
                                    .group(
                                            BiomeSource.CODEC
                                                    .fieldOf("biome_source")
                                                    .forGetter(BetterFlatChunkGen::getBiomeSource),
                                            Codec.LONG
                                                    .fieldOf("seed")
                                                    .stable()
                                                    .forGetter(BetterFlatChunkGen::getSeed),
                                            FlatChunkGeneratorConfig.CODEC
                                                    .fieldOf("settings")
                                                    .forGetter(BetterFlatChunkGen::getConfig))
                                    .apply(instance, instance.stable(BetterFlatChunkGen::new)));

    public BetterFlatChunkGen(BiomeSource biomeSource, Long seed, FlatChunkGeneratorConfig flatChunkGeneratorConfig) {
        super(flatChunkGeneratorConfig);
        this.seed = seed;
        this.biomeSource = biomeSource;
    }

    public long getSeed() {
        return this.seed;
    }

    @Override
    protected Codec<? extends net.minecraft.world.gen.chunk.ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor structureAccessor, Chunk chunk) {
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                BlockPos pos = chunk.getPos().getStartPos();
                int x = pos.getX() + j;
                int z = pos.getZ() + k;
                // NW is always 0,0
                // 0,0 -1,-1
                //lines
                if(x >= 0 && z >= 0) {
                    if (Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((x<0?x:x+1)) % 128 < 2 || Math.abs((z<0?z:z+1)) % 128 < 2) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((x<0?x:x+1)) % 16 < 2 || Math.abs((z<0?z:z+1)) % 16 < 2) {
                        // full borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //borders
                if(x >= 0 && z < 0) {
                    if (Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //dots
                if(x < 0 && z >= 0) {
                    // almost works lol
                    if ((Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) && (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2)) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else if ((Math.abs((x<0?x:x+1)) % 128 < 2 || Math.abs((z<0?z:z+1)) % 128 < 2) && (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2)) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2) {
                        // full borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), false);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        chunk.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
                    }
                }
                //none
                if(x < 0 && z < 0) {
                    chunk.setBlockState(new BlockPos(j,0,k), Blocks.WHITE_STAINED_GLASS.getDefaultState(),false);
                }
            }
        }
    }
}
