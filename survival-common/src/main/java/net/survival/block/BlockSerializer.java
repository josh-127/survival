package net.survival.block;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.reflections.Reflections;

import net.survival.block.state.BlockState;

final class BlockSerializer
{
    private static final HashMap<Class<?>, Integer> classToTagMap = new HashMap<>();
    private static final HashMap<Integer, Class<?>> tagToClassMap = new HashMap<>();

    private BlockSerializer() {}

    static {
        var reflection = new Reflections(BlockState.class);
        var blockClazzes = reflection.getTypesAnnotatedWith(BlockSerializable.class);

        for (var clazz : blockClazzes) {
            var annotation = clazz.getAnnotation(BlockSerializable.class);
            var tag = annotation.value();

            classToTagMap.put(clazz, tag);
            tagToClassMap.put(tag, clazz);
        }
    }

    public static void serializeBlock(BlockState block, ByteBuffer buffer) {
        var blockClazz = block.getClass();
        var tag = classToTagMap.get(blockClazz);

        if (tag == null) {
            var annotation = blockClazz.getAnnotation(BlockSerializable.class);

            if (annotation == null) {
                throw new BlockSerializerException(String.format(
                        "%s is missing a @%s annotation.",
                        blockClazz.getName(),
                        BlockSerializable.class.getName()));
            }

            tag = annotation.value();

            var otherClazz = tagToClassMap.get(tag);

            if (otherClazz != null) {
                throw new BlockSerializerException(String.format(
                        "%s's tag (%X) collides with %s's.",
                        blockClazz.getName(),
                        tag,
                        otherClazz.getName()));
            }

            classToTagMap.put(blockClazz, tag);
            tagToClassMap.put(tag, blockClazz);
        }

        buffer.putInt(tag);

        // TODO: Serialize block state.
    }

    public static BlockState deserializeBlock(ByteBuffer buffer) {
        var tag = buffer.getInt();
        var blockClazz = tagToClassMap.get(tag);

        if (blockClazz == null) {
            throw new BlockSerializerException(String.format(
                    "Tag %X has no corresponding block class.",
                    tag));
        }

        try {
            var constructor = blockClazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            var instance = (BlockState) constructor.newInstance();
            // TODO: De-serialize block state.

            return instance;
        }
        catch (NoSuchMethodException e) {
            throw new BlockSerializerException(String.format(
                    "%s is missing a default constructor.",
                    blockClazz.getName()));
        }
        catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | SecurityException e)
        {
            throw new RuntimeException(e);
        }
    }
}