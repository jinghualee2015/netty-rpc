package com.netty.rpc.protocol.serialization;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class SerializationFactory {
    public static RpcSerialization getRpcSerialization(int serializationType) {
        SerializationTypeEnum typeEnum = SerializationTypeEnum.findType(serializationType);
        switch (typeEnum) {
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal" + serializationType);
        }
    }
}
