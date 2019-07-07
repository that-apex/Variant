package net.mrgregorix.variant.utils.io;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A helper interface for {@link ByteArrayUtils#prepareDataByteArray(DataPopulator)}
 *
 * @see ByteArrayUtils#prepareDataByteArray(DataPopulator)
 */
@FunctionalInterface
public interface DataPopulator
{
    /**
     * Populate the given {@link DataOutputStream} with bytes.
     *
     * @param data data stream to populate
     *
     * @throws IOException when an IOException occurred, will be rethrown
     */
    void populate(DataOutputStream data) throws IOException;
}
