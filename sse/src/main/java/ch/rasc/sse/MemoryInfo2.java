package ch.rasc.sse;

public class MemoryInfo2 {
  private final long heap;

  private final long nonHeap;

  private final long ts;

  private String id;

  public MemoryInfo2(long heap, long nonHeap) {
    this.ts = System.currentTimeMillis();
    this.heap = heap;
    this.nonHeap = nonHeap;
  }


  public String getId() {
	return id;
}


public void setId(String id) {
	this.id = id;
}


public long getHeap() {
    return this.heap;
  }

  public long getNonHeap() {
    return this.nonHeap;
  }

  public long getTs() {
    return this.ts;
  }


@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("MemoryInfo2 [heap=");
	builder.append(heap);
	builder.append(", nonHeap=");
	builder.append(nonHeap);
	builder.append(", ts=");
	builder.append(ts);
	builder.append(", id=");
	builder.append(id);
	builder.append("]");
	return builder.toString();
}

}
