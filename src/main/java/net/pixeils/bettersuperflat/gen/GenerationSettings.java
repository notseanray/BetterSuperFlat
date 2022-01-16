package net.pixeils.bettersuperflat.gen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;

import net.minecraft.block.Blocks;
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
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Optional;
import java.util.function.Supplier;

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
    // this emulates the getDefaultConfig method
    StructuresConfig structConf = new StructuresConfig(Optional.ofNullable(StructuresConfig.DEFAULT_STRONGHOLD), Maps.newHashMap(ImmutableMap.of(StructureFeature.VILLAGE, StructuresConfig.DEFAULT_STRUCTURES.get(StructureFeature.VILLAGE))));
    FlatChunkGeneratorConfig chunkConf = new FlatChunkGeneratorConfig(structConf,biomeRegistry);
    chunkConf.setBiome(() -> biomeRegistry.get(BiomeKeys.THE_VOID));
    FlatChunkGeneratorLayer layer = new FlatChunkGeneratorLayer(1, Blocks.BLACK_STAINED_GLASS);
    chunkConf.getLayers().add(layer);
    return new BetterFlatChunkGen(chunkConf);
    //return new BetterFlatChunkGen(FlatChunkGeneratorConfig.getDefaultConfig(biomeRegistry));
  }

  /*

  public static FlatLevelGeneratorSettings getDefault(Registry<Biome> lvt0) {
      StructureSettings structuresettings = new StructureSettings(Optional.of(StructureSettings.DEFAULT_STRONGHOLD), Maps.newHashMap(ImmutableMap.of(StructureFeature.VILLAGE, StructureSettings.DEFAULTS.get(StructureFeature.VILLAGE))));
      FlatLevelGeneratorSettings flatlevelgeneratorsettings = new FlatLevelGeneratorSettings(structuresettings, lvt0);
      flatlevelgeneratorsettings.biome = () -> {
         return lvt0.getOrThrow(Biomes.PLAINS);
      };
      flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
      flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
      flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
      flatlevelgeneratorsettings.updateLayers();
      return flatlevelgeneratorsettings;
   }

   */

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
