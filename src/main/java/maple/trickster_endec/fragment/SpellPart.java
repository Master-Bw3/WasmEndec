package maple.trickster_endec.fragment;


import io.netty.buffer.ByteBuf;
import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.format.bytebuf.ByteBufDeserializer;
import io.wispforest.endec.format.data.DataInputDeserializer;
import io.wispforest.endec.format.data.DataOutputSerializer;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.SpellUtils;
import maple.trickster_endec.endecs.EndecTomfoolery;
import maple.trickster_endec.spell_instruction.SpellInstruction;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public final class SpellPart implements Fragment {
    public static final StructEndec<SpellPart> ENDEC = EndecTomfoolery.recursive(self -> StructEndecBuilder.of(
            Fragment.ENDEC.fieldOf("glyph", SpellPart::getGlyph),
            EndecTomfoolery.protocolVersionAlternatives(
                    Map.of(
                            (byte) 1, self.listOf()
                    ),
                    EndecTomfoolery.withAlternative(SpellInstruction.STACK_ENDEC.xmap(
                            instructions -> SpellUtils.decodeInstructions(instructions, new Stack<>(), new Stack<>(), Optional.empty()),
                            SpellUtils::flattenNode
                    ), self).listOf()
            ).fieldOf("sub_parts", SpellPart::getSubParts), SpellPart::new
    ));

    public Fragment glyph;

    public List<SpellPart> subParts;

    public SpellPart(Fragment glyph, List<SpellPart> subParts) {
        this.glyph = glyph;
        this.subParts = new ArrayList<>(subParts);
    }

    public SpellPart(Fragment glyph) {
        this(glyph, new ArrayList<>());
    }

    public SpellPart() {
        this(new PatternGlyph());
    }

    public Fragment getGlyph() {
        return glyph;
    }

    public List<SpellPart> getSubParts() {
        return subParts;
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.SPELL_PART;
    }

    @Override
    public int hashCode() {
        return Objects.hash(glyph, subParts);
    }

    @Override
    public String toString() {
        return "SpellPart[" +
                "glyph=" + glyph + ", " +
                "subParts=" + subParts + ']';
    }

    public static SpellPart fromBytesOld(byte protocolVersion, DataInputStream dataInputStream) {
        SpellPart result;
        result = ENDEC.decode(
                SerializationContext.empty().withAttributes(
                        EndecTomfoolery.UBER_COMPACT_ATTRIBUTE,
                        EndecTomfoolery.PROTOCOL_VERSION_ATTRIBUTE.instance(protocolVersion)
                ),
                DataInputDeserializer.of(dataInputStream)
        );

        return result;
    }
}

