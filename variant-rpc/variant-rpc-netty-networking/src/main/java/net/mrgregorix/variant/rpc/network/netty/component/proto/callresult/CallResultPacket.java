package net.mrgregorix.variant.rpc.network.netty.component.proto.callresult;

import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;
import net.mrgregorix.variant.utils.annotation.Nullable;

public class CallResultPacket extends Packet
{
    private final           int     callId;
    private final           boolean success;
    private @Nullable final byte[]  returnValue;
    private @Nullable final byte[]  exceptionValue;

    private CallResultPacket(final int callId, final boolean success, @Nullable final byte[] returnValue, @Nullable final byte[] exceptionValue)
    {
        super(PacketType.PACKET_CALL_RESULT);
        this.callId = callId;
        this.success = success;
        this.returnValue = returnValue;
        this.exceptionValue = exceptionValue;
    }

    public static CallResultPacket createSuccess(final int callId, final byte[] returnValue)
    {
        return new CallResultPacket(callId, true, returnValue, null);
    }

    public static CallResultPacket createFailure(final int callId, final byte[] exceptionValue)
    {
        return new CallResultPacket(callId, false, null, exceptionValue);
    }

    public int getCallId()
    {
        return this.callId;
    }

    public boolean isSuccess()
    {
        return this.success;
    }

    public byte[] getReturnValue()
    {
        if (! this.isSuccess())
        {
            throw new IllegalArgumentException("Call was not a success");
        }

        return this.returnValue;
    }

    public byte[] getExceptionValue()
    {
        if (this.isSuccess())
        {
            throw new IllegalArgumentException("Call was a success");
        }

        return this.exceptionValue;
    }
}
