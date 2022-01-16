package net.pixeils.bettersuperflat.gen;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

import java.util.Optional;

public class GenerationSettings {

  public static final String NAME = "quaconcmp";

  public static SimpleRegistry<DimensionOptions> getBetterSuperFlatDimensionOptions(
      Registry<DimensionType> dimensionTypeRegistry,
      Registry<Biome> biomeRegistry,
      Registry<ChunkGeneratorSettings> settingsRegistry,
      long seed) {
    SimpleRegistry<DimensionOptions> simpleRegistry =
        new SimpleRegistry<>(Registry.DIMENSION_KEY, Lifecycle.experimental());
    simpleRegistry.add(
        DimensionOptions.OVERWORLD,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.OVERWORLD_REGISTRY_KEY),
            createOverworldGenerator(biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    simpleRegistry.add(
        DimensionOptions.NETHER,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.THE_NETHER_REGISTRY_KEY),
            createNetherGenerator(biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    simpleRegistry.add(
        DimensionOptions.END,
        new DimensionOptions(
            () -> dimensionTypeRegistry.get(DimensionType.THE_END_REGISTRY_KEY),
            createEndGenerator(biomeRegistry, settingsRegistry, seed)),
        Lifecycle.stable());
    return simpleRegistry;
  }


  public static net.minecraft.world.gen.chunk.ChunkGenerator createOverworldGenerator(
      Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    System.out.println("before where i think it breaks");
    StructuresConfig conf = new StructuresConfig(Optional.ofNullable(StructuresConfig.DEFAULT_STRONGHOLD), StructuresConfig.DEFAULT_STRUCTURES);
    return new BetterFlatChunkGen(new FlatChunkGeneratorConfig(conf,biomeRegistry));
  }

  public static net.minecraft.world.gen.chunk.ChunkGenerator createNetherGenerator(
      Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    return new ChunkGenerator(
            new FixedBiomeSource(biomeRegistry.get(BiomeKeys.THE_VOID)),
        seed,
        () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.NETHER));
  }

  public static net.minecraft.world.gen.chunk.ChunkGenerator createEndGenerator(
      Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
    return new ChunkGenerator(
            new FixedBiomeSource(biomeRegistry.get(BiomeKeys.THE_VOID)),
        seed,
        () -> settingsRegistry.getOrThrow(ChunkGeneratorSettings.END));
  }
}
