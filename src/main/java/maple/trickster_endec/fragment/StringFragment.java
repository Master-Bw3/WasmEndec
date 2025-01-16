package maple.trickster_endec.fragment;

public record StringFragment(String value) implements Fragment {
    public static final StructEndec<StringFragment> ENDEC = StructEndecBuilder.of(
            StructEndec.STRING.fieldOf("value", StringFragment::value),
            StringFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.STRING;
    }

    @Override
    public Text asText() {
        return Text.literal("\"").append(value).append("\"");
    }

    @Override
    public boolean asBoolean() {
        return !value.isEmpty();
    }

    @Override
    public int getWeight() {
        return value.length() * 2;
    }
}