//package cloud.huel;
//
//import cloud.huel.dao.EmpDao;
//import cloud.huel.domain.Emp;
//import com.sun.org.apache.bcel.internal.generic.NEW;
//import org.apache.ibatis.session.SqlSession;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//
///**
// * @author 张晓华
// * @version 1.0
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:applicationContext.xml"})
//public class TestSpring {
//	Random random = new Random();
//	@Autowired
//	SqlSession sqlSession = null;
//	@Test
//	public void test001(){
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		EmpDao dao = applicationContext.getBean(EmpDao.class);
//		List<Emp> list = dao.queryPartOfEmpList();
//		list.forEach((emp)->{
//			System.out.println(emp);
//		});
//	}
//
//
//	@Test
//	public void testBatchCommand(){
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Integer num = 0;
//		for (int i = 0; i < 1000; i++) {
////			null, name, age, gender, did
//			String name = "";
//			int age = 0;
//			String gender = "";
//			Integer did = 0;
//			if (i %2 ==0) {
//				gender = "女";
//				did = 2;
//				if (i % 4 ==0) {
//					did = 4;
//				}
//			}else {
//				gender = "男";
//			}
//			if (i %3 ==0) {
//				did = 3;
//			}else {
//				did = 1;
//			}
//			age = random.nextInt(50);
//			name = UUID.randomUUID().toString().substring(1,6);
//			int [] didArray = {1,2,3,4};
//
//			Emp emp = new Emp();
//			emp.setEmpName(name);
//			emp.setEmpAge(age);
//			emp.setEmpGender(gender);
//			emp.setDid(did);
//			EmpDao dao = sqlSession.getMapper(EmpDao.class);
//			num+= dao.addEmp(emp);
//		}
//		System.out.println(num);
//	}
//}
