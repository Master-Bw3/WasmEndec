package maple.trickster_endec.fragment;

public record DimensionFragment(RegistryKey<World> world) implements Fragment {
    public static StructEndec<DimensionFragment> ENDEC = StructEndecBuilder.of(
            CodecUtils.toEndec(RegistryKey.createCodec(RegistryKeys.WORLD)).fieldOf("world", DimensionFragment::world),
            DimensionFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.DIMENSION;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Text asText() {
        return Text.literal(WordUtils.capitalize(world.getValue().getPath().replace('_', ' ')));
    }

    @Override
    public boolean asBoolean() {
        return true;
    }

    @Override
    public int getWeight() {
        return 16;
    }

    public static DimensionFragment of(World world) {
        return new DimensionFragment(world.getRegistryKey());
    }
}