/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.handler.codec.http.websocketx.extensions;

import io.netty.channel.*;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.internal.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This handler negotiates and initializes the WebSocket Extensions.
 * <p>
 * It negotiates the extensions based on the client desired order,
 * ensures that the successfully negotiated extensions are consistent between them,
 * and initializes the channel pipeline with the extension decoder and encoder.
 * <p>
 * Find a basic implementation for compression extensions at
 * <tt>io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler</tt>.
 */
public class WebSocketServerExtensionHandler extends ChannelDuplexHandler {

    private final List<WebSocketServerExtensionHandshaker> extensionHandshakers;

    private List<WebSocketServerExtension> validExtensions;

    /**
     * Constructor
     *
     * @param extensionHandshakers The extension handshaker in priority order. A handshaker could be repeated many times
     *                             with fallback configuration.
     */
    public WebSocketServerExtensionHandler(WebSocketServerExtensionHandshaker... extensionHandshakers) {
        ObjectUtil.checkNotNull(extensionHandshakers, "extensionHandshakers");
        if (extensionHandshakers.length == 0) {
            throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
        }
        this.extensionHandshakers = Arrays.asList(extensionHandshakers);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            if (WebSocketExtensionUtil.isWebsocketUpgrade(request.headers())) {
                String extensionsHeader = request.headers().getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);

                if (extensionsHeader != null) {
                    List<WebSocketExtensionData> extensions =
                            WebSocketExtensionUtil.extractExtensions(extensionsHeader);
                    int rsv = 0;

                    for (WebSocketExtensionData extensionData : extensions) {
                        Iterator<WebSocketServerExtensionHandshaker> extensionHandshakersIterator =
                                extensionHandshakers.iterator();
                        WebSocketServerExtension validExtension = null;

                        while (validExtension == null && extensionHandshakersIterator.hasNext()) {
                            WebSocketServerExtensionHandshaker extensionHandshaker =
                                    extensionHandshakersIterator.next();
                            validExtension = extensionHandshaker.handshakeExtension(extensionData);
                        }

                        if (validExtension != null && ((validExtension.rsv() & rsv) == 0)) {
                            if (validExtensions == null) {
                                validExtensions = new ArrayList<WebSocketServerExtension>(1);
                            }
                            rsv = rsv | validExtension.rsv();
                            validExtensions.add(validExtension);
                        }
                    }
                }
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpHeaders headers = ((HttpResponse) msg).headers();

            if (WebSocketExtensionUtil.isWebsocketUpgrade(headers)) {

                if (validExtensions != null) {
                    String headerValue = headers.getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);

                    for (WebSocketServerExtension extension : validExtensions) {
                        WebSocketExtensionData extensionData = extension.newReponseData();
                        headerValue = WebSocketExtensionUtil.appendExtension(headerValue,
                                extensionData.name(),
                                extensionData.parameters());
                    }
                    promise.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) {
                            if (future.isSuccess()) {
                                for (WebSocketServerExtension extension : validExtensions) {
                                    WebSocketExtensionDecoder decoder = extension.newExtensionDecoder();
                                    WebSocketExtensionEncoder encoder = extension.newExtensionEncoder();
                                    ctx.pipeline()
                                            .addAfter(ctx.name(), decoder.getClass().getName(), decoder)
                                            .addAfter(ctx.name(), encoder.getClass().getName(), encoder);
                                }
                            }
                        }
                    });

                    if (headerValue != null) {
                        headers.set(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
                    }
                }

                promise.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (future.isSuccess()) {
                            ctx.pipeline().remove(WebSocketServerExtensionHandler.this);
                        }
                    }
                });
            }
        }

        super.write(ctx, msg, promise);
    }
}
