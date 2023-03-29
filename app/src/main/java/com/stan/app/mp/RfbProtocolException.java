package com.stan.app.mp;

import java.io.IOException;

class RfbProtocolException extends IOException {
    public RfbProtocolException(String message) {
        super(message);
    }
}