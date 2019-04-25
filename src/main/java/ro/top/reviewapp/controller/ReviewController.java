package ro.top.reviewapp.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.top.reviewapp.entities.Feedback;
import ro.top.reviewapp.repositories.FeedbackRepository;

@Controller	
public class ReviewController {
	private final Path EMERGENCY_PATH = Paths.get("unsaved.txt");
	
	@Autowired
	private FeedbackRepository feedbackRepo;
	
	@GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index(Model model) {
		model.addAttribute("checkedRadio", "average");
		return "index";
	}
	
	@PostMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String review(HttpServletRequest request, Model model, 
			@RequestParam("structure") String structure,
			@RequestParam("materials") String materials,
			@RequestParam("teaching") String teaching,
			@RequestParam("practice") String practice,
			@RequestParam("experience") String experience,
			@RequestParam("comments") String comments) {
				
		Feedback f = new Feedback(
				Integer.parseInt(structure), 
				Integer.parseInt(materials), 
				Integer.parseInt(teaching), 
				Integer.parseInt(practice), 
				Integer.parseInt(experience), 
				request.getParameter("name"),
				comments);
		
		try {
			feedbackRepo.save(f);
		} catch (Exception e) {
			try (BufferedWriter bw = Files.newBufferedWriter(EMERGENCY_PATH, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
				new ObjectMapper().writeValue(
							bw,
							f
						);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return "success";
	}
	
	@GetMapping(path = "/feedback", produces=MediaType.TEXT_HTML_VALUE) 
	public String getReviews(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
		model.addAttribute("data", feedbackRepo.findAll(
					new PageRequest(page < 0 ? 0 : page, 5)
				));
		model.addAttribute("currentPage", page);
		return "reviews";
	}
	
	@GetMapping(path = "/feedback/download", produces = "application/text")
	@ResponseBody
	public ResponseEntity<Object> downloadFeedback() {
		List<Feedback> feedbacks = feedbackRepo.findAll();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		Path path = Paths.get("./", "tempFile" + ".txt");
		
		
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE);) {
			
			for (Feedback f : feedbacks) {
				try {
					mapper.writeValue(bw, f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			InputStreamResource isr = new InputStreamResource(Files.newInputStream(path, StandardOpenOption.DELETE_ON_CLOSE));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename='%s'", "feedbacks_" + LocalDateTime.now() + ".txt"));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			
			ResponseEntity<Object> response = ResponseEntity.ok()
														    .headers(headers)
														    .contentLength(Files.size(path))
														    .contentType(MediaType.parseMediaType("application/text"))
														    .body(isr);
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());		
		}	
		
	}
	
	@PostMapping(path = "/feedback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadDb(Model model, @RequestParam("file") MultipartFile file, @RequestParam(name = "page", defaultValue = "0") int page) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = file.getInputStream();
			
			try {
				List<Feedback> list = mapper.readValue(is, new TypeReference<List<Feedback>>(){});

				for (Feedback f : list) {
					try {
						f.setId(null);
						feedbackRepo.save(f);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				model.addAttribute("message", "Success!");
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
			}

		} catch (Exception e) {
			model.addAttribute("message", "Failed with exception : " + e);
		}
		model.addAttribute("data", feedbackRepo.findAll(new PageRequest(page < 0 ? 0 : page, 5)));
		model.addAttribute("currentPage", page);
		return "reviews";
	}
		
}
