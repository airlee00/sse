package ch.rasc.sse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class SSEController {
	private  final Logger log = LoggerFactory.getLogger(SSEController.class);

  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<HashMap<String, SseEmitter>> emi = new CopyOnWriteArrayList<>();
 // private final HashMap<String, SseEmitter> emi = new HashMap<>();

  @GetMapping("/memory")
  public SseEmitter handle(HttpServletResponse response) {
    response.setHeader("Cache-Control", "no-store");

    SseEmitter emitter = new SseEmitter();
    // SseEmitter emitter = new SseEmitter(180_000L);

    this.emitters.add(emitter);

    emitter.onCompletion(() -> this.emitters.remove(emitter));
    emitter.onTimeout(() -> this.emitters.remove(emitter));

    return emitter;
  }

  @GetMapping("/memory2/{app}")
  public SseEmitter handle2(@PathVariable String app, HttpServletResponse response) {
	  response.setHeader("Cache-Control", "no-store");

	  SseEmitter emitter = new SseEmitter();
	  // SseEmitter emitter = new SseEmitter(180_000L);
      HashMap<String, SseEmitter> m = new HashMap<String, SseEmitter>();
      m.put(app, emitter);

	  this.emi.add(m);

	  emitter.onCompletion(() -> this.emi.remove(emitter));
	  emitter.onTimeout(() -> this.emi.remove(emitter));

	  return emitter;
  }

  @EventListener
  public void onMemoryInfo22(MemoryInfo2 memoryInfo) {
	  log.info("==onMemoryInfo22===>{}", memoryInfo);
	  List<HashMap> deadEmitters = new ArrayList<>();
	  this.emi.forEach(m -> {
		  m.forEach( (k,v) -> {

			  if(k.equals(memoryInfo.getId())) {
				  SseEmitter e = m.get(memoryInfo.getId());
				  log.info("==onMemoryInfo22===>{}", e);
				  try {
				        e.send(memoryInfo);

				        // close connnection, browser automatically reconnects
				        e.complete();

				        //SseEventBuilder builder = SseEmitter.event().name("second").data("1");
				        // SseEventBuilder builder =
				        // SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
				        // emitter.send(builder);
				      }
				      catch (Exception ee) {
				    	  deadEmitters.add(m);
				      }
			  }
		  });
	  });

	  this.emi.removeAll(deadEmitters);

  }
  @EventListener
  public void onMemoryInfo(MemoryInfo memoryInfo) {
	  log.info("==onMemoryInfo===>{}", memoryInfo);
	  List<SseEmitter> deadEmitters = new ArrayList<>();
	  this.emitters.forEach(emitter -> {
		  try {
			  emitter.send(memoryInfo);

			  // close connnection, browser automatically reconnects
			  // emitter.complete();

			  // SseEventBuilder builder = SseEmitter.event().name("second").data("1");
			  // SseEventBuilder builder =
			  // SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
			  // emitter.send(builder);
		  }
		  catch (Exception e) {
			  deadEmitters.add(emitter);
		  }
	  });

	  this.emitters.removeAll(deadEmitters);
  }

}
