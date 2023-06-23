package com.app.Helpers;

import com.app.Models.Boards;
import com.app.Models.Standard;
import com.app.Models.Standard;
import com.app.Models.Student;
import com.app.Models.StudentBehaviouralProfile;
import com.app.Models.StudentCoCurriculumActivityProfile;
import com.app.Models.StudentGeneralProfile;
import com.app.Models.StudentSportsProfile;
import com.app.Repositories.StandardRepository;
import com.app.Services.StudentService.StudentService;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CSVHelper {

	public static String TYPE = "text/csv";
	static String[] HEADERs = { "studentName", "studentCode", "studentEmail", "password" };

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StudentService studentService;

	@Autowired
	private FindStandardHelper findStandardHelper;

	@Autowired
	private StandardRepository standardRepository;

	public static boolean hasCSVFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}

	public List<Student> csvToStudents(InputStream inputStream, String standardLevel, Boards board,
			Year yearFrom, String branchCode) {
		Standard foundStandard = findStandardHelper.findStandard(standardLevel, board, yearFrom, branchCode);
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

//            List<Student> students = new ArrayList<Student>();
			List<Student> students = foundStandard.getStudents();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				if (students.stream().filter(s -> s.getStudentCode().equals(csvRecord.get("studentCode"))).findAny()
						.isPresent()) {
					continue;
				} else {
					Student student = new Student();
					student.setStudentName(csvRecord.get("studentName"));
					student.setStudentCode(csvRecord.get("studentCode"));
					student.setStandard(standardLevel);
					student.setYearFrom(yearFrom);
					student.setBoard(board);
					student.setBranchCode(branchCode);
				     student.setStudentGeneralProfile(new StudentGeneralProfile(student.getStudentCode()));
		                student.setStudentSportsProfile(new StudentSportsProfile(student.getStudentCode()));
		                student.setStudentBehaviouralProfile(new StudentBehaviouralProfile(student.getStudentCode()));
		                student.setStudentCoCurriculumActivityProfile(new StudentCoCurriculumActivityProfile(student.getStudentCode()));
		               
					students.add(student);
					
					foundStandard.setNoOfStudents(foundStandard.getNoOfStudents()+1);
				}
			}
			foundStandard.setStudents(students);

			standardRepository.save(foundStandard);
			
			return students;

		} catch (IOException e) {
			throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
		}

	}

	public static ByteArrayInputStream studentsToCSV(List<Student> students) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
			for (Student student : students) {
				List<String> data = Arrays.asList(student.getStudentName(), student.getStudentCode());

				csvPrinter.printRecord(data);
			}

			csvPrinter.flush();
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
		}
	}
}
