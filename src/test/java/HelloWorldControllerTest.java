import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.activiti6.Activiti6DemoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Activiti6DemoApplication.class)
public class HelloWorldControllerTest {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;

	@Test
	public void t2(){
	   runtimeService.startProcessInstanceByKey("more");
	}
	
	@Test
	public void t3(){
		Map<String,Object> map=new HashMap<>();
		map.put("more", true);
		taskService.complete("15005",map);
	}
	
	
}
