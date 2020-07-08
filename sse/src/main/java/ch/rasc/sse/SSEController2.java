package ch.rasc.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SSEController2 {

	private  final Logger log = LoggerFactory.getLogger(SSEController2.class);
	@Autowired
	MemoryObserverJob memoryObserverJob;

  @GetMapping("/input/{id}")
  public @ResponseBody String handle(@PathVariable String id) {
	  log.info("========>" + id);
	  memoryObserverJob.doSomething2(id);
	  return id;
  }


}
