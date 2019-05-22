package com.fab.devportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fab.devportal.dao.ApiDescTbl;
import com.fab.devportal.dao.SubApiTbl;
import com.fab.devportal.repo.SubAPIRepository;
import com.fab.devportal.service.APIDetailsService;
import com.fab.devportal.service.SubApiService;
import com.fab.devportal.utility.SwaggerUtility;

import ch.qos.logback.classic.Logger;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("fabapis")
public class WelcomeController {

    // inject via application.properties
    @Value("${welcome.message}")
    private String message;
    
    @Autowired 
	private SubApiService subApiService;
    
    @Autowired
    private APIDetailsService apiDtlsService;

    //private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String main(Model model) {
        //model.addAttribute("message", message);
        //model.addAttribute("tasks", tasks);

        //return "welcome"; //view
    	
    	Iterable<SubApiTbl> subApiLists = subApiService.findAll();
    	model.addAttribute("subapilist", subApiLists);
    	
    	return "index";
    }
    
    @GetMapping("/welcome")
    public String api(Model model) {
        //model.addAttribute("message", message);
        //model.addAttribute("tasks", tasks);

        //return "welcome"; //view
    	return "welcome";
    }

    @GetMapping("/smartbank")
    public String smartBank(Model model) {

        List<SubApiTbl> subApiLists = subApiService.findByParentApiName("Smart Bank");
        
        model.addAttribute("subapilist", subApiLists);

        return "smartbank"; //view
    }
    
    @GetMapping("/openbank")
    public String openBank(Model model) {

        List<SubApiTbl> subApiLists = subApiService.findByParentApiName("Open Bank");
        
        model.addAttribute("subapilist", subApiLists);

        return "openbank"; //view
    }
    
    @PostMapping("/apidetails")
    public String apiDetails(@RequestParam Long id,  Model model) {
    	
    	System.out.println("Id Received from Smart bank page" + id);

        Optional<ApiDescTbl> apiDetailsList =apiDtlsService.findById(id);
        
        model.addAttribute("apidetails", apiDetailsList.get());
        
        System.out.println("apiDetailsList.get()" + apiDetailsList.get().getId());

        return "apidescription"; //view
    }
    
    @PostMapping("/swagger")
    public String swaggerDetails(@RequestParam String swaggerName, Model model) {
    	
    	System.out.println("Swagger Name Received from Smart bank page" + swaggerName);
    	
    	String path ="/Users/fab/Downloads/API/" + swaggerName;
    	
    	Swagger swagger = new SwaggerParser().read(path);
    	
		System.out.println(swagger.getInfo().getDescription());
		
		Map <String, String> swaggerComps = new HashMap<String, String>(); 
		
		
		for (Map.Entry<String, Path> entry : swagger.getPaths().entrySet()) {
			
			System.out.println(entry.getKey());
			swaggerComps.put("path", entry.getKey());
			
			SwaggerUtility swaggerUtility = new SwaggerUtility();
			
			//ssswaggerComps =swaggerUtility.printOperations(swagger, entry.getValue().getOperationMap(), swaggerComps);
			
			
			
		}

        return "swagger"; //view
    }

}