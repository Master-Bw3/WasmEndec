package maple.trickster_endec.fragment;

public record VoidFragment() implements Fragment {
    public static final VoidFragment INSTANCE = new VoidFragment();
    public static final StructEndec<VoidFragment> ENDEC = EndecTomfoolery.unit(INSTANCE);

    @Override
    public FragmentType<?> type() {
        return FragmentType.VOID;
    }

    @Override
    public Text asText() {
        return Text.literal("void");
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
