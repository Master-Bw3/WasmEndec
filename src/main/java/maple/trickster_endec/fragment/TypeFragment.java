package maple.trickster_endec.fragment;

public record TypeFragment(FragmentType<?> typeType) implements Fragment {
    public static final StructEndec<TypeFragment> ENDEC = EndecTomfoolery.lazy(() -> StructEndecBuilder.of(
            MinecraftEndecs.ofRegistry(FragmentType.REGISTRY).fieldOf("of_type", TypeFragment::typeType),
            TypeFragment::new
    ));

    @Override
    public FragmentType<?> type() {
        return FragmentType.TYPE;
    }

    @Override
    public Text asText() {
        return typeType.getName();
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