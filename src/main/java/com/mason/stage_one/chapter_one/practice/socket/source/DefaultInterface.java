package com.mason.stage_one.chapter_one.practice.socket.source;

/**
 * Created by mwu on 2020/4/9
 * Choose a network interface to be the default for
 * outgoing IPv6 traffic that does not specify a scope_id (and which needs one).
 * <p>
 * Platforms that do not require a default interface may return null
 * which is what this implementation does.
 */

class DefaultInterface {

    static NetworkInterface getDefault() {
        return null;
    }
}