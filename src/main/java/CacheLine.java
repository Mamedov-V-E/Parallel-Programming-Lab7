import org.zeromq.ZFrame;

public class CacheLine {
    private String id;
    private ZFrame address;
    private int minKey;
    private int maxKey;
    private long hertbeatTime;

    public CacheLine(String id, ZFrame address, int minKey, int maxKey, long hertbeatTime) {
        this.id = id;
        this.address = address;
        this.minKey = minKey;
        this.maxKey = maxKey;
        this.hertbeatTime = hertbeatTime;
    }

    public String getId() {
        return id;
    }

    public ZFrame getAddress() {
        return address;
    }

    public int getMinKey() {
        return minKey;
    }

    public int getMaxKey() {
        return maxKey;
    }

    public void setHertbeatTime(long hertbeatTime) {
        this.hertbeatTime = hertbeatTime;
    }

    public boolean isDead() {
        return System.currentTimeMillis() >
    }
}
