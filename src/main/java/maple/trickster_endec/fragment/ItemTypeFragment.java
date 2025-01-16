package maple.trickster_endec.fragment;

public record ItemTypeFragment(Item item) implements Fragment {
    public static final StructEndec<ItemTypeFragment> ENDEC = StructEndecBuilder.of(
            MinecraftEndecs.ofRegistry(Registries.ITEM).fieldOf("item", ItemTypeFragment::item),
            ItemTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.ITEM_TYPE;
    }

    @Override
    public Text asText() {
        return item.getName();
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