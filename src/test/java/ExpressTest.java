import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.MembershipEntity;
import org.activiti.engine.impl.persistence.entity.MembershipEntityImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.el.ExpressionFactoryImpl;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.InterpolationTermState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StreamUtils;

import com.activiti6.Activiti6DemoApplication;
import com.activiti6.cmd.JumpActivitiCmd;
import com.activiti6.entity.NextNode;
import com.activiti6.service.ActivitiService;

import de.odysseus.el.util.SimpleContext;
import net.sf.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Activiti6DemoApplication.class)
public class ExpressTest {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	IdentityService identityService;
	@Autowired
	HistoryService historyService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	ActivitiService activitiService;
	
	
	@Autowired
	TransactionTemplate transactionTemplate;
	
	@Test
	public void test1(){
		ExpressionFactory factory=new ExpressionFactoryImpl();
		SimpleContext simpleContext=new SimpleContext();
		simpleContext.setVariable("a", factory.createValueExpression("1.2", String.class));
		ValueExpression expression= factory.createValueExpression(simpleContext, "${a>1}", Boolean.class);
		System.out.println(expression.getValue(simpleContext));
	}
	@Test
	public void t2(){
		transactionTemplate.execute(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				jdbcTemplate.update(" insert into modeljson(model_id) values('22') ");
				throw new RuntimeException();
			}
		});
	}
	
	@Test
	public void iojson() throws IOException{
		String str= FileUtils.readFileToString(new File("F:\\bpmnjs\\bpmnEditor\\bpmnEditor\\src\\test\\java\\t.json"), "utf-8");
		JSONObject jsonObject=JSONObject.fromObject(str);
		System.out.println(jsonObject);
		
		ExpressionFactory factory=new ExpressionFactoryImpl();
		SimpleContext simpleContext=new SimpleContext();
		simpleContext.setVariable("a", factory.createValueExpression(jsonObject, JSONObject.class));
		ValueExpression expression= factory.createValueExpression(simpleContext, "${a.child[1]>1 and a.name=='a1'}", Boolean.class);
		System.out.println(expression.getValue(simpleContext));
	}
}
