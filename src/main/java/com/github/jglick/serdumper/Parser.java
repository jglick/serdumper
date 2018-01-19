package com.github.jglick.serdumper;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.io.ObjectStreamConstants.*;
import org.apache.commons.io.input.CountingInputStream;

/** @see <a href="https://docs.oracle.com/javase/8/docs/platform/serialization/spec/protocol.html">Object Serialization Stream Protocol</a> */
public class Parser {

    private final DataInput input;
    private final CountingInputStream count;
    private int depth;

    public Parser(InputStream is) {
        count = new CountingInputStream(is);
        input = new DataInputStream(count);
    }

    public void stream() throws IOException {
        magic();
        version();
        contents();
    }

    private void magic() throws IOException {
        expect(STREAM_MAGIC, input.readShort());
    }

    private void version() throws IOException {
        expect(STREAM_VERSION, input.readShort());
    }

    private void contents() throws IOException {
        content();
        while (count.available() > 0) {
            content();
        }
    }

    private void content() throws IOException {
        byte tc = input.readByte();
        switch (tc) {
        case TC_NULL:
            nullReference();
            break;
        case TC_OBJECT:
            // TODO
            break;
        default:
            unsupported("TC: 0x%x", tc);
        }
    }

    private void nullReference() {
        log("null");
    }

    private void newObject() {
//        classDesc();
//        newHandle();
//        classdata(); // TODO array
    }

    private void expect(short expected, short actual) throws IOException {
        if (actual != expected) {
            throw new IOException(String.format("@%d expected 0x%x but got 0x%x", count.getByteCount(), expected, actual));
        }
    }
    
    private void unsupported(String msg, Object... args) throws IOException {
            throw new IOException(String.format("UNSUPPORTED: " + msg, args));
    }

    private void log(String msg, Object... args) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.printf(msg, args);
    }

}
