package net.mrgregorix.variant.rpc.api.service;

import net.mrgregorix.variant.rpc.api.base.RpcServiceName;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;

public interface RpcService
{
    default String getName()
    {
        return AnnotationUtils.getAnnotation(this.getClass(), RpcServiceName.class).value();
    }
}
