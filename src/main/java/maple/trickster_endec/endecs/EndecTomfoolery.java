package maple.trickster_endec.endecs;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function3;
import io.wispforest.endec.*;
import io.wispforest.endec.impl.StructEndecBuilder;

import java.util.*;
import java.util.function.Function;

public class EndecTomfoolery {

    public static class UndashedUuid {
        public static UUID fromString(final String string) {
            if (string.indexOf('-') != -1) {
                throw new IllegalArgumentException("Invalid undashed UUID string: " + string);
            }
            return fromStringLenient(string);
        }

        public static UUID fromStringLenient(final String string) {
            return java.util.UUID.fromString(string.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }

        public static String toString(final UUID uuid) {
            return uuid.toString().replace("-", "");
        }
    }


    public record Vector(
              double x,
              double y,
              double z) {

        @Override
        public String toString() {
            return "Vector[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "z=" + z + ']';
        }
    }

    public record VectorI(
              int x,
              int y,
              int z) {

        @Override
        public String toString() {
            return "Vector[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "z=" + z + ']';
        }
    }


    public static final Endec<VectorI> ALWAYS_READABLE_BLOCK_POS =
            vectorEndec(Endec.INT, VectorI::new, VectorI::x, VectorI::y, VectorI::z);

    public static final Endec<UUID> UUID = Endec.STRING.xmap(UndashedUuid::fromStringLenient, java.util.UUID::toString);
    public static final SerializationAttribute.Marker UBER_COMPACT_ATTRIBUTE = SerializationAttribute.marker("uber_compact");
    public static final SerializationAttribute.WithValue<Byte> PROTOCOL_VERSION_ATTRIBUTE = SerializationAttribute.withValue("protocol_version");
    public static Endec<Vector> VECTOR_3D_ENDEC = EndecTomfoolery.<Double, Vector>vectorEndec(Endec.DOUBLE, Vector::new, Vector::x, Vector::y, Vector::z);
    public static final SerializationAttribute.Marker CODEC_SAFE = SerializationAttribute.marker("codec_safe");

    public static <C, V> Endec<V> vectorEndec(Endec<C> componentEndec, Function3<C, C, C, V> constructor, Function<V, C> xGetter, Function<V, C> yGetter, Function<V, C> zGetter) {
        return componentEndec.listOf().validate(ints -> {
            if (ints.size() != 3) {
                throw new IllegalStateException("vector array must have three elements");
            }
        }).xmap(
                components -> constructor.apply(components.get(0), components.get(1), components.get(2)),
                vector -> List.of(xGetter.apply(vector), yGetter.apply(vector), zGetter.apply(vector))
        );
    }

    public static <T, A extends T> Endec<T> withAlternative(Endec<T> primary, Endec<A> alternative) {
        return new EitherEndec<T, A>(
                primary,
                alternative,
                false
        ).xmap(
                (either) -> either.map(Function.identity(), Function.identity()),
                Either::left
        );
    }

    public static <T> Endec<Optional<T>> safeOptionalOf(Endec<T> endec) {
        return Endec.ifAttr(CODEC_SAFE, Endec.<Optional<T>>of(
                (ctx, serializer, value) -> {
                    try (var struct = serializer.struct()) {
                        struct.field("present", ctx, Endec.BOOLEAN, value.isPresent());
                        value.ifPresent(t -> struct.field("value", ctx, endec, t));
                    }
                },
                (ctx, deserializer) -> {
                    var struct = deserializer.struct();
                    //noinspection DataFlowIssue
                    if (struct.field("present", ctx, Endec.BOOLEAN)) {
                        //noinspection DataFlowIssue
                        return Optional.of(struct.field("value", ctx, endec));
                    } else {
                        return Optional.empty();
                    }
                }
        )).orElse(endec.optionalOf());
    }

    public static <T> Endec<Stack<T>> stackOf(Endec<T> endec) {
        return endec.listOf().xmap(l -> {
            var stack = new Stack<T>();
            stack.addAll(l);
            return stack;
        }, ArrayList::new);
    }

    public static <T> StructEndec<T> funnyFieldOf(Endec<T> endec, String key) {
        return StructEndecBuilder.of(
                endec.fieldOf(key, Function.identity()),
                Function.identity()
        );
    }

    public static <T> StructEndec<T> recursive(Function<StructEndec<T>, StructEndec<T>> wrapped) {
        return new RecursiveStructEndec<>(wrapped);
    }

    public static <T> StructEndec<T> lazy(Supplier<StructEndec<T>> supplier) {
        return recursive(e -> supplier.get());
    }

    public static <T> StructEndec<T> unit(T value) {
        return new StructEndec<>() {
            @Override
            public void encodeStruct(SerializationContext ctx, Serializer<?> serializer, Serializer.Struct struct, T value) {
                // no-op
            }

            @Override
            public T decodeStruct(SerializationContext ctx, Deserializer<?> deserializer, Deserializer.Struct struct) {
                return value;
            }
        };
    }

    public static <T> StructEndec<T> unit(Supplier<T> value) {
        return new StructEndec<>() {
            @Override
            public void encodeStruct(SerializationContext ctx, Serializer<?> serializer, Serializer.Struct struct, T value) {
                // no-op
            }

            @Override
            public T decodeStruct(SerializationContext ctx, Deserializer<?> deserializer, Deserializer.Struct struct) {
                return value.get();
            }
        };
    }

    public static <T> Endec<T> protocolVersionAlternatives(Map<Byte, Endec<T>> protocols, Endec<T> defaultProtocol) {
        return new Endec<>() {
            @Override
            public void encode(SerializationContext ctx, Serializer<?> serializer, T value) {
                var protocolVersion = ctx.getAttributeValue(PROTOCOL_VERSION_ATTRIBUTE);
                if (protocolVersion == null) {
                    defaultProtocol.encode(ctx, serializer, value);
                    return;
                }

                var protocol = protocols.get(protocolVersion);
                if (protocol == null) {
                    defaultProtocol.encode(ctx, serializer, value);
                    return;
                }

                protocol.encode(ctx, serializer, value);
            }

            @Override
            public T decode(SerializationContext ctx, Deserializer<?> deserializer) {
                var protocolVersion = ctx.getAttributeValue(PROTOCOL_VERSION_ATTRIBUTE);
                if (protocolVersion == null) {
                    return defaultProtocol.decode(ctx, deserializer);
                }

                var protocol = protocols.get(protocolVersion);
                if (protocol == null) {
                    return defaultProtocol.decode(ctx, deserializer);
                }

                return protocol.decode(ctx, deserializer);
            }
        };
    }

    private static class RecursiveStructEndec<T> implements StructEndec<T> {
        private final Supplier<StructEndec<T>> wrapped;

        RecursiveStructEndec(Function<StructEndec<T>, StructEndec<T>> wrapped) {
            this.wrapped = Suppliers.memoize(() -> wrapped.apply(this));
        }

        @Override
        public void encodeStruct(SerializationContext ctx, Serializer<?> serializer, Serializer.Struct struct, T value) {
            wrapped.get().encodeStruct(ctx, serializer, struct, value);
        }

        @Override
        public T decodeStruct(SerializationContext ctx, Deserializer<?> deserializer, Deserializer.Struct struct) {
            return wrapped.get().decodeStruct(ctx, deserializer, struct);
        }
    }

    public static <K, V> Endec<HashMap<K, V>> hamt(Endec<K> key, Endec<V> value) {
        return Endec.map(key, value).xmap(HashMap::new, (x) -> x);
    }
}
