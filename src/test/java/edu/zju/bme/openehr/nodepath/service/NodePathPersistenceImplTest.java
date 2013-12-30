package edu.zju.bme.openehr.nodepath.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		List<String> dadls = new ArrayList<String>();
		dadls.add(readLines("patient1.dadl"));
		dadls.add(readLines("patient2.dadl"));
		
		assertEquals(nodePathPersistence.insert(dadls), 0);
	}
	
	@Test
	public void testSelectCoarseNodePathByObjectUids() throws Exception {
		List<String> objectUids = new ArrayList<String>();
		objectUids.add("patient1");
		List<CoarseNodePath> listCoarseNodePath = nodePathPersistence.selectCoarseNodePathByObjectUids(objectUids);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 1);
		objectUids.add("patient2");
		listCoarseNodePath = nodePathPersistence.selectCoarseNodePathByObjectUids(objectUids);
		assertNotNull(listCoarseNodePath);
		assertEquals(listCoarseNodePath.size(), 2);
	}

	protected String readLines(String name) throws IOException {
		StringBuilder result = new StringBuilder();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = reader.readLine();
		while (line != null) {
			result.append(line);
			result.append("\n");
			line = reader.readLine();
		}
		reader.close();
		
		return result.toString();
	}

}
