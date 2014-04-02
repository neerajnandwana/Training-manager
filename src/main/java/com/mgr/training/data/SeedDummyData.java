package com.mgr.training.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.auth.PasswordDigest;
import com.mgr.training.data.Training.Kind;
import com.mgr.training.data.Training.Mode;
import com.mgr.training.store.EmployeeStore;
import com.mgr.training.store.TrainingStore;
import com.mgr.training.store.UserStore;

@Singleton
public class SeedDummyData {
	private final UserStore userStore;
	private final EmployeeStore empStore;
	private final TrainingStore trainingStore;
	private final PasswordDigest passwordDigest;

	@Inject
	public SeedDummyData(UserStore userStore, EmployeeStore empStore, TrainingStore trainingStore, PasswordDigest password) {
		this.userStore = userStore;
		this.empStore = empStore;
		this.trainingStore = trainingStore;
		this.passwordDigest = password;
	}

	public void seedData() throws Exception {
		Futures.successfulAsList(insertUsers(), insertEmployees(), insertTraining());
	}

	private ListenableFuture<List<User>> insertUsers() {
		return userStore.async.add(getUsers());
	}

	private ListenableFuture<List<Employee>> insertEmployees() {
		return empStore.async.add(getEmployees());
	}

	private ListenableFuture<List<Training>> insertTraining() throws Exception {
		return trainingStore.async.add(getTraining());
	}

	private List<User> getUsers() {
		int lastId = 106;
		List<User> users = Lists.newArrayList();
		users.add(buildUser("Robert", "100", "welcome100"));
		users.add(buildUser("Leo", "101", "welcome101"));
		users.add(buildUser("Bret", "102", "welcome102"));
		users.add(buildUser("Steve", "103", "welcome103"));
		users.add(buildUser("Chris", "104", "welcome104"));
		users.add(buildUser("Fred", "105", "welcome105"));
		users.add(buildUser("Dave", "106", "welcome106"));
		
		for(int i=0; i<10; i++){
			lastId ++;
			users.add(buildUser("User"+lastId, ""+lastId, "welcome"+lastId));
		}
		return users;
	}

	private List<Employee> getEmployees() {
		List<Employee> emps = Lists.newArrayList();
		emps.add(buildEmployee("100", "Robert", "robert@app.com", "c2", "106", "Process-1"));
		emps.add(buildEmployee("101", "Leo", "leo@app.com", "c2", "106", "Process-2"));
		emps.add(buildEmployee("102", "Bret", "bret@app.com", "c3", "106", "Process-1"));
		emps.add(buildEmployee("103", "Steve", "steve@app.com", "c2", "106", "Process-1"));
		emps.add(buildEmployee("104", "Chris", "chris@app.com", "c3", "106", "Process-2"));
		emps.add(buildEmployee("105", "Fred", "fred@app.com", "c2", "106", "Process-2"));
		emps.add(buildEmployee("106", "Dave", "dave@app.com", "c1", "", "Process-2"));
		return emps;
	}

	private List<Training> getTraining() throws Exception {
		List<Training> trainings = Lists.newArrayList();
		
		List<Employee> trainers = empStore.find(Lists.newArrayList("103", "101"));
		List<TrainingMetadata> attendees = buildAttendedMetadataFor(Lists.newArrayList("102", "104", "105"));
		trainings.add(buildTraining("T-001", "Java", "Learn about java", Training.Kind.SYSTEMS_AND_TOOLS, Training.Mode.CLASS_ROOM, trainers, attendees,
				DateTime.now().toDate(), DateTime.now().plusDays(3).toDate(), 16));

		trainers = empStore.find(Lists.newArrayList("104"));
		attendees = buildAttendedMetadataFor(Lists.newArrayList("101", "102", "106"));
		trainings.add(buildTraining("T-002", "C++", "Learn about C++", Training.Kind.SYSTEMS_AND_TOOLS, Training.Mode.CLASS_ROOM, trainers, attendees, 
				DateTime.now().toDate(), DateTime.now().plusDays(3).toDate(), 16));

		trainers = empStore.find(Lists.newArrayList("101", "103"));
		attendees = buildAttendedMetadataFor(Lists.newArrayList("102", "104", "105"));
		trainings.add(buildTraining("T-003", "Linux", "Learn about SDLC", Training.Kind.ANALYSIS, Training.Mode.E_LEARNING, trainers, attendees, 
				DateTime.now().toDate(), DateTime.now().plusDays(3).toDate(), 16));

		return trainings;
	}

	private User buildUser(String displayName, String userId, String password) {
		User user = new User();
		user.setDisplayName(displayName);
		user.setUserId(userId);
		user.setPassword(passwordDigest.digest(password));
		return user;
	}

	private Employee buildEmployee(String id, String name, String email, String level, String managerId, String processName) {
		Employee emp = new Employee();
		emp.setId(id);
		emp.setName(name);
		emp.setEmail(email);
		emp.setLevel(level);
		emp.setManagerId(managerId);
		emp.setProcessName(processName);
		return emp;
	}

	private Training buildTraining(String trainingId, String trainingTitle, String description, Kind kind, Mode mode, List<Employee> trainerIds,
			List<TrainingMetadata> attendees, Date trainingStartDate, Date trainingEndDate, int numberOfHours) {
		Training training = new Training();
		training.setTrainingId(trainingId);
		training.setTrainingTitle(trainingTitle);
		training.setDescription(description);
		training.setKind(kind.toString());
		training.setMode(mode.toString());
		training.setTrainerIds(trainerIds);
		training.setMeta(attendees);
		training.setTrainingStartDate(trainingStartDate);
		training.setTrainingEndDate(trainingEndDate);
		training.setNumberOfHours(numberOfHours);
		training.setLevelAllowed("c1,c2,c3");
		return training;
	}

	private List<TrainingMetadata> buildAttendedMetadataFor(ArrayList<String> ids) {
		List<TrainingMetadata> list = Lists.newArrayList();
		for (String id : ids) {
			list.add(buildAttendedMetadata(id));
		}
		return list;
	}

	private TrainingMetadata buildAttendedMetadata(String attendeesId) {
		TrainingMetadata meta = new TrainingMetadata();
		meta.setAttend(true);
		meta.setAttendeesId(attendeesId);
		meta.setFeedback("Feedback by : " + attendeesId);
		meta.setHoursCompleted(5);
		meta.setRating(ramdomeIn5Range());
		meta.setTrainerFeedback("Trainer feed back about attendees: " + attendeesId);
		return meta;
	}

	private int ramdomeIn5Range() {
		Random rn = new Random();
		return rn.nextInt(5) + 1;
	}

}
