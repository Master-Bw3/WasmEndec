package maple.trickster_endec.fragment;

public record BlockTypeFragment(Block block) implements Fragment {
    public static final StructEndec<BlockTypeFragment> ENDEC = StructEndecBuilder.of(
            MinecraftEndecs.ofRegistry(Registries.BLOCK).fieldOf("block", BlockTypeFragment::block),
            BlockTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.BLOCK_TYPE;
    }

    @Override
    public Text asText() {
        return block.getName();
    }

    @Override
    public boolean asBoolean() {
        return true;
    }

    @Override
    public int getWeight() {
        return 16;
    }
}
