package ch.rasc.sse;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MemoryObserverJob {

  public final ApplicationEventPublisher eventPublisher;

  public MemoryObserverJob(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Scheduled(fixedRate = 1000)
  public void doSomething() {
    MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
    MemoryUsage heap = memBean.getHeapMemoryUsage();
    MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

    MemoryInfo mi = new MemoryInfo(heap.getUsed(), nonHeap.getUsed());
    mi.setId("default");
    this.eventPublisher.publishEvent(mi);
  }

  public void doSomething2(String id) {
	  MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
	  MemoryUsage heap = memBean.getHeapMemoryUsage();
	  MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

	  MemoryInfo2 mi = new MemoryInfo2(heap.getUsed(), nonHeap.getUsed());
	  mi.setId(id);
	  System.out.println(">>>>>>>>>>>>>>>" + id);
	  this.eventPublisher.publishEvent(mi);
  }

}