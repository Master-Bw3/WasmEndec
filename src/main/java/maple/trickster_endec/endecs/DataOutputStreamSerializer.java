package maple.trickster_endec.endecs;


import io.netty.buffer.ByteBufUtil;
import io.wispforest.endec.Endec;
import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.Serializer;
import io.wispforest.endec.util.VarInts;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DataOutputStreamSerializer<B extends DataOutputStream> implements Serializer<B> {

    private final B stream;

    protected DataOutputStreamSerializer(B buffer) {
        this.stream = buffer;
    }

    public static <B extends DataOutputStream> DataOutputStreamSerializer<B> of(B stream) {
        return new DataOutputStreamSerializer<>(stream);
    }

    // ---

    @Override
    public void writeByte(SerializationContext ctx, byte value) {
        try {
            this.stream.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeShort(SerializationContext ctx, short value) {
        try {
            this.stream.writeShort(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeInt(SerializationContext ctx, int value) {
        try {
            this.stream.writeInt(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeLong(SerializationContext ctx, long value) {
        try {
            this.stream.writeLong(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeFloat(SerializationContext ctx, float value) {
        try {
            this.stream.writeFloat(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeDouble(SerializationContext ctx, double value) {
        try {
            this.stream.writeDouble(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---

    @Override
    public void writeVarInt(SerializationContext ctx, int value) {
        VarInts.writeInt(value, b -> this.writeByte(ctx, b));
    }

    @Override
    public void writeVarLong(SerializationContext ctx, long value) {
        VarInts.writeLong(value, b -> this.writeByte(ctx, b));
    }

    // ---

    @Override
    public void writeBoolean(SerializationContext ctx, boolean value) {
        try {
            this.stream.writeBoolean(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeString(SerializationContext ctx, String value) {
        this.writeVarInt(ctx, ByteBufUtil.utf8Bytes(value));

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        try {
            this.stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBytes(SerializationContext ctx, byte[] bytes) {
        this.writeVarInt(ctx, bytes.length);
        try {
            this.stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> void writeOptional(SerializationContext ctx, Endec<V> endec, Optional<V> optional) {
        this.writeBoolean(ctx, optional.isPresent());
        optional.ifPresent(value -> endec.encode(ctx, this, value));
    }

    // ---

    @Override
    public <V> Map<V> map(SerializationContext ctx, Endec<V> valueEndec, int size) {
        this.writeVarInt(ctx, size);
        return new Sequence<>(ctx, valueEndec);
    }

    @Override
    public <E> Serializer.Sequence<E> sequence(SerializationContext ctx, Endec<E> elementEndec, int size) {
        this.writeVarInt(ctx, size);
        return new Sequence<>(ctx, elementEndec);
    }

    @Override
    public Struct struct() {
        return new Sequence<>(null, null);
    }

    // ---

    @Override
    public B result() {
        return this.stream;
    }

    // ---

    private class Sequence<V> implements Serializer.Sequence<V>, Struct, Map<V> {

        private final SerializationContext ctx;
        private final Endec<V> valueEndec;

        private Sequence(SerializationContext ctx, Endec<V> valueEndec) {
            this.ctx = ctx;
            this.valueEndec = valueEndec;
        }

        @Override
        public void element(V element) {
            this.valueEndec.encode(this.ctx, DataOutputStreamSerializer.this, element);
        }

        @Override
        public void entry(String key, V value) {
            DataOutputStreamSerializer.this.writeString(this.ctx, key);
            this.valueEndec.encode(this.ctx, DataOutputStreamSerializer.this, value);
        }

        @Override
        public <F> Struct field(String name, SerializationContext ctx, Endec<F> endec, F value, boolean mayOmit) {
            endec.encode(ctx, DataOutputStreamSerializer.this, value);
            return this;
        }

        @Override
        public void end() {}
    }
}
