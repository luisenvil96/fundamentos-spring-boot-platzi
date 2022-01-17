package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;

	private UserRepository userRepository;

	private UserService userService;

	@Autowired
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency,
								  MyBean myBean,
								  MyBeanWithDependency myBeanWithDependency,
								  MyBeanWithProperties myBeanWithProperties,
								  UserPojo userPojo,
								  UserRepository userRepository,
								  UserService userService) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional() {
		User test1 = new User("Test1Transactional1", "Test1Transactional1@domain.com", LocalDate.now());
		User test2 = new User("Test2Transactional2", "Test2Transactional2@domain.com", LocalDate.now());
		User test3 = new User("Test3Transactional3", "Test1Transactional1@domain.com", LocalDate.now());
		User test4 = new User("Test4Transactional4", "Test4Transactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try {
			userService.saveTransactional(users);
		} catch (Exception e) {
			LOGGER.error("Esta es una exception dentro del metodo Transaccional " + e);
		}

		userService.getAllUsers()
				.stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transaccional: " + user));
	}

	private void getInformationJpqlFromUser() {
		/*
		LOGGER.info("Usuario con el metodo findByUserEmail: " +
				userRepository.findByUserEmail("julie@gmail.com")
						.orElseThrow(() -> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("user", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo sort " + user));

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method" + user));

		LOGGER.info("Usuario con query method findByEmailAndName: " + userRepository.findByEmailAndName("daniela@gmail.com", "Daniela")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%J%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike: " + user));

		userRepository.findByNameOrEmail("user10", null)
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail: " + user));
		*/
		userRepository.findByBirthDateBetween(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 2))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo findByBirthDateBetween: " + user));

		userRepository.findByNameContainingOrderByIdDesc("user")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo findByNameContainingOrderByIdDesc: " + user));

		LOGGER.info("Usuario con metodo getAllByBirthDateAndEmail: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(2021, 07, 21), "daniela@gmail.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario a partir del namedParameter")));

	}

	private void saveUsersInDataBase() {
		User user1 = new User("John", "john@gmail.com", LocalDate.of(2021, 03, 20));
		User user2 = new User("John", "julie@gmail.com", LocalDate.of(2021, 05, 21));
		User user3 = new User("Daniela", "daniela@gmail.com", LocalDate.of(2021, 07, 21));
		User user4 = new User("user4", "user4@gmail.com", LocalDate.of(2021, 07, 7));
		User user5 = new User("user5", "user5@gmail.com", LocalDate.of(2021, 11, 11));
		User user6 = new User("user6", "user6@gmail.com", LocalDate.of(2021, 2, 25));
		User user7 = new User("user7", "user7@gmail.com", LocalDate.of(2021, 3, 11));
		User user8 = new User("user8", "user8@gmail.com", LocalDate.of(2021, 4, 12));
		User user9 = new User("user9", "user9@gmail.com", LocalDate.of(2021, 5, 22));
		User user10 = new User("user10", "user10@gmail.com", LocalDate.of(2021, 8, 3));
		User user11 = new User("user11", "user11@gmail.com", LocalDate.of(2021, 1, 12));
		User user12 = new User("user12", "user12@gmail.com", LocalDate.of(2021, 2, 2));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12);
		list.stream().forEach(userRepository::save);
//		Otras opciones de guardar en la db son...
//		list.forEach(userRepository::save);
//		userRepository.saveAll(list);
	}


	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword() + "-" + userPojo.getAge());
		try {
			int value = 10/0;
			LOGGER.debug("Mi valor: " + value);
		} catch	(Exception e) {
			LOGGER.error("Esto es un error al dividir por cero" + e.getStackTrace());
		}
	}
}
