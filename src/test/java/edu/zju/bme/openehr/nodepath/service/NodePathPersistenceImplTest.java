package edu.zju.bme.openehr.nodepath.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.zju.bme.snippet.java.FileOperator;

public class NodePathPersistenceImplTest {
	
	NodePathPersistence nodePathPersistence;

	public NodePathPersistenceImplTest() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml", NodePathPersistenceImplTest.class);
		
		nodePathPersistence = (NodePathPersistence) context.getBean("wsclient");
	}

	@Test
	public void testInsert() throws Exception {
		nodePathPersistence.delete("delete from CoarseNodePathEntity");
		nodePathPersistence.delete("delete from CoarseNodePathIndex");
		
		List<String> dadls = new ArrayList<String>();
		dadls.add(FileOperator.INSTANCE.readLinesFromResource("patient1.dadl"));
		dadls.add(FileOperator.INSTANCE.readLinesFromResource("patient2.dadl"));
		
		List<String> adls = new ArrayList<String>();
		adls.add(FileOperator.INSTANCE.readLinesFromFile("../document/knowledge/ZJU/archetype/openEHR-DEMOGRAPHIC-PERSON.patient.v1.adl"));
		
		assertEquals(nodePathPersistence.insert(dadls, adls), 0);
		assertEquals(nodePathPersistence.insert(dadls, adls), 0);
	}
	
	@Test
	public void testSelectCoarseNodePathByObjectUids() throws Exception {
		testInsert();
		
		List<String> objectUids = new ArrayList<String>();
		objectUids.add("patient1");
		List<CoarseNodePathEntity> listCoarseNodePath = 
				nodePathPersistence.selectCoarseNodePathByObjectUids(objectUids);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 1);
		objectUids.add("patient2");
		listCoarseNodePath = nodePathPersistence.selectCoarseNodePathByObjectUids(objectUids);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 2);
	}
	
	@Test
	public void selectCoarseNodePathByPathValues() throws Exception {
		testInsert();
		
		List<String> paths = new ArrayList<>();
		List<String> values = new ArrayList<>();
		paths.add("/details[at0001]/items[at0004]/value/value");
		values.add("1984-08-11T19:20:30+08:00");		
		List<CoarseNodePathEntity> listCoarseNodePath = 
				nodePathPersistence.selectCoarseNodePathByPathValues(paths, values);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 1);
		paths.add("/details[at0001]/items[at0009]/value/value");
		values.add("zhangsan");
		listCoarseNodePath = 
				nodePathPersistence.selectCoarseNodePathByPathValues(paths, values);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 1);
		paths.add("/details[at0001]/items[at0009]/value/value");
		values.add("lisi");
		listCoarseNodePath = 
				nodePathPersistence.selectCoarseNodePathByPathValues(paths, values);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 2);
	}

}
