package maple.trickster_endec.fragment;

import io.netty.buffer.Unpooled;
import io.wispforest.endec.Endec;
import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.format.bytebuf.ByteBufDeserializer;
import io.wispforest.endec.format.bytebuf.ByteBufSerializer;
import io.wispforest.endec.format.data.DataInputDeserializer;
import io.wispforest.endec.format.data.DataOutputSerializer;
import maple.trickster_endec.Identifier;
import maple.trickster_endec.endecs.EndecTomfoolery;
import maple.trickster_endec.spell_instruction.SerializedSpellInstruction;
import maple.trickster_endec.spell_instruction.SpellInstruction;
import maple.trickster_endec.spell_instruction.SpellInstructionType;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public interface Fragment extends SpellInstruction {
    final int MAX_WEIGHT = 64000;

    @SuppressWarnings("unchecked")
    final StructEndec<Fragment> ENDEC = EndecTomfoolery.lazy(() -> (StructEndec<Fragment>) Endec.dispatchedStruct(
                    FragmentType::endec,
                    Fragment::type,
                    Endec.<FragmentType<?>>ifAttr(EndecTomfoolery.UBER_COMPACT_ATTRIBUTE, Endec.INT.xmap(FragmentType::getFromInt, FragmentType::getIntId))
                            .orElse(Identifier.ENDEC.xmap(FragmentType.REGISTRY::get, FragmentType::getID))
            )
    );

    @Override
    default SerializedSpellInstruction asSerialized() {
        return new SerializedSpellInstruction(SpellInstructionType.FRAGMENT, this);
    }

    byte[] GZIP_HEADER = new byte[] { 0x1f, (byte) 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xff };

    default String toBase64() {
        return Base64.getEncoder().encodeToString(toBytes());
    }

    default byte[] toBytes() {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);
        try {
            dataOut.writeByte(4); // Protocol version
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ENDEC.encode(
                SerializationContext.empty().withAttributes(
                        EndecTomfoolery.UBER_COMPACT_ATTRIBUTE,
                        EndecTomfoolery.PROTOCOL_VERSION_ATTRIBUTE.instance((byte) 4)
                ),
                DataOutputSerializer.of(dataOut), this
        );

        var byteStream = new ByteArrayOutputStream();
        try (byteStream) {
            try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream)) {
                zipStream.write(byteOut.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException("Fragment encoding broke. what.");
        }


        var bytes = byteStream.toByteArray();
        byte[] result;
        result = Arrays.copyOfRange(bytes, 10, bytes.length);


        return result;
    }

    static Fragment fromBase64(String string) {
        return fromBytes(Base64.getDecoder().decode(string.strip()));
    }

    static Fragment fromBytes(byte[] bytes) {
        ByteArrayInputStream byteIn;
        DataInputStream dataIn;

        var byteStream = new ByteArrayInputStream(ArrayUtils.addAll(GZIP_HEADER, bytes));
        try (byteStream) {
            try (GZIPInputStream zipStream = new GZIPInputStream(byteStream)) {
                byteIn = new ByteArrayInputStream(zipStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Fragment decoding broke. what.");
        }
        dataIn = new DataInputStream(byteIn);

        byte protocolVersion = 0;
        try {
            protocolVersion = dataIn.readByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (protocolVersion < 3) {
            return SpellPart.fromBytesOld(protocolVersion, dataIn);
        } else {
            Fragment result;
            result = ENDEC.decode(
                    SerializationContext.empty().withAttributes(
                            EndecTomfoolery.UBER_COMPACT_ATTRIBUTE,
                            EndecTomfoolery.PROTOCOL_VERSION_ATTRIBUTE.instance(protocolVersion)
                    ),
                    DataInputDeserializer.of(dataIn)
            );

            return result;
        }
    }

    FragmentType<?> type();
}
