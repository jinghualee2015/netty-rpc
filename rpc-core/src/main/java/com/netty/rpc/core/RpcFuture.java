package com.netty.rpc.core;

import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcFuture<T> {
    private Promise<T> promised;

    private long timeout;


}
