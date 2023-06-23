package com.app.Controllers;
import java.io.IOException;
import java.time.Year;
import java.util.List;

import com.app.Models.AcademicYear;
import com.app.Models.Boards;
import com.app.Models.SchoolBoard;
import com.app.Services.SchoolService.SchoolService;
import com.app.Services.TrashBinService.TrashBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.Models.School;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/school")
public class SchoolController {

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private TrashBinService trashBinService;

	@PreAuthorize("hasAnyRole('Administrator')")
	@PostMapping("/add")
	public ResponseEntity<?> addSchool(@RequestBody @Valid School school)
	{
		return schoolService.addSchool(school);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@PostMapping("/academic-year/add")
	public ResponseEntity<?> addAcademicYearToSchool(@RequestBody AcademicYear academicYear,@RequestParam("branch-code") String branchCode)
	{
		return schoolService.addAcademicYearToSchool(academicYear, branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','AcademicAuditor')")
	@GetMapping("/academic-year/get")
	public ResponseEntity<?> getAcademicYear(@RequestParam("year-from") Year yearFrom ,@RequestParam("branch-code") String branchCode)
	{
		return schoolService.getAcademicYear(yearFrom, branchCode);
	}
	@PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','Teacher')")
	@GetMapping("/academic-year/get/all")
	public ResponseEntity<?> getAllAcademicYears(@RequestParam("branch-code") String branchCode)
	{
		return schoolService.getAllAcademicYears(branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@PostMapping("/academic-year/upload")
	public ResponseEntity<?> uploadJsonFileForAcademicYear(@RequestParam("branch-code")String branchCode,@RequestParam("year-from") Year yearFrom,@RequestParam("file") MultipartFile file)
	{
		try {
			return schoolService.uploadDataToAcademicYear(branchCode,yearFrom,file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@GetMapping("/academic-year/download")
	public ResponseEntity<?> downloadJsonFileOfAcademicYear(@RequestParam("branch-code") String branchCode ,@RequestParam("year-from") Year yearFrom)
	{
		return schoolService.downloadAcademicYearFromDatabase(branchCode,yearFrom);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/academic-year/trash/move")
	public ResponseEntity<?> moveAcademicYearToTrash(@RequestParam("year-from") Year yearFrom , @RequestParam("branch-code") String branchCode)
	{
		return schoolService.moveYearToTrash(yearFrom,branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/academic-year/trash/undo")
	public ResponseEntity<?> undoAcademicYearToTrash(@RequestParam("year-from") Year yearFrom)
	{
		return trashBinService.undoAcademicYearFromTrashBin(yearFrom);
	}

	@GetMapping("/academic-year/trash/get/all")
	public ResponseEntity<?> getAllAcademicYearsInTrash()
	{
		return trashBinService.getAllAcademicYearsInTrash();
	}

	@DeleteMapping("/academic-year/trash/delete")
	public ResponseEntity<?> deleteAcademicYearPermanently(@RequestParam("year-from") Year yearFrom)
	{
		return trashBinService.deleteAcademicYearPermanently(yearFrom);
	}

	@DeleteMapping("/academic-year/trash/delete/all")
	public ResponseEntity<?> deleteAllAcademicYearPermanently()
	{
		return trashBinService.deleteAllAcademicYearsPermanently();
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@PutMapping("/edit")
	public ResponseEntity<?> updateBranchAddress(@RequestBody School school)
	{
		return schoolService.updateBranchAddress(school);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@GetMapping("/get")
	public ResponseEntity<?> getSchool(@RequestParam("branch-code") String branchCode)
	{
		return schoolService.getSchool(branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','Teacher')")
	@GetMapping("/get/all")
	public ResponseEntity<?> getAllBranches(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize, @RequestParam("sort-by") String fieldName)
	{
		return schoolService.getAllBranches(pageNo,pageSize,fieldName);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteSchool(@RequestParam("branch-code") String branchCode)
	{
		return schoolService.deleteSchool(branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@PostMapping("/board/add")
	public ResponseEntity<?> addBoard(@RequestParam("board") Boards board,@RequestParam("year-from") Year academicYearfrom,@RequestParam("branch-code") String branchCode)
	{
		return schoolService.addBoard(board, academicYearfrom, branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@GetMapping("/board/get")
	public ResponseEntity<?> getBoard(@RequestParam("board") Boards board,@RequestParam("year-from") Year academicYearfrom,@RequestParam("branch-code") String branchCode)
	{
		return schoolService.getBoard(board, academicYearfrom, branchCode);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','Teacher')")
	@GetMapping("/board/get/all")
	public ResponseEntity<?> getAllBoards(@RequestParam("branch-code") String branchCode,@RequestParam("year-from") Year academicYearfrom)
	{
		return schoolService.getAllBoards(branchCode, academicYearfrom);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping ("/board/trash/move")
	public ResponseEntity<?> moveBoardToTrashBin(@RequestParam("branch-code") String branchCode,@RequestParam("year-from") Year academicYear,@RequestParam("board") Boards board)
	{
		return trashBinService.moveBoardToTrashBin(branchCode,academicYear,board);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/board/trash/undo")
	public ResponseEntity<?> undoBoardFromTrashBin(@RequestParam("board") Boards board)
	{
		return trashBinService.undoBoardFromTrashBin(board);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@GetMapping("/board/trash/get/all")
	public ResponseEntity<?> getAllBoardInTrash()
	{
		return trashBinService.getAllBoardInTrash();
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/board/trash/delete")
	public ResponseEntity<?> deleteBoardPermanently(@RequestParam("board") Boards board)
	{
		return trashBinService.deleteBoardPermanently(board);
	}

	@PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
	@DeleteMapping("/board/trash/delete/all")
	public ResponseEntity<?> deleteAllBoardsPermanently()
	{
		return trashBinService.deleteAllBoardsPermanently();
	}

}
