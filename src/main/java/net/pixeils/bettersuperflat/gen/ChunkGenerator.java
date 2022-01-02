package net.pixeils.bettersuperflat.gen;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

public class ChunkGenerator extends NoiseChunkGenerator {

    private final long seed;

    public static final Codec<ChunkGenerator> CODEC =
            RecordCodecBuilder.create(
                    (instance) ->
                            instance
                                    .group(
                                            BiomeSource.CODEC
                                                    .fieldOf("biome_source")
                                                    .forGetter(ChunkGenerator::getBiomeSource),
                                            Codec.LONG
                                                    .fieldOf("seed")
                                                    .stable()
                                                    .forGetter(ChunkGenerator::getSeed),
                                            ChunkGeneratorSettings.REGISTRY_CODEC
                                                    .fieldOf("settings")
                                                    .forGetter(ChunkGenerator::getSettings))
                                    .apply(instance, instance.stable(ChunkGenerator::new)));

    public ChunkGenerator(
            BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {

        super(biomeSource, seed, settings);
        this.seed = seed;
    }


    public long getSeed() {
        return this.seed;
    }

    public Supplier<ChunkGeneratorSettings> getSettings() {
        return this.settings;
    }

    @Override
    protected Codec<? extends net.minecraft.world.gen.chunk.ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public net.minecraft.world.gen.chunk.ChunkGenerator withSeed(long seed) {
        return new ChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        Arrays.fill(chunk.getSectionArray(), WorldChunk.EMPTY_SECTION);

        BlockPos pos =
                new BlockPos(
                        region.getCenterChunkX(),
                        region.getHeight() - 256,
                        region.getCenterChunkZ());

            if (chunk.getPos().getEndX() >= 0) {
                if (chunk.getPos().getEndZ() >= 0) {
                    // ++
                    generateChunkFloorSE(region, chunk, false);
                } else {
                    // +-
                    generateChunkFloorNE(region, chunk);
                }
            } else {
                if (chunk.getPos().getEndZ() >= 0) {
                    // -+
                    generateChunkFloorSW(region, chunk, false);
                } else {
                    // --
                    generateChunkFloorNW(region, chunk);
                }
            }
        }

    // +,+
    // this algorithm for generation *should* work in all quadrents even through its only used in one here
    // generates lines
    protected static void generateChunkFloorSE(WorldAccess world, Chunk chunk, boolean isNether) {
        int y=0;
        for (int x = chunk.getPos().getStartX(); x <= chunk.getPos().getEndX(); x++) {
                for (int z = chunk.getPos().getStartZ(); z <= chunk.getPos().getEndZ(); z++) {
                    if (Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) {
                        // region borders
                        BlockPos blockPos =
                                new BlockPos(x, y, z);
                            world.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), 2);
                    } else if (Math.abs((x<0?x:x+1)) % (isNether?64:128) < 2 || Math.abs((z<0?z:z+1)) % (isNether?64:128) < 2) {
                        // 8x grid
                        BlockPos blockPos =
                                new BlockPos(x, y, z);
                        world.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), 2);
                    } else if (Math.abs((x<0?x:x+1)) % 16 < 2 || Math.abs((z<0?z:z+1)) % 16 < 2) {
                        // full borders
                        BlockPos blockPos =
                                new BlockPos(x, y, z);
                        world.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), 2);
                    } else {
                        BlockPos blockPos =
                                new BlockPos(x, y, z);
                        world.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), 2);
                    }
                }
            }
        }

    // -,+
    // generates dots
    protected static void generateChunkFloorSW(WorldAccess world, Chunk chunk, boolean isNether) {
        int y=0;
        for (int x = chunk.getPos().getStartX(); x <= chunk.getPos().getEndX(); x++) {
            for (int z = chunk.getPos().getStartZ(); z <= chunk.getPos().getEndZ(); z++) {
                if ((Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) && (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2))  {
                    // region borders
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), 2);
                } else if ((Math.abs((x<0?x:x+1)) % (isNether?64:128) < 2 || Math.abs((z<0?z:z+1)) % (isNether?64:128) < 2) && (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2)) {
                    // 8x grid
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.RED_STAINED_GLASS.getDefaultState(), 2);
                } else if (Math.abs((x<0?x:x+1)) % 16 < 2 && Math.abs((z<0?z:z+1)) % 16 < 2) {
                    // full borders
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.LIGHT_GRAY_STAINED_GLASS.getDefaultState(), 2);
                } else {
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), 2);
                }
            }
        }
    }

    // +,-
    // generates region borders
    protected static void generateChunkFloorNE(WorldAccess world, Chunk chunk) {
        int y=0;
        for (int x = chunk.getPos().getStartX(); x <= chunk.getPos().getEndX(); x++) {
            for (int z = chunk.getPos().getStartZ(); z <= chunk.getPos().getEndZ(); z++) {
                if (Math.abs((x<0?x:x+1)) % 512 < 2 || Math.abs((z<0?z:z+1)) % 512 < 2) {
                    // region borders
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.BLUE_STAINED_GLASS.getDefaultState(), 2);
                } else {
                    BlockPos blockPos =
                            new BlockPos(x, y, z);
                    world.setBlockState(blockPos, Blocks.WHITE_STAINED_GLASS.getDefaultState(), 2);
                }
            }
        }
    }

    // -,-
    // generates white floor lol
    protected static void generateChunkFloorNW(WorldAccess world, Chunk chunk) {
        for (int x = chunk.getPos().getStartX(); x <= chunk.getPos().getEndX(); x++) {
            for (int z = chunk.getPos().getStartZ(); z <= chunk.getPos().getEndZ(); z++) {
                world.setBlockState(new BlockPos(x, 0, z), Blocks.WHITE_STAINED_GLASS.getDefaultState(), 2);
            }
        }
    }

    /* @Override
    public CompletableFuture<Chunk> populateNoise(
            Executor executor, StructureAccessor accessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    } */

    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }


    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        ChunkPos chunkPos = new ChunkPos(region.getCenterChunkX(),region.getCenterChunkZ());
        BlockPos pos = new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ());
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        BlockBox box = new BlockBox(startX, 0, startZ, startX + 15, region.getHeight(), startZ + 15);
    }
}
