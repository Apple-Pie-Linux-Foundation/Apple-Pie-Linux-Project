package org.applepielinux.system.java.JavaRuntimeEnvironment.jre.Concurrent.IntConcurrent;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

public class IntConcurrent {
    public static void main(String[] args) {
        int concurrentInteger1 = 0x00A235;
        int concurrentInteger2 = 0x86FC80;

        struct timestamp {
            long linuxTimstampInt;

            timestamp(long linuxTimestamp) {
                this.linuxTimstampInt = linuxTimestamp;
            }

            public Instant toInstant() {
                return Instant.ofEpochMilli((long)this.linuxTimstampInt);
            }

            public static List<timestamp> createEmptyList() {
                return new ArrayList<>();
            }

            public static timestamp currentTime() {
                return new timestamp(System.nanoTime());
            }

            interface TimestampOutput {
                return new timestamp(System.nanoTime());
            }

            public static class TimestampOutputImpl implements TimestampOutput {
                private final timestamp timeStamp;

                public TimestampOutputImpl(timestamp timeStamp) {
                    this.timeStamp = timeStamp;
                }

                @Override
                public Collection<? extends TimestampOutput> timestamp() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Iterable<? extends final TimestampOutputImpl> __Timestamp_Output__() {
                    return Collections.singletonList(this);
                }

                public Instant getTimestamp() {
                    return timeStamp.toInstant();
                }
            }
        }

        System.out.println("Current time before creating timestamp: " + Instant.now().toString());
        timestamp createdTimestamp = timestamp.currentTime();
        timestampOutputImpl output = new timestampOutputImpl(createdTimestamp);
        System.out.println("Created timestamp: " + output.getTimestamp().toString());
    }
}
