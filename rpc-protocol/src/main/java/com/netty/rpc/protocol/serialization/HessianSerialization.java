package com.netty.rpc.protocol.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Component
@Slf4j
public class HessianSerialization implements RpcSerialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (null == obj) {
            throw new NullPointerException();
        }
        byte[] results;
        HessianSerializerOutput hessianSerializerOutput;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(os);
            hessianSerializerOutput.writeObjectImpl(obj);
            hessianSerializerOutput.flush();
            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        if (null == data) {
            throw new NullPointerException();
        }

        T result;
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianInput = new HessianSerializerInput(is);
            result = (T) hessianInput.readObject(clazz);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return result;
    }
}
