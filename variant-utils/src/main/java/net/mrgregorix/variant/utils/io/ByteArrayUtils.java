package net.mrgregorix.variant.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Helper utilities for manipulations of byte arrays.
 */
public class ByteArrayUtils
{
    /**
     * Creates a new {@code byte[]} from data supplied by the {@link DataPopulator}
     *
     * @param dataPopulator populator for the data
     *
     * @return the newly created, populated byte array
     *
     * @throws IOException rethrown from {@link DataPopulator#populate(DataOutputStream)}
     */
    public static byte[] prepareDataByteArray(final DataPopulator dataPopulator) throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dataPopulator.populate(new DataOutputStream(baos));
        return baos.toByteArray();
    }

    private ByteArrayUtils()
    {
    }
}
