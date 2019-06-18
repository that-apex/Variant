package net.mrgregorix.variant.rpc.network.netty.utils;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;

/**
 * Utilities for manipulating {@link ByteBuf}s
 */
public class BufferUtils
{
    /**
     * Writes the given string to the given buffer.
     *
     * @param buf    buffer that the string be written to
     * @param string string to write
     */
    public static void writeString(final ByteBuf buf, final String string)
    {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buf.writeShort(bytes.length);
        buf.writeBytes(bytes);
    }

    /**
     * Reads a string from the given buffer.
     *
     * @param buf buffer that the string will be read from
     *
     * @return the read string
     */
    public static String readString(final ByteBuf buf)
    {
        final byte[] bytes = new byte[buf.readShort()];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private BufferUtils()
    {
    }
}
