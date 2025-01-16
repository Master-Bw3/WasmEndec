package maple.trickster_endec.fragment;

public record EntityTypeFragment(EntityType<?> entityType) implements Fragment {
    public static final StructEndec<EntityTypeFragment> ENDEC = StructEndecBuilder.of(
            MinecraftEndecs.ofRegistry(Registries.ENTITY_TYPE).fieldOf("entity_type", EntityTypeFragment::entityType),
            EntityTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.ENTITY_TYPE;
    }

    @Override
    public Text asText() {
        return entityType.getName();
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
