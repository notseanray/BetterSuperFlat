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
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import java.util.function.Supplier;

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
                                                    .forGetter(BetterFlatChunkGen::getSettings))
                                    .apply(instance, instance.stable(BetterFlatChunkGen::new)));

    public BetterFlatChunkGen(BiomeSource biomeSource, Long seed, FlatChunkGeneratorConfig flatChunkGeneratorConfig) {
        super(flatChunkGeneratorConfig);
        this.seed = seed;
        this.biomeSource = biomeSource;
    }

    public long getSeed() {
        return this.seed;
    }

    public FlatChunkGeneratorConfig getSettings() {
        return this.getConfig();
    }

    @Override
    protected Codec<? extends net.minecraft.world.gen.chunk.ChunkGenerator> getCodec() {
        return CODEC;
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
                    int j2 = j + 16 * lvt3.getPos().x;
                    int k2 = k + 16 * lvt3.getPos().z;
                    if (Math.abs((j<0?j2:j2+1)) % 512 < 2 && Math.abs((k<0?k2:k2+1)) % 512 < 2) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((j<0?j2:j2+1)) % (128) < 2 && Math.abs((k<0?k2:k2+1)) % (128) < 2) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(j, 0, k);
                        lvt3.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), false);
                    } else if (Math.abs((j<0?j2:j2+1)) % 16 < 2 && Math.abs((k<0?k2:k2+1)) % 16 < 2) {
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
